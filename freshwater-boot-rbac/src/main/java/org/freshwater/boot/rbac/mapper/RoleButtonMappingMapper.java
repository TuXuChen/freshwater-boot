package org.freshwater.boot.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.rbac.entity.RoleButtonMappingEntity;


/**
 * <p>
 * 角色按钮关联信息 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
public interface RoleButtonMappingMapper extends BaseMapper<RoleButtonMappingEntity> {

  /**
   * 根据角色ID删除
   * @param roleId
   */
  void deleteByRoleId(@Param("roleId") String roleId);
}
