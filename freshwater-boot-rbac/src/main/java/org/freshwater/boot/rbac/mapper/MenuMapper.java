package org.freshwater.boot.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.rbac.entity.MenuEntity;

import java.util.List;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
public interface MenuMapper extends BaseMapper<MenuEntity> {

  /**
   * 根据类型和编码统计
   * @param type
   * @param code
   * @return
   */
  long selectCountByTypeAndCode(@Param("type") Integer type, @Param("code") String code);

  /**
   * 根据编码统计, 排除ID
   * @param type
   * @param code
   * @param id
   * @return
   */
  long selectCountByTypeAndCodeAndIdNot(@Param("type") Integer type, @Param("code") String code, @Param("id") String id);

  /**
   * 根据类型查询
   * @param type
   * @param state 状态,0:禁用,1:启用 可不传
   * @return
   */
  List<MenuEntity> selectByTypeAndState(@Param("type") Integer type, @Param("state") Integer state);

  /**
   * 根据角色ID查询关联的菜单
   * @param roleId
   * @param type
   * @return
   */
  List<MenuEntity> selectByRoleIdAndType(@Param("roleId") String roleId, @Param("type") Integer type);

  /**
   * 根据id和状态查询
   * @param id
   * @param state
   * @return
   */
  MenuEntity selectByIdAndState(@Param("id") String id, @Param("state") Integer state);

  /**
   * 根据角色集合\类型\状态查询菜单
   * @param roleIds
   * @param type
   * @param state
   * @return
   */
  List<MenuEntity> selectByRoleIdsAndTypeAndState(@Param("roleIds") List<String> roleIds, @Param("type") Integer type, @Param("state") Integer state);
}
