package org.freshwater.boot.rbac.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.entity.UserRoleMappingEntity;
import org.freshwater.boot.rbac.service.UserRoleMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户角色关联 前端控制器
 * </p>
 *
 * @author Paul Chan
 * @since 2021-12-02
 */
@RestController
@Api(tags = "用户角色关联")
@RequestMapping("/v1/userRoleMappings")
public class UserRoleMappingController {

  @Autowired
  private UserRoleMappingService userRoleMappingService;

  /**
   * 新增用户角色关联
   * @param userRoleMapping
   * @return
   */
  @PostMapping("")
  @ApiOperation("新增用户角色关联")
  public ResponseModel create(@RequestBody UserRoleMappingEntity userRoleMapping) {
    UserRoleMappingEntity savedUserRoleMapping = userRoleMappingService.create(userRoleMapping);
    return ResponseModelUtils.success(savedUserRoleMapping);
  }

 /**
  * 更新用户角色关联
  * @param userRoleMapping
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新用户角色关联")
  public ResponseModel update(@RequestBody UserRoleMappingEntity userRoleMapping) {
    UserRoleMappingEntity savedUserRoleMapping = userRoleMappingService.update(userRoleMapping);
    return ResponseModelUtils.success(savedUserRoleMapping);
  }

  /**
   * 绑定用户和角色
   * @param userId
   * @param roles
   * @return
   */
  @PutMapping("rebindUserRoles")
  @ApiOperation("绑定用户和角色")
  public ResponseModel rebindUserRoles(@RequestParam @ApiParam("用户ID") String userId, @RequestBody List<RoleEntity> roles) {
    userRoleMappingService.rebindUserRoles(userId, roles);
    return ResponseModelUtils.success();
  }

  /**
   * 根据主键ID删除数据
   * @param id
   * @return
   */
  @DeleteMapping("{id}")
  @ApiOperation("根据主键ID删除数据")
  public ResponseModel deleteById(@PathVariable String id) {
    userRoleMappingService.deleteById(id);
    return ResponseModelUtils.success();
  }

  /**
   * 根据ID查询用户角色关联基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询用户角色关联基本信息")
  public ResponseModel findById(@PathVariable String id) {
    UserRoleMappingEntity userRoleMapping = userRoleMappingService.findById(id);
    return ResponseModelUtils.success(userRoleMapping);
  }

}
