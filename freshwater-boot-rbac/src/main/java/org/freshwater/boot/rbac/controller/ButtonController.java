package org.freshwater.boot.rbac.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.rbac.entity.ButtonEntity;
import org.freshwater.boot.rbac.service.ButtonService;
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
 * 按钮 前端控制器
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22
 */
@Api(value = "按钮管理", tags = {"按钮管理"})
@RestController
@RequestMapping("/v1/buttons")
public class ButtonController {

  @Autowired
  private ButtonService buttonService;

  /**
   * 创建按钮
   * @param button
   * @return
   */
  @PostMapping("")
  @ApiOperation("创建按钮")
  public ResponseModel create(@RequestBody ButtonEntity button) {
    ButtonEntity savedButton = buttonService.create(button);
    return ResponseModelUtils.success(savedButton);
  }

 /**
  * 更新按钮
  * @param button
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新按钮")
  public ResponseModel update(@RequestBody ButtonEntity button) {
    ButtonEntity savedButton = buttonService.update(button);
    return ResponseModelUtils.success(savedButton);
  }

  /**
   * 根据主键ID删除数据
   * @param id
   * @return
   */
  @DeleteMapping("{id}")
  @ApiOperation("根据主键ID删除数据")
  public ResponseModel deleteById(@PathVariable String id) {
    buttonService.deleteById(id);
    return ResponseModelUtils.success();
  }

  /**
   * 根据ID查询按钮基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询按钮基本信息")
  public ResponseModel findById(@PathVariable String id) {
    ButtonEntity button = buttonService.findById(id);
    return ResponseModelUtils.success(button);
  }

  /**
   * 根据菜单ID和状态查询
   * @param menuId
   * @param state
   * @return
   */
  @GetMapping("findByMenuIdAndState")
  @ApiOperation(value = "根据菜单ID和状态查询", notes = "状态可不传")
  public ResponseModel findByMenuIdAndState(@RequestParam @ApiParam("菜单ID") String menuId,
                                            @RequestParam(required = false) @ApiParam("状态,0:禁用,1:启用, 可不传") Integer state) {
    List<ButtonEntity> buttons = buttonService.findByMenuIdAndState(menuId, state);
    return ResponseModelUtils.success(buttons);
  }

  /**
   * 根据用户ID和类型查询用户可用的按钮
   * @param userId
   * @return
   */
  @GetMapping("findByUserIdAndType")
  @ApiOperation("根据用户ID和类型查询用户可用的按钮")
  public ResponseModel findByUserIdAndType(@RequestParam @ApiParam("用户ID") String userId,
                                           @RequestParam @ApiParam("用户ID")  Integer type) {
    List<ButtonEntity> buttons = buttonService.findByUserIdAndType(userId, type);
    return ResponseModelUtils.success(buttons);
  }

}
