package org.freshwater.boot.rbac.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.entity.UserRoleMappingEntity;
import org.freshwater.boot.rbac.mapper.UserRoleMappingMapper;
import org.freshwater.boot.rbac.service.RoleService;
import org.freshwater.boot.rbac.service.UserRoleMappingService;

import java.util.List;

/**
 * <p>
 * 用户角色关联信息 服务实现类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:46
 */
@Service
public class UserRoleMappingServiceImpl extends ServiceImpl<UserRoleMappingMapper, UserRoleMappingEntity> implements UserRoleMappingService {

  @Autowired
  private RoleService roleService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public UserRoleMappingEntity create(UserRoleMappingEntity userRoleMapping) {
    ValidationUtils.validate(userRoleMapping, CreateValidate.class);
    baseMapper.insert(userRoleMapping);
    return userRoleMapping;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public UserRoleMappingEntity update(UserRoleMappingEntity userRoleMapping) {
    ValidationUtils.validate(userRoleMapping, UpdateValidate.class);
    UserRoleMappingEntity dbUserRoleMapping = baseMapper.selectById(userRoleMapping.getId());
    Validate.notNull(dbUserRoleMapping, "更新的用户角色关联信息不存在");
    dbUserRoleMapping.setUserId(userRoleMapping.getUserId());
    dbUserRoleMapping.setRoleId(userRoleMapping.getRoleId());
    baseMapper.updateById(dbUserRoleMapping);
    return dbUserRoleMapping;
  }

  @Override
  public void rebindUserRoles(String userId, List<RoleEntity> roles) {
    this.rebindValidation(userId, roles);
    baseMapper.deleteByUserId(userId);
    for (RoleEntity role : roles) {
      UserRoleMappingEntity mapping = new UserRoleMappingEntity();
      mapping.setUserId(userId);
      mapping.setRoleId(role.getId());
      baseMapper.insert(mapping);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteById(String id) {
    Validate.notBlank(id, "主键ID不能为空");
    baseMapper.deleteById(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteByUserId(String userId) {
    Validate.notBlank(userId, "用户ID不能为空");
    long countUserId = baseMapper.selectCountByUserId(userId);
    Validate.isTrue(countUserId != 0, "用户角色关联信息不存在");
    baseMapper.deleteByUserId(userId);
  }

  @Override
  public UserRoleMappingEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  /**
   * 绑定前数据校验
   * @param userId
   * @param roles
   */
  private void rebindValidation(String userId, List<RoleEntity> roles) {
    Validate.notBlank(userId, "用户ID不能为空!");
    Validate.notEmpty(roles, "角色不能为空!");
    Validate.isTrue(roles.size() == 1, "用户只能有一个角色!");
    for (RoleEntity role : roles) {
      Validate.notBlank(role.getId(), "角色ID不能为空!");
      RoleEntity dbRole = roleService.findById(role.getId());
      Validate.notNull(dbRole, "角色不存在:%s", role.getId());
    }
  }

}
