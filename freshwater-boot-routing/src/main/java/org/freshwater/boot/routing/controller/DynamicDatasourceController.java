package org.freshwater.boot.routing.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.routing.dto.DynamicDatasourceDTO;
import org.freshwater.boot.routing.entity.DynamicDatasourceEntity;
import org.freshwater.boot.routing.service.DynamicDatasourceService;

/**
 * <p>
 * 动态数据源 前端控制器
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-23
 */
@Api(value = "动态数据源管理", tags = {"动态数据源管理"})
@RestController
@RequestMapping("/v1/dynamicDatasources")
public class DynamicDatasourceController {

  @Autowired
  private DynamicDatasourceService dynamicDatasourceService;

  /**
   * 创建动态数据源
   * @param dynamicDatasource
   * @return
   */
  @PostMapping("")
  @ApiOperation("创建动态数据源")
  public ResponseModel create(@RequestBody DynamicDatasourceDTO dynamicDatasource) {
    DynamicDatasourceEntity savedDynamicDatasource = dynamicDatasourceService.create(dynamicDatasource);
    return ResponseModelUtils.success(savedDynamicDatasource);
  }

 /**
  * 更新动态数据源
  * @param dynamicDatasource
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新动态数据源")
  public ResponseModel update(@RequestBody DynamicDatasourceDTO dynamicDatasource) {
    DynamicDatasourceEntity savedDynamicDatasource = dynamicDatasourceService.update(dynamicDatasource);
    return ResponseModelUtils.success(savedDynamicDatasource);
  }

  /**
   * 根据主键ID删除数据
   * @param id
   * @return
   */
  @DeleteMapping("{id}")
  @ApiOperation("根据主键ID删除数据")
  public ResponseModel deleteById(@PathVariable String id) {
    dynamicDatasourceService.deleteById(id);
    return ResponseModelUtils.success();
  }

  /**
   * 根据ID查询动态数据源基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询动态数据源基本信息")
  public ResponseModel findById(@PathVariable String id) {
    DynamicDatasourceEntity dynamicDatasource = dynamicDatasourceService.findById(id);
    return ResponseModelUtils.success(dynamicDatasource);
  }

  /**
   * 分页查询
   * @param pageable
   * @return
   */
  @GetMapping("findPage")
  @ApiOperation(value = "分页查询", notes = "分页条件为page和size,page从1开始,size默认50")
  public ResponseModel findPage(@PageableDefault(page = 1, size = 50) Pageable pageable) {
    PageInfo<DynamicDatasourceEntity> page = dynamicDatasourceService.findPage(pageable);
    return ResponseModelUtils.success(page);
  }

  /**
   * 根据编码启用数据源
   * @param code
   * @return
   */
  @GetMapping("enableByCode")
  @ApiOperation(value = "根据编码启用数据源", notes = "不传参则是设置默认数据源")
  public ResponseModel enableByCode(@RequestParam(required = false) @ApiParam("数据源编码:可不传") String code) {
    String databaseCode = dynamicDatasourceService.enableByCode(code);
    return ResponseModelUtils.success(databaseCode);
  }

}
