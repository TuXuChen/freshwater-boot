package org.freshwater.boot.rbac.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.rbac.entity.ButtonEntity;
import org.freshwater.boot.rbac.entity.MenuEntity;
import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.entity.RoleMenuMappingEntity;
import org.freshwater.boot.rbac.mapper.RoleMenuMappingMapper;
import org.freshwater.boot.rbac.service.MenuService;
import org.freshwater.boot.rbac.service.RoleButtonMappingService;
import org.freshwater.boot.rbac.service.RoleMenuMappingService;
import org.freshwater.boot.rbac.service.RoleService;

import java.util.List;

/**
 * <p>
 * 角色菜单关联信息 服务实现类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:50
 */
@Service
public class RoleMenuMappingServiceImpl extends ServiceImpl<RoleMenuMappingMapper, RoleMenuMappingEntity> implements RoleMenuMappingService {

  @Autowired
  private RoleService roleService;
  @Autowired
  private MenuService menuService;
  @Autowired
  private RoleButtonMappingService roleButtonMappingService;

  @Override
  @Transactional
  public RoleMenuMappingEntity create(RoleMenuMappingEntity roleMenuMapping) {
    ValidationUtils.validate(roleMenuMapping, CreateValidate.class);
    baseMapper.insert(roleMenuMapping);
    return roleMenuMapping;
  }

  @Override
  @Transactional
  public RoleMenuMappingEntity update(RoleMenuMappingEntity roleMenuMapping) {
    ValidationUtils.validate(roleMenuMapping, UpdateValidate.class);
    RoleMenuMappingEntity dbRoleMenuMapping = baseMapper.selectById(roleMenuMapping.getId());
    Validate.notNull(dbRoleMenuMapping, "更新的角色菜单关联信息不存在");
    dbRoleMenuMapping.setRoleId(roleMenuMapping.getRoleId());
    dbRoleMenuMapping.setMenuId(roleMenuMapping.getMenuId());
    baseMapper.updateById(dbRoleMenuMapping);
    return dbRoleMenuMapping;
  }

  @Override
  @Transactional
  public void bindRoleMenus(String roleId, List<MenuEntity> menus) {
    Validate.notEmpty(menus, "绑定的菜单按钮不能为空");
    this.bindValidation(roleId, menus);
    baseMapper.deleteByRoleId(roleId);
    List<ButtonEntity> buttons = Lists.newArrayList();
    for (MenuEntity menu : menus) {
      RoleMenuMappingEntity mapping = new RoleMenuMappingEntity();
      mapping.setRoleId(roleId);
      mapping.setMenuId(menu.getId());
      baseMapper.insert(mapping);
      if(!CollectionUtils.isEmpty(menu.getButtons())) {
        buttons.addAll(menu.getButtons());
      }
    }
    roleButtonMappingService.rebindRoleButtons(roleId, buttons);
  }

  @Override
  @Transactional
  public void rebindRoleMenus(String roleId, List<MenuEntity> menus) {
    baseMapper.deleteByRoleId(roleId);
    if(CollectionUtils.isEmpty(menus)) {
      roleButtonMappingService.rebindRoleButtons(roleId, Lists.newArrayList());
      return;
    }
    this.bindRoleMenus(roleId, menus);
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    Validate.notBlank(id, "主键ID不能为空");
    baseMapper.deleteById(id);
  }

  @Override
  public RoleMenuMappingEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  /**
   * 绑定前数据校验
   * @param roleId
   * @param menus
   */
  private void bindValidation(String roleId, List<MenuEntity> menus) {
    Validate.notBlank(roleId, "角色ID不能为空");
    Validate.notEmpty(menus, "绑定的菜单按钮不能为空");
    RoleEntity role = roleService.findById(roleId);
    Validate.notNull(role, "角色不存在:%s", roleId);
    if(!CollectionUtils.isEmpty(menus)) {
      for (MenuEntity menu : menus) {
        Validate.notBlank(menu.getId(), "菜单ID不能为空");
        MenuEntity dbMenu = menuService.findById(menu.getId());
        Validate.notNull(dbMenu, "菜单不存在:%s", menu.getId());
      }
    }
  }

}
