package org.freshwater.boot.rbac.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.rbac.dto.UserQueryDTO;
import org.freshwater.boot.rbac.dto.UserRegisterDTO;
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22
 */
@Api(value = "用户管理", tags = {"用户管理"})
@RestController
@RequestMapping("/v1/users")
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * 创建用户
   * @param user
   * @return
   */
  @PostMapping("")
  @ApiOperation("创建用户")
  public ResponseModel create(@RequestBody UserEntity user) {
    UserEntity savedUser = userService.create(user);
    return ResponseModelUtils.success(savedUser);
  }

  /**
   * 注册用户
   * @param registerUser
   * @return
   */
  @ApiOperation("注册用户")
  @PostMapping("register")
  public ResponseModel register(@RequestBody UserRegisterDTO registerUser) {
    UserEntity user = userService.register(registerUser);
    return ResponseModelUtils.success(user);
  }

 /**
  * 更新用户
  * @param user
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新用户")
  public ResponseModel update(@RequestBody UserEntity user) {
    UserEntity savedUser = userService.update(user);
    return ResponseModelUtils.success(savedUser);
  }

  /**
   * 根据主键ID删除数据
   * @param id
   * @return
   */
  @DeleteMapping("{id}")
  @ApiOperation("根据主键ID删除数据")
  public ResponseModel deleteById(@PathVariable String id) {
    userService.deleteById(id);
    return ResponseModelUtils.success();
  }

  /**
   * 根据ID查询用户基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询用户基本信息")
  public ResponseModel findById(@PathVariable String id) {
    UserEntity user = userService.findById(id);
    return ResponseModelUtils.success(user);
  }

  /**
   * 多条件分页查询
   *
   * @param query
   * @param pageable
   * @return
   */
  @GetMapping("findByConditions")
  @ApiOperation(value = "多条件分页查询", notes = "分页条件为page和size,page从1开始,size默认50")
  public ResponseModel findByConditions(UserQueryDTO query, @PageableDefault(page = 1, size = 50) Pageable pageable) {
    PageInfo page = userService.findByConditions(query, pageable);
    return ResponseModelUtils.success(page);
  }

}
