package org.freshwater.boot.rbac.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.rbac.entity.MenuEntity;
import org.freshwater.boot.rbac.service.MenuService;
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
 * 菜单 前端控制器
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22
 */
@Api(value = "菜单管理", tags = {"菜单管理"})
@RestController
@RequestMapping("/v1/menus")
public class MenuController {

  @Autowired
  private MenuService menuService;

  /**
   * 创建菜单
   * @param menu
   * @return
   */
  @PostMapping("")
  @ApiOperation("创建菜单")
  public ResponseModel create(@RequestBody MenuEntity menu) {
    MenuEntity savedMenu = menuService.create(menu);
    return ResponseModelUtils.success(savedMenu);
  }

 /**
  * 更新菜单
  * @param menu
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新菜单")
  public ResponseModel update(@RequestBody MenuEntity menu) {
    MenuEntity savedMenu = menuService.update(menu);
    return ResponseModelUtils.success(savedMenu);
  }

  /**
   * 根据主键ID删除数据
   * @param id
   * @return
   */
  @DeleteMapping("{id}")
  @ApiOperation("根据主键ID删除数据")
  public ResponseModel deleteById(@PathVariable String id) {
    menuService.deleteById(id);
    return ResponseModelUtils.success();
  }

  /**
   * 根据ID查询菜单基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询菜单基本信息")
  public ResponseModel findById(@PathVariable String id) {
    MenuEntity menu = menuService.findById(id);
    return ResponseModelUtils.success(menu);
  }

  /**
   * 根据类型和状态查询菜单树
   *
   * @param type
   * @param state 可以不传
   * @return
   */
  @GetMapping("findTreeByTypeAndState")
  @ApiOperation("根据类型和状态查询菜单树")
  public ResponseModel findTreeByTypeAndState(@RequestParam @ApiParam("类型:0:后台菜单,1:APP菜单") Integer type,
                                              @RequestParam(required = false) @ApiParam("状态,0:禁用,1:启用 可不传") Integer state) {
    List<MenuEntity> menus = menuService.findTreeByTypeAndState(type, state);
    return ResponseModelUtils.success(menus);
  }

  /**
   * 根据角色ID和类型查询关联的菜单
   *
   * @return
   */
  @GetMapping("findByRoleIdAndType")
  @ApiOperation("根据角色ID和类型查询关联的菜单")
  public ResponseModel findByRoleIdAndType(@RequestParam @ApiParam("角色ID") String roleId,
                                           @RequestParam(required = false) @ApiParam("类型:0:后台菜单,1:APP菜单,可不传") Integer type) {
    List<MenuEntity> menus = menuService.findByRoleIdAndType(roleId, type);
    return ResponseModelUtils.success(menus);
  }

  /**
   * 根据角色ID和类型查询可选择的菜单树
   * @param roleId
   * @param type
   * @return
   */
  @GetMapping("findCheckedTreeByRoleIdAndType")
  @ApiOperation("根据角色ID和类型查询可选择的菜单树")
  public ResponseModel findCheckedTreeByRoleIdAndType(@RequestParam @ApiParam("角色ID") String roleId,
                                                      @RequestParam(required = false) @ApiParam("类型:0:后台菜单,1:APP菜单,可不传") Integer type) {
    List<MenuEntity> menus = menuService.findCheckedTreeByRoleIdAndType(roleId, type);
    return ResponseModelUtils.success(menus);
  }

  /**
   * 根据角色ID和类型查询关联的菜单和按钮
   *
   * @return
   */
  @GetMapping("findDetailsByRoleIdAndType")
  @ApiOperation("根据角色ID和类型查询关联的菜单和按钮")
  public ResponseModel findDetailsByRoleIdAndType(@RequestParam @ApiParam("角色ID") String roleId,
                                                  @RequestParam(required = false) @ApiParam("类型:0:后台菜单,1:APP菜单,可不传") Integer type) {
    List<MenuEntity> menus = menuService.findDetailsByRoleIdAndType(roleId, type);
    return ResponseModelUtils.success(menus);
  }

  /**
   * 根据角色ID和类型查询关联的菜单和按钮的树结构
   *
   * @return
   */
  @GetMapping("findTreeByRoleIdAndType")
  @ApiOperation("根据角色ID和类型查询关联的菜单和按钮的树结构")
  public ResponseModel findTreeByRoleIdAndType(@RequestParam @ApiParam("角色ID") String roleId,
                                               @RequestParam(required = false) @ApiParam("类型:0:后台菜单,1:APP菜单,可不传") Integer type) {
    List<MenuEntity> menus = menuService.findTreeByRoleIdAndType(roleId, type);
    return ResponseModelUtils.success(menus);
  }

  /**
   * 根据用户ID和类型查询用户可见的菜单树
   * @param userId
   * @param type
   * @return
   */
  @GetMapping("findTreeByUserIdAndType")
  @ApiOperation("根据用户ID和类型查询用户可见的菜单树")
  public ResponseModel findTreeByUserIdAndType(@RequestParam @ApiParam("用户ID") String userId,
                                               @RequestParam(required = false) @ApiParam("类型:0:后台菜单,1:APP菜单,可不传") Integer type) {
    List<MenuEntity> menus = menuService.findTreeByUserIdAndType(userId, type);
    return ResponseModelUtils.success(menus);
  }

}
