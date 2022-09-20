package org.freshwater.boot.rbac.service;


import org.freshwater.boot.rbac.entity.MenuEntity;
import org.freshwater.boot.rbac.entity.RoleMenuMappingEntity;

import java.util.List;

/**
 * <p>
 * 角色菜单关联信息 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:50
 */
public interface RoleMenuMappingService {

  /**
   * 创建角色菜单关联信息
   * @param roleMenuMapping
   * @return
   */
  RoleMenuMappingEntity create(RoleMenuMappingEntity roleMenuMapping);

  /**
   * 更新角色菜单关联信息
   * @param roleMenuMapping
   * @return
   */
  RoleMenuMappingEntity update(RoleMenuMappingEntity roleMenuMapping);

  /**
   * 绑定一个角色的菜单和按钮
   * @param roleId
   * @param menus
   */
  void bindRoleMenus(String roleId, List<MenuEntity> menus);

  /**
   * 重新绑定一个角色的菜单和按钮
   * @param roleId
   * @param menus
   */
  void rebindRoleMenus(String roleId, List<MenuEntity> menus);

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 根据ID查询角色菜单关联信息
   * @param id
   * @return
  */
  RoleMenuMappingEntity findById(String id);
}
