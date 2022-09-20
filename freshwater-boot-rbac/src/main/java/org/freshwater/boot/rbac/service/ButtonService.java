package org.freshwater.boot.rbac.service;


import org.freshwater.boot.rbac.entity.ButtonEntity;
import org.freshwater.boot.rbac.entity.MenuEntity;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 按钮 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:52
 */
public interface ButtonService {

  /**
   * 创建按钮
   * @param button
   * @return
   */
  ButtonEntity create(ButtonEntity button);

  /**
   * 保存菜单的按钮
   * @param menu
   * @param buttons
   */
  void save(MenuEntity menu, List<ButtonEntity> buttons);

  /**
   * 更新按钮
   * @param button
   * @return
   */
  ButtonEntity update(ButtonEntity button);

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 清除缓存
   */
  void deleteUrlCache();

  /**
   * 根据菜单ID禁用
   * @param menuId
   */
  void disableByMenuId(String menuId);

  /**
   * 根据ID查询按钮
   * @param id
   * @return
  */
  ButtonEntity findById(String id);

  /**
   * 根据菜单ID和状态查询
   * @param menuId
   * @param state
   * @return
   */
  List<ButtonEntity> findByMenuIdAndState(String menuId, Integer state);

  /**
   * 根据类型和状态查询
   * @param type
   * @param state
   * @return
   */
  List<ButtonEntity> findByTypeAndState(Integer type, Integer state);

  /**
   * 根据用户id和按钮类型查询
   * @param userId
   * @param type
   * @return
   */
  List<ButtonEntity> findByUserIdAndType(String userId, Integer type);

  /**
   * 根据角色ID和类型查询
   * @param roleId
   * @param type 类型:0:后台按钮,1:APP按钮  可不传
   * @return
   */
  List<ButtonEntity> findByRoleIdAndType(String roleId, Integer type);

  /**
   * 根据用户账户和类型查询用户可用的按钮urls
   * @param userId
   * @param type
   * @return
   */
  Set<String> findUrlsByUserIdAndType(String userId, Integer type);

  /**
   * 根据用户账户和类型查询用户可用的按钮urls
   * <p>
   *   增加了缓存,先从缓存中读取,如果缓存里面没有再从数据库读取
   * </p>
   * @param account
   * @param type
   * @return
   */
  Set<String> findUrlsByUserAccountAndTypeCache(String account, Integer type);

  /**
   * 根据菜单ID和角色id和类型查询有选中状态的按钮
   * @param menuId
   * @param roleId
   * @param type
   * @return
   */
  List<ButtonEntity> findCheckedByMenuIdAndRoleIdAndType(String menuId, String roleId, Integer type);

}
