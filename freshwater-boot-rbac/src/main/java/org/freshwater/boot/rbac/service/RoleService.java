package org.freshwater.boot.rbac.service;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Pageable;
import org.freshwater.boot.rbac.dto.RoleQueryDTO;
import org.freshwater.boot.rbac.entity.RoleEntity;

import java.util.List;


/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
public interface RoleService {

  /**
   * 创建角色
   * @param role
   * @return
   */
  RoleEntity create(RoleEntity role);

  /**
   * 更新角色
   * @param role
   * @return
   */
  RoleEntity update(RoleEntity role);

  /**
   * 根据ID启用
   * @param id
   */
  void enableById(String id);

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 根据ID查询角色
   * @param id
   * @return
  */
  RoleEntity findById(String id);

  /**
   * 根据用户ID和状态查询角色
   * @param id
   * @param state
   * @return
   */
  List<RoleEntity> findByUserIdAndState(String id, Integer state);

  /**
   * 根据用户账号和状态查询角色
   * @param username
   * @param state 可不传
   * @return
   */
  List<RoleEntity> findByUserNameAndState(String username, Integer state);

  /**
   * 根据状态和用户id查询用户关联的角色
   * @param state 可不传
   * @param userId
   * @return
   */
  List<RoleEntity> findByStateAndUserId(Integer state, String userId);

  /**
   * 多条件分页查询
   * @param query
   * @param pageable
   * @return
   */
  PageInfo<RoleEntity> findByConditions(RoleQueryDTO query, Pageable pageable);

}
