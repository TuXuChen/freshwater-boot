package org.freshwater.boot.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.rbac.entity.ButtonEntity;

import java.util.List;

/**
 * <p>
 * 按钮 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:52
 */
public interface ButtonMapper extends BaseMapper<ButtonEntity> {

  /**
   * 根据菜单ID更新状态
   *
   * @param menuId
   * @param state
   * @return
   */
  int updateStateByMenuId(@Param("menuId") String menuId, @Param("state") Integer state);

  /**
   * 根据编码 类型统计
   *
   * @param code
   * @param type
   * @return
   */
  long selectCountByCodeAndType(@Param("code") String code, @Param("type") Integer type);

  /**
   * 根据编码 类型统计, 排除ID
   *
   * @param code
   * @param type
   * @param id
   * @return
   */
  long selectCountByCodeAndTypeAndIdNot(@Param("code") String code, @Param("type") Integer type, @Param("id") String id);

  /**
   * 根据编码和类型查询
   *
   * @param code
   * @param type
   * @return
   */
  ButtonEntity selectByCodeAndType(@Param("code") String code, @Param("type") Integer type);

  /**
   * 根据菜单ID和状态查询
   *
   * @param menuId
   * @param state  可不传
   * @return
   */
  List<ButtonEntity> selectByMenuIdAndState(@Param("menuId") String menuId, @Param("state") Integer state);

  /**
   * 根据类型 状态查询按钮
   *
   * @param type  可不传
   * @param state 可不传
   * @return
   */
  List<ButtonEntity> selectByTypeAndState(@Param("type") Integer type, @Param("state") Integer state);

  /**
   * 根据角色ID集合和类型查询
   *
   * @param roleIds
   * @param type
   * @param state
   * @return
   */
  List<ButtonEntity> selectByRoleIdsAndTypeAndState(@Param("roleIds") List<String> roleIds, @Param("type") Integer type, @Param("state") Integer state);

  /**
   * 根据菜单ID、角色ID、类型（可不传）查询
   *
   * @param menuId
   * @param roleId
   * @param type
   * @return
   */
  List<ButtonEntity> selectByMenuIdAndRoleIdAndType(@Param("menuId") String menuId, @Param("roleId") String roleId, @Param("type") Integer type);

  /**
   * 根据角色ID和类型查询
   *
   * @param roleId
   * @param type   可不传
   * @return
   */
  List<ButtonEntity> selectByRoleIdAndType(@Param("roleId") String roleId, @Param("type") Integer type);
}
