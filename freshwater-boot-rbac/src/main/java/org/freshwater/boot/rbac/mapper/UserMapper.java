package org.freshwater.boot.rbac.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.rbac.dto.UserQueryDTO;
import org.freshwater.boot.rbac.entity.UserEntity;

import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 16:22
 */
public interface UserMapper extends BaseMapper<UserEntity> {

  /**
   * 根据Id统计用户
   * @param id
   * @return
   */
  long selectCountById(@Param("id") String id);

  /**
   * 根据登录账号统计
   * @param userAccount
   * @return
   */
  long selectCountByUserAccount(@Param("userAccount") String userAccount);

  /**
   * 根据登录账号统计 排除ID
   * select count(*) from user where userAccount = #{userAccount} and id != #{id}
   * @param userAccount
   * @param id
   * @return
   */
  long selectByUserAccountAndIdNot(@Param("userAccount") String userAccount, @Param("id") String id);

  /**
   * 根据用户账号查询
   * @param userAccount
   * @return
   */
  UserEntity selectByUserAccount(@Param("userAccount") String userAccount);

  /**
   * 多条件查询
   * @param query
   * @return
   */
  List<UserEntity> selectByConditions(@Param("query") UserQueryDTO query);

}
