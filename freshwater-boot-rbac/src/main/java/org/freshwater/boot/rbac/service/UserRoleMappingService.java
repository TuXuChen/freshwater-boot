package org.freshwater.boot.rbac.service;


import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.entity.UserRoleMappingEntity;

import java.util.List;

/**
 * <p>
 * 用户角色关联信息 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:46
 */
public interface UserRoleMappingService {

  /**
   * 创建用户角色关联信息
   * @param userRoleMapping
   * @return
   */
  UserRoleMappingEntity create(UserRoleMappingEntity userRoleMapping);

  /**
   * 更新用户角色关联信息
   * @param userRoleMapping
   * @return
   */
  UserRoleMappingEntity update(UserRoleMappingEntity userRoleMapping);

  /**
   * 绑定用户和角色
   * @param userId
   * @param roles
   */
  void rebindUserRoles(String userId, List<RoleEntity> roles);

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 根据用户Id删除用户角色关联信息
   * @param userId
   */
  void deleteByUserId(String userId);

  /**
   * 根据ID查询用户角色关联信息
   * @param id
   * @return
  */
  UserRoleMappingEntity findById(String id);

}
