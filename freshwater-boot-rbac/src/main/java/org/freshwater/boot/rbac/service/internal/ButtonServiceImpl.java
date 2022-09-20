package org.freshwater.boot.rbac.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.mapper.ButtonMapper;
import org.freshwater.boot.rbac.service.ButtonService;
import org.freshwater.boot.rbac.service.RoleService;
import org.freshwater.boot.rbac.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.freshwater.boot.rbac.constants.Constants.ROLE_ADMIN_CODE;
import static org.freshwater.boot.rbac.constants.RedisKeys.USE_URLS;
import static org.freshwater.boot.rbac.constants.RedisKeys.USE_URLS_PREFIX;


/**
 * <p>
 * 按钮 服务实现类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:52
 */
@Service
public class ButtonServiceImpl extends ServiceImpl<ButtonMapper, ButtonEntity> implements ButtonService {

  @Autowired
  private RbacProperties rbacProperties;
  @Autowired
  private RoleService roleService;
  @Autowired
  private UserService userService;
  @Autowired
  private RedissonClient redissonClient;
  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  @Transactional
  public ButtonEntity create(ButtonEntity button) {
    ValidationUtils.validate(button, CreateValidate.class);
    long count = baseMapper.selectCountByCodeAndType(button.getCode(), button.getType());
    if(count > 0) {
      ButtonEntity dbButton = baseMapper.selectByCodeAndType(button.getCode(), button.getType());
      dbButton.setMenuId(button.getMenuId());
      return this.update(dbButton, button);
    }
    button.setCreateTime(LocalDateTime.now());
    button.setModifyTime(LocalDateTime.now());
    baseMapper.insert(button);
    return button;
  }

  @Override
  public void save(MenuEntity menu, List<ButtonEntity> buttons) {
    Validate.notNull(menu, "菜单不能为空");
    buttons = ObjectUtils.defaultIfNull(buttons, Lists.newArrayList());
    Map<String, ButtonEntity> buttonMap = buttons.stream().collect(Collectors.toMap(ButtonEntity::getCode, v -> v, (v1, v2) -> v1));
    List<ButtonEntity> dbButtons = baseMapper.selectByMenuIdAndState(menu.getId(), null);
    Set<ButtonEntity> creates = Sets.newHashSet();
    Set<ButtonEntity> updates = Sets.newHashSet();
    Set<ButtonEntity> deletes = Sets.newHashSet();
    WdCollectionUtils.collectionDiscrepancy(buttons, dbButtons, ButtonEntity::getCode, deletes, updates, creates);
    for (ButtonEntity button : deletes) {
      button.setState(NormalStatusEnum.DISABLE.getStatus());
      button.setModifyTime(LocalDateTime.now());
      baseMapper.updateById(button);
    }
    for (ButtonEntity dbButton : updates) {
      ButtonEntity button = buttonMap.get(dbButton.getCode());
      dbButton.setState(button.getState());
      dbButton.setName(button.getName());
      dbButton.setModifyTime(LocalDateTime.now());
      dbButton.setUrls(button.getUrls());
      baseMapper.updateById(dbButton);
    }
    for (ButtonEntity button : creates) {
      button.setMenuId(menu.getId());
      button.setType(menu.getType());
      button.setId(null);
      this.create(button);
    }
  }

  @Override
  @Transactional
  public ButtonEntity update(ButtonEntity button) {
    ValidationUtils.validate(button, UpdateValidate.class);
    long count = baseMapper.selectCountByCodeAndTypeAndIdNot(button.getCode(), button.getType(), button.getId());
    Validate.isTrue(count == 0, "按钮编码重复");
    ButtonEntity dbButton = baseMapper.selectById(button.getId());
    Validate.notNull(dbButton, "更新的按钮不存在");
    dbButton.setState(button.getState());
    dbButton.setType(button.getType());
    dbButton.setCode(button.getCode());
    dbButton.setName(button.getName());
    dbButton.setMenuId(button.getMenuId());
    dbButton.setUrls(button.getUrls());
    dbButton.setCreateTime(button.getCreateTime());
    dbButton.setModifyTime(button.getModifyTime());
    baseMapper.updateById(dbButton);
    return dbButton;
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    Validate.notBlank(id, "主键ID不能为空");
    baseMapper.deleteById(id);
  }

  @Override
  public void deleteUrlCache() {
    String pattern = StringUtils.join(USE_URLS_PREFIX, "*", ":*");
    Set<String> keys = redisTemplate.keys(pattern);
    this.deleteUrlRedisKeys(keys);
  }

  @Override
  public void disableByMenuId(String menuId) {
    Validate.notBlank(menuId, "菜单ID不能为空");
    baseMapper.updateStateByMenuId(menuId, NormalStatusEnum.DISABLE.getStatus());
  }

  @Override
  public ButtonEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  @Override
  public List<ButtonEntity> findByMenuIdAndState(String menuId, Integer state) {
    if(StringUtils.isBlank(menuId)) {
      return Lists.newArrayList();
    }
    return baseMapper.selectByMenuIdAndState(menuId, state);
  }

  @Override
  public List<ButtonEntity> findByTypeAndState(Integer type, Integer state) {
    return baseMapper.selectByTypeAndState(type, state);
  }

  @Override
  public List<ButtonEntity> findByUserIdAndType(String userId, Integer type) {
    if (StringUtils.isBlank(userId) || Objects.isNull(type)) {
      return Lists.newArrayList();
    }
    List<RoleEntity> roles = roleService.findByUserIdAndState(userId, NormalStatusEnum.ENABLE.getStatus());
    if(CollectionUtils.isEmpty(roles)) {
      return Lists.newArrayList();
    }
    for (RoleEntity role : roles) {
      for (String roleCode : rbacProperties.getIgnoreMethodCheckRoles()) {
        if(role.getCode().equalsIgnoreCase(roleCode)) {
          return this.findByTypeAndState(type, NormalStatusEnum.ENABLE.getStatus());
        }
      }
    }
    List<String> roleIds = roles.stream().map(RoleEntity::getId).collect(Collectors.toList());
    return baseMapper.selectByRoleIdsAndTypeAndState(roleIds, type, NormalStatusEnum.ENABLE.getStatus());
  }

  @Override
  public List<ButtonEntity> findByRoleIdAndType(String roleId, Integer type) {
    if(StringUtils.isBlank(roleId)) {
      return Lists.newArrayList();
    }
    return baseMapper.selectByRoleIdAndType(roleId, type);
  }

  @Override
  public Set<String> findUrlsByUserIdAndType(String userId, Integer type) {
    if (StringUtils.isBlank(userId) || Objects.isNull(type)) {
      return Sets.newHashSet();
    }
    List<ButtonEntity> buttons = this.findByUserIdAndType(userId, type);
    if(CollectionUtils.isEmpty(buttons)) {
      return Sets.newHashSet();
    }
    Set<String> urlSet = Sets.newHashSet();
    for (ButtonEntity button : buttons) {
      String urls = button.getUrls();
      if(StringUtils.isNotBlank(urls)) {
        String[] split = urls.split(",");
        urlSet.addAll(Sets.newHashSet(split));
      }
    }
    return urlSet;
  }

  @Override
  public Set<String> findUrlsByUserAccountAndTypeCache(String account, Integer type) {
    if (StringUtils.isBlank(account) || Objects.isNull(type)) {
      return Sets.newHashSet();
    }
    String key = String.format(USE_URLS, account, type);
    RSetCache<String> urls = redissonClient.getSetCache(key);
    Set<String> urlSet;
    if (!urls.isExists()) {
      UserEntity user = userService.findByUserAccount(account);
      if (Objects.isNull(user)) {
        return Sets.newHashSet();
      }
      urlSet = this.findUrlsByUserIdAndType(user.getId(), type);
      if (urlSet != null) {
        urls.addAll(urlSet);
      }
    }
    return urls;
  }

  @Override
  public List<ButtonEntity> findCheckedByMenuIdAndRoleIdAndType(String menuId, String roleId, Integer type) {
    if(StringUtils.isAnyBlank(menuId, roleId)) {
      return Lists.newArrayList();
    }
    List<ButtonEntity> buttons = this.findByMenuIdAndState(menuId, NormalStatusEnum.ENABLE.getStatus());
    if(CollectionUtils.isEmpty(buttons)) {
      return buttons;
    }
    RoleEntity role = roleService.findById(roleId);
    if (null == role) {
      return Lists.newArrayList();
    }
    List<ButtonEntity> roleButtons = baseMapper.selectByMenuIdAndRoleIdAndType(menuId, roleId, type);
    Set<String> roleButtonCodeSet = roleButtons.stream().map(ButtonEntity::getCode).collect(Collectors.toSet());
    for (ButtonEntity button : buttons) {
      if (role.getCode().equalsIgnoreCase(ROLE_ADMIN_CODE)) {
        button.setChecked(true);
      } else {
        button.setChecked(roleButtonCodeSet.contains(button.getCode()));
      }
    }
    return buttons;
  }

  /**
   * 更新按钮
   * @param dbButton
   * @param button
   * @return
   */
  private ButtonEntity update(ButtonEntity dbButton, ButtonEntity button) {
    dbButton.setState(button.getState());
    dbButton.setCode(button.getCode());
    dbButton.setName(button.getName());
    dbButton.setUrls(button.getUrls());
    dbButton.setModifyTime(LocalDateTime.now());
    baseMapper.updateById(dbButton);
    return dbButton;
  }

  /**
   * 删除redis中url的key
   * @param keys
   */
  private void deleteUrlRedisKeys(Set<String> keys) {
    if(CollectionUtils.isEmpty(keys)) {
      return;
    }
    for (String key : keys) {
      RSetCache<Object> cache = redissonClient.getSetCache(key);
      cache.deleteAsync();
    }
  }

}
