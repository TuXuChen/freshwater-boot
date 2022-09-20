package org.freshwater.boot.rbac.service;


import org.freshwater.boot.rbac.entity.MenuEntity;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
public interface MenuService {

  /**
   * 创建菜单
   * @param menu
   * @return
   */
  MenuEntity create(MenuEntity menu);

  /**
   * 更新菜单
   * @param menu
   * @return
   */
  MenuEntity update(MenuEntity menu);

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 根据ID查询菜单
   * @param id
   * @return
  */
  MenuEntity findById(String id);

  /**
   * 根据类型查询菜单树
   * @param type
   * @param state 状态,0:禁用,1:启用 可不传
   * @return
   */
  List<MenuEntity> findTreeByTypeAndState(Integer type, Integer state);

  /**
   * 根据角色ID查询关联的菜单
   * @param roleId
   * @param type
   * @return
   */
  List<MenuEntity> findByRoleIdAndType(String roleId, Integer type);

  /**
   * 根据角色ID和类型查询可选择的菜单树
   * @param roleId
   * @param type
   * @return
   */
  List<MenuEntity> findCheckedTreeByRoleIdAndType(String roleId, Integer type);

  /**
   * 根据角色ID和类型查询关联的菜单和按钮
   * @param roleId
   * @param type
   * @return
   */
  List<MenuEntity> findDetailsByRoleIdAndType(String roleId, Integer type);

  /**
   * 根据角色ID和类型查询关联的菜单和按钮的树结构
   * @param roleId
   * @param type
   * @return
   */
  List<MenuEntity> findTreeByRoleIdAndType(String roleId, Integer type);

  /**
   * 根据用户ID和类型查询用户可见的菜单树
   * @param userId
   * @param type
   * @return
   */
  List<MenuEntity> findTreeByUserIdAndType(String userId, Integer type);
}
