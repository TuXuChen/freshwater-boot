package org.freshwater.boot.rbac.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.rbac.dto.RoleQueryDTO;
import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.service.RoleService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22
 */
@Api(value = "角色管理", tags = {"角色管理"})
@RestController
@RequestMapping("/v1/roles")
public class RoleController {

  @Autowired
  private RoleService roleService;

  /**
   * 创建角色
   * @param role
   * @return
   */
  @PostMapping("")
  @ApiOperation("创建角色")
  public ResponseModel create(@RequestBody RoleEntity role) {
    RoleEntity savedRole = roleService.create(role);
    return ResponseModelUtils.success(savedRole);
  }

 /**
  * 更新角色
  * @param role
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新角色")
  public ResponseModel update(@RequestBody RoleEntity role) {
    RoleEntity savedRole = roleService.update(role);
    return ResponseModelUtils.success(savedRole);
  }

  /**
   * 根据主键ID启用
   * @param id
   * @return
   */
  @PutMapping("enableById")
  @ApiOperation("根据主键ID启用")
  public ResponseModel enableById(@RequestParam @ApiParam String id) {
    roleService.enableById(id);
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
    roleService.deleteById(id);
    return ResponseModelUtils.success();
  }

  /**
   * 根据ID查询角色基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询角色基本信息")
  public ResponseModel findById(@PathVariable String id) {
    RoleEntity role = roleService.findById(id);
    return ResponseModelUtils.success(role);
  }

  /**
   * 根据状态和用户id查询用户关联的角色
   * @param userId
   * @return
   */
  @GetMapping("findByStateAndUserId")
  @ApiOperation("根据状态和用户id查询用户关联的角色")
  public ResponseModel findByStateAndUserId(@RequestParam(required = false) @ApiParam("状态:0禁用,1:启用, 可不传") Integer state,
                                            @RequestParam @ApiParam("用户ID") String userId) {
    List<RoleEntity> roles = roleService.findByStateAndUserId(state, userId);
    return ResponseModelUtils.success(roles);
  }

  /**
   * 多条件分页查询
   * @param query
   * @param pageable
   * @return
   */
  @GetMapping("findByConditions")
  @ApiOperation(value = "多条件分页查询", notes = "分页条件为page和size,page从1开始,size默认50")
  public ResponseModel findByConditions(RoleQueryDTO query, @PageableDefault(page = 1, size = 50) Pageable pageable) {
    PageInfo<RoleEntity> page = roleService.findByConditions(query, pageable);
    return ResponseModelUtils.success(page);
  }

}
