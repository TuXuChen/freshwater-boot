package org.freshwater.boot.rbac.service;

import org.freshwater.boot.rbac.entity.ButtonEntity;
import org.freshwater.boot.rbac.entity.RoleButtonMappingEntity;

import java.util.List;

/**
 * <p>
 * 角色按钮关联信息 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
public interface RoleButtonMappingService {

  /**
   * 创建角色按钮关联信息
   * @param roleButtonMapping
   * @return
   */
  RoleButtonMappingEntity create(RoleButtonMappingEntity roleButtonMapping);

  /**
   * 更新角色按钮关联信息
   * @param roleButtonMapping
   * @return
   */
  RoleButtonMappingEntity update(RoleButtonMappingEntity roleButtonMapping);

  /**
   * 绑定一个角色的按钮
   * @param roleId
   * @param buttons
   */
  void rebindRoleButtons(String roleId, List<ButtonEntity> buttons);

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 根据ID查询角色按钮关联信息
   * @param id
   * @return
  */
  RoleButtonMappingEntity findById(String id);
}
