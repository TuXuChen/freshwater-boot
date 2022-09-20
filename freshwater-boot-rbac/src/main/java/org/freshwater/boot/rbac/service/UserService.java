package org.freshwater.boot.rbac.service;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Pageable;
import org.freshwater.boot.rbac.dto.UserQueryDTO;
import org.freshwater.boot.rbac.dto.UserRegisterDTO;
import org.freshwater.boot.rbac.entity.UserEntity;


/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 16:22
 */
public interface UserService {

  /**
   * 创建用户
   * @param user
   * @return
   */
  UserEntity create(UserEntity user);

  /**
   * 注册用户
   * @param registerUser
   * @return
   */
  UserEntity register(UserRegisterDTO registerUser);

  /**
   * 更新用户
   * @param user
   * @return
   */
  UserEntity update(UserEntity user);

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 根据ID查询用户
   * @param id
   * @return
  */
  UserEntity findById(String id);

  /**
   * 根据用户账号查询
   * @param username
   * @return
   */
  UserEntity findByUserAccount(String username);

  /**
   * 多条件分页查询
   * @param query
   * @param pageable
   * @return
   */
  PageInfo findByConditions(UserQueryDTO query, Pageable pageable);

}
