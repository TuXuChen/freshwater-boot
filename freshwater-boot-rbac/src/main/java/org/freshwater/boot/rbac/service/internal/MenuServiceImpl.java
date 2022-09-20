package org.freshwater.boot.rbac.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.freshwater.boot.common.constants.enums.NormalStatusEnum;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.common.utils.WdCollectionUtils;
import org.freshwater.boot.rbac.configuration.RbacProperties;
import org.freshwater.boot.rbac.entity.ButtonEntity;
import org.freshwater.boot.rbac.entity.MenuEntity;
import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.entity.RoleMenuMappingEntity;
import org.freshwater.boot.rbac.mapper.MenuMapper;
import org.freshwater.boot.rbac.service.ButtonService;
import org.freshwater.boot.rbac.service.MenuService;
import org.freshwater.boot.rbac.service.RoleMenuMappingService;
import org.freshwater.boot.rbac.service.RoleService;
import org.freshwater.boot.rbac.utils.EntityUtils;
import org.freshwater.boot.rbac.utils.SecurityUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.freshwater.boot.rbac.constants.Constants.ROLE_ADMIN_CODE;
import static org.freshwater.boot.rbac.constants.Constants.USER_ACCOUNT;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {

  @Autowired
  private RbacProperties rbacProperties;
  @Autowired
  private RoleService roleService;
  @Autowired
  private ButtonService buttonService;
  @Autowired
  private RoleMenuMappingService roleMenuMappingService;

  @Override
  @Transactional
  public MenuEntity create(MenuEntity menu) {
    ValidationUtils.validate(menu, CreateValidate.class);
    long count = baseMapper.selectCountByTypeAndCode(menu.getType(), menu.getCode());
    Validate.isTrue(count == 0, "重复编码");
    EntityUtils.initCreatorInfo(menu);
    LocalDateTime now = LocalDateTime.now();
    menu.setCreateTime(now);
    menu.setModifyTime(now);
    baseMapper.insert(menu);
    this.createDefaultRolesMenuMapping(menu);
    return menu;
  }

  @Override
  @Transactional
  public MenuEntity update(MenuEntity menu) {
    ValidationUtils.validate(menu, UpdateValidate.class);
    long count = baseMapper.selectCountByTypeAndCodeAndIdNot(menu.getType(), menu.getCode(), menu.getId());
    Validate.isTrue(count == 0, "重复编码");
    MenuEntity dbMenu = baseMapper.selectById(menu.getId());
    Validate.notNull(dbMenu, "更新的菜单不存在");
    EntityUtils.initModifierInfo(dbMenu);
    dbMenu.setState(menu.getState());
    dbMenu.setType(menu.getType());
    dbMenu.setCode(menu.getCode());
    dbMenu.setName(menu.getName());
    dbMenu.setUrl(menu.getUrl());
    dbMenu.setIcon(menu.getIcon());
    dbMenu.setParentId(menu.getParentId());
    dbMenu.setSortIndex(menu.getSortIndex());
    baseMapper.updateById(dbMenu);
    return dbMenu;
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    Validate.notBlank(id, "主键ID不能为空");
    baseMapper.deleteById(id);
  }

  @Override
  public MenuEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  @Override
  public List<MenuEntity> findTreeByTypeAndState(Integer type, Integer state) {
    if(type == null) {
      return Lists.newArrayList();
    }
    List<MenuEntity> menus = baseMapper.selectByTypeAndState(type, state);
    List<MenuEntity> tree = WdCollectionUtils.list2Tree(menus, MenuEntity::getId,
        MenuEntity::getParentId, MenuEntity::getChildren, (t, u) -> {
          t.setChildren(u);
          return t;
        },
        Comparator.comparing(MenuEntity::getSortIndex));
    // 将root节点中非真正的root节点去掉
    tree = tree.stream().filter(m -> StringUtils.isBlank(m.getParentId())).collect(Collectors.toList());
    // 加载按钮
    List<ButtonEntity> buttons = buttonService.findByTypeAndState(type, state);
    Map<String, MenuEntity> menuMap = WdCollectionUtils.tree2List(tree, MenuEntity::getChildren, null).stream().collect(Collectors.toMap(MenuEntity::getId, v -> v));
    for (ButtonEntity button : buttons) {
      MenuEntity menu = menuMap.get(button.getMenuId());
      if(menu != null) {
        menu.addButton(button);
      }
    }
    return tree;
  }

  @Override
  public List<MenuEntity> findByRoleIdAndType(String roleId, Integer type) {
    if(StringUtils.isBlank(roleId)) {
      return Lists.newArrayList();
    }
    return baseMapper.selectByRoleIdAndType(roleId, type);
  }

  @Override
  public List<MenuEntity> findCheckedTreeByRoleIdAndType(String roleId, Integer type) {
    RoleEntity role = roleService.findById(roleId);
    if (null == role) {
      return Lists.newArrayList();
    }
    List<MenuEntity> roleMenus = this.findByRoleIdAndType(roleId, type);
    List<MenuEntity> tree = this.findTreeByTypeAndState(type, NormalStatusEnum.ENABLE.getStatus());
    Set<String> roleMenuCodeSet = roleMenus.stream().map(MenuEntity::getCode).collect(Collectors.toSet());
    List<MenuEntity> children = tree;
    while (!CollectionUtils.isEmpty(children)) {
      List<MenuEntity> nextChildren = Lists.newArrayList();
      for (MenuEntity child : children) {
        if (role.getCode().equalsIgnoreCase(ROLE_ADMIN_CODE)) {
          child.setChecked(true);
        } else {
          child.setChecked(roleMenuCodeSet.contains(child.getCode()));
        }
        List<ButtonEntity> buttons = buttonService.findCheckedByMenuIdAndRoleIdAndType(child.getId(), roleId, type);
        child.setButtons(buttons);
        nextChildren.addAll(ObjectUtils.defaultIfNull(child.getChildren(), Lists.newArrayList()));
      }
      children = nextChildren;
    }
    return tree;
  }

  @Override
  public List<MenuEntity> findDetailsByRoleIdAndType(String roleId, Integer type) {
    List<MenuEntity> menus = this.findByRoleIdAndType(roleId, type);
    if(!CollectionUtils.isEmpty(menus)) {
      List<ButtonEntity> buttons = buttonService.findByRoleIdAndType(roleId, type);
      if(buttons != null) {
        Map<String, List<ButtonEntity>> menuButtonMap = buttons.stream().collect(Collectors.groupingBy(ButtonEntity::getMenuId));
        for (MenuEntity menu : menus) {
          menu.setButtons(menuButtonMap.get(menu.getId()));
        }
      }
    }
    return menus;
  }

  @Override
  public List<MenuEntity> findTreeByRoleIdAndType(String roleId, Integer type) {
    List<MenuEntity> menus = baseMapper.selectByRoleIdAndType(roleId, type);
    for (MenuEntity menu : menus) {
      List<ButtonEntity> buttons = buttonService.findByRoleIdAndType(roleId, type);
      menu.setButtons(buttons);
    }
    List<MenuEntity> tree = WdCollectionUtils.list2Tree(menus, MenuEntity::getId, MenuEntity::getParentId,
        MenuEntity::getChildren, (o, c) -> {
          o.setChildren(c);
          return o;
        }, Comparator.comparing(MenuEntity::getCreateTime));
    this.handleParent(tree, null);
    return tree;
  }

  @Override
  public List<MenuEntity> findTreeByUserIdAndType(String userId, Integer type) {
    if(StringUtils.isBlank(userId) || type == null) {
      return Lists.newArrayList();
    }
    List<RoleEntity> roles = roleService.findByStateAndUserId(NormalStatusEnum.ENABLE.getStatus(), userId);
    if(CollectionUtils.isEmpty(roles)) {
      return Lists.newArrayList();
    }
    for (RoleEntity role : roles) {
      for (String roleCode : rbacProperties.getIgnoreMethodCheckRoles()) {
        if(role.getCode().equalsIgnoreCase(roleCode)) {
          return this.findTreeByTypeAndState(type, NormalStatusEnum.ENABLE.getStatus());
        }
      }
    }
    List<String> roleIds = roles.stream().map(RoleEntity::getId).collect(Collectors.toList());
    List<MenuEntity> menus = baseMapper.selectByRoleIdsAndTypeAndState(roleIds, type, NormalStatusEnum.ENABLE.getStatus());
    List<MenuEntity> tree = WdCollectionUtils.list2Tree(menus, MenuEntity::getId, MenuEntity::getParentId,
        MenuEntity::getChildren, (o, c) -> {
          o.setChildren(c);
          return o;
        }, Comparator.comparing(MenuEntity::getCreateTime));
    this.handleParent(tree, NormalStatusEnum.ENABLE.getStatus());
    return tree;
  }

  /**
   * 处理菜单的上级
   * @param tree
   * @param state
   */
  private void handleParent(List<MenuEntity> tree, Integer state) {
    /**
     * 大致逻辑为:
     * 如果菜单有上级ID,则根据状态找到这个菜单的根节点,如果找到就加入新的根节点
     * 在根节点中移除当前菜单
     */
    Iterator<MenuEntity> iterator = tree.iterator();
    Map<String, MenuEntity> parentMap = new HashMap<>();
    Map<String, MenuEntity> rootMap = new HashMap<>();
    while (iterator.hasNext()) {
      MenuEntity menu = iterator.next();
      if(StringUtils.isNotBlank(menu.getParentId())) {
        MenuEntity root = this.getRoot(menu, state, parentMap);
        if(root != null) {
          rootMap.put(root.getParentId(), root);
        }
        iterator.remove();
      }
    }
    tree.addAll(rootMap.values());
    Collections.sort(tree, Comparator.comparing(MenuEntity::getSortIndex));
  }

  /**
   * 获取一个菜单的root节点
   * @param menu
   * @param state
   * @return
   */
  private MenuEntity getRoot(MenuEntity menu, Integer state, Map<String, MenuEntity> parentMap) {
    if(StringUtils.isBlank(menu.getParentId())) {
      return menu;
    }
    MenuEntity parent = parentMap.get(menu.getParentId());
    if(parent == null) {
      parent = baseMapper.selectByIdAndState(menu.getParentId(), state);
      if(parent != null) {
        parentMap.put(parent.getId(), parent);
      }
    }
    if(parent == null) {
      return null;
    }
    parent.addChild(menu);
    return this.getRoot(parent, state, parentMap);
  }

  /**
   * 为当前登录用户创建默认角色菜单关联信息
   * @param menu
   */
  private void createDefaultRolesMenuMapping(MenuEntity menu) {
    String userAccount = SecurityUtils.getUserAccount();
    if (userAccount.equals(USER_ACCOUNT)) {
      return;
    }
    List<RoleEntity> roles = roleService.findByUserNameAndState(userAccount, NormalStatusEnum.ENABLE.getStatus());
    if (!CollectionUtils.isEmpty(roles)) {
      roles.forEach(role -> {
        RoleMenuMappingEntity mapping = new RoleMenuMappingEntity();
        mapping.setRoleId(role.getId());
        mapping.setMenuId(menu.getId());
        roleMenuMappingService.create(mapping);
      });
    }
  }

}
