package org.freshwater.boot.rbac.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.rbac.entity.UserRoleMappingEntity;

/**
 * <p>
 * 用户角色关联信息 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:46
 */
public interface UserRoleMappingMapper extends BaseMapper<UserRoleMappingEntity> {

  /**
   * 根据用户Id删除
   * @param userId
   */
  void deleteByUserId(@Param("userId") String userId);

  /**
   * 根据用户Id统计关联信息
   * @param userId
   * @return
   */
  long selectCountByUserId(@Param("userId") String userId);

}
