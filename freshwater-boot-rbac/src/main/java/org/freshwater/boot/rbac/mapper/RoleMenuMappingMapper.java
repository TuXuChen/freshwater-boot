package org.freshwater.boot.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.rbac.entity.RoleMenuMappingEntity;


/**
 * <p>
 * 角色菜单关联信息 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:50
 */
public interface RoleMenuMappingMapper extends BaseMapper<RoleMenuMappingEntity> {

  /**
   * 根据角色ID删除关联的菜单
   * @param roleId
   */
  void deleteByRoleId(@Param("roleId") String roleId);
}
