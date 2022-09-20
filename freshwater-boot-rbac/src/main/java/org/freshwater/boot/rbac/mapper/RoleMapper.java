package org.freshwater.boot.rbac.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.rbac.dto.RoleQueryDTO;
import org.freshwater.boot.rbac.entity.RoleEntity;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
public interface RoleMapper extends BaseMapper<RoleEntity> {

  /**
   * 根据名称统计
   * @param name
   * @return
   */
  long selectCountByName(@Param("name") String name);

  /**
   * 根据名称统计,排除ID
   * @param name
   * @param id
   * @return
   */
  long selectCountByNameAndIdNot(@Param("name") String name, @Param("id") String id);

  /**
   * 根据用户id和状态查询角色
   * @param userId
   * @param state
   * @return
   */
  List<RoleEntity> selectByUserIdAndState(@Param("userId") String userId, @Param("state") Integer state);

  /**
   * 根据用户账号和状态查询角色
   * @param username
   * @param state 可不传
   * @return
   */
  List<RoleEntity> selectByUserNameAndState(@Param("username") String username, @Param("state") Integer state);

  /**
   * 根据状态和用户id查询用户关联的角色
   * @param state
   * @param userId
   * @return
   */
  List<RoleEntity> selectByStateAndUserId(@Param("state") Integer state, @Param("userId") String userId);

  /**
   * 多条件查询
   * @param query
   * @return
   */
  List<RoleEntity> selectByConditions(@Param("query") RoleQueryDTO query);
}
