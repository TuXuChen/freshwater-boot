package org.freshwater.boot.rbac.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.rbac.entity.MenuEntity;
import org.freshwater.boot.rbac.entity.RoleMenuMappingEntity;
import org.freshwater.boot.rbac.service.RoleMenuMappingService;
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
 * 角色菜单关联信息 前端控制器
 * </p>
 *
 * @author Paul Chan
 * @since 2021-06-16
 */
@RestController
@Api(tags = "角色菜单关联信息")
@RequestMapping("/v1/roleMenuMappings")
public class RoleMenuMappingController {

  @Autowired
  private RoleMenuMappingService roleMenuMappingService;

  /**
   * 创建角色菜单关联信息
   * @param roleMenuMapping
   * @return
   */
  @PostMapping("")
  @ApiOperation("创建角色菜单关联信息")
  public ResponseModel create(@RequestBody RoleMenuMappingEntity roleMenuMapping) {
    RoleMenuMappingEntity savedRoleMenuMapping = roleMenuMappingService.create(roleMenuMapping);
    return ResponseModelUtils.success(savedRoleMenuMapping);
  }

 /**
  * 更新角色菜单关联信息
  * @param roleMenuMapping
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新角色菜单关联信息")
  public ResponseModel update(@RequestBody RoleMenuMappingEntity roleMenuMapping) {
    RoleMenuMappingEntity savedRoleMenuMapping = roleMenuMappingService.update(roleMenuMapping);
    return ResponseModelUtils.success(savedRoleMenuMapping);
  }

  /**
   * 绑定一个角色的菜单和按钮
   * @param roleId
   * @param menus
   * @return
   */
  @PostMapping("bindRoleMenus")
  @ApiOperation("绑定一个角色的菜单和按钮")
  public ResponseModel bindRoleMenus(@RequestParam @ApiParam("角色ID") String roleId,
                                     @RequestBody List<MenuEntity> menus) {
    roleMenuMappingService.bindRoleMenus(roleId, menus);
    return ResponseModelUtils.success();
  }

  /**
   * 重新绑定一个角色的菜单和按钮
   * @param roleId
   * @param menus
   * @return
   */
  @PostMapping("rebindRoleMenus")
  @ApiOperation("重新绑定一个角色的菜单和按钮")
  public ResponseModel rebindRoleMenus(@RequestParam @ApiParam("角色ID") String roleId,
                                       @RequestBody List<MenuEntity> menus) {
    roleMenuMappingService.rebindRoleMenus(roleId, menus);
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
    roleMenuMappingService.deleteById(id);
    return ResponseModelUtils.success();
  }

  /**
   * 根据ID查询角色菜单关联信息基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询角色菜单关联信息基本信息")
  public ResponseModel findById(@PathVariable String id) {
    RoleMenuMappingEntity roleMenuMapping = roleMenuMappingService.findById(id);
    return ResponseModelUtils.success(roleMenuMapping);
  }

}
