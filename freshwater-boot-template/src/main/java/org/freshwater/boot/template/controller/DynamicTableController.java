package org.freshwater.boot.template.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.freshwater.boot.template.dto.DynamicTableCreateDTO;
import org.freshwater.boot.template.dto.DynamicTableQueryDTO;
import org.freshwater.boot.template.dto.DynamicTableUpdateDTO;
import org.freshwater.boot.template.dto.ExecuteModel;
import org.freshwater.boot.template.entity.DynamicTableEntity;
import org.freshwater.boot.template.service.DynamicTableService;
import org.freshwater.boot.template.vo.DynamicTableDetailsVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 动态表 前端控制器
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-10
 */
@Api(value = "动态表管理", tags = {"动态表管理"})
@RestController
@RequestMapping("/v1/dynamicTables")
public class DynamicTableController {

  @Autowired
  private DynamicTableService dynamicTableService;

  /**
   * 创建动态表
   * @param table
   * @return
   */
  @PostMapping("")
  @ApiOperation("创建动态表")
  public ResponseModel create(@RequestBody DynamicTableCreateDTO table) {
    DynamicTableEntity savedDynamicTable = dynamicTableService.create(table);
    return ResponseModelUtils.success(savedDynamicTable);
  }

 /**
  * 更新动态表
  * @param table
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新动态表")
  public ResponseModel update(@RequestBody DynamicTableUpdateDTO table) {
    DynamicTableEntity savedDynamicTable = dynamicTableService.update(table);
    return ResponseModelUtils.success(savedDynamicTable);
  }

  /**
   * 根据ID查询动态表基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询动态表基本信息")
  public ResponseModel findById(@PathVariable String id) {
    DynamicTableEntity dynamicTable = dynamicTableService.findById(id);
    return ResponseModelUtils.success(dynamicTable);
  }

  /**
   * 根据id查询详情信息
   * @param id
   * @return
   */
  @GetMapping("findDetailsById")
  @ApiOperation("根据id查询详情信息")
  public ResponseModel findDetailsById(@RequestParam @ApiParam("id") String id) {
    DynamicTableDetailsVO dynamicTableDetailsVO = dynamicTableService.findDetailsById(id);
    return ResponseModelUtils.success(dynamicTableDetailsVO);
  }

  /**
   * 根据编码查询详情信息
   * @param code
   * @return
   */
  @GetMapping("findDetailsByCode")
  @ApiOperation("根据编码查询详情信息")
  public ResponseModel findDetailsByCode(@RequestParam @ApiParam("编码") String code) {
    DynamicTableDetailsVO dynamicTableDetailsVO = dynamicTableService.findDetailsByCode(code);
    return ResponseModelUtils.success(dynamicTableDetailsVO);
  }

  /**
   * 多条件分页查询
   * @param pageable
   * @param query
   * @return
   */
  @GetMapping("findByConditions")
  @ApiOperation(value = "多条件分页查询", notes = "分页条件为page和size,page从1开始,size默认50")
  public ResponseModel findByConditions(DynamicTableQueryDTO query, @PageableDefault(page = 1, size = 50) Pageable pageable) {
    PageInfo<DynamicTableEntity> page = dynamicTableService.findByConditions(query, pageable);
    return ResponseModelUtils.success(page);
  }

  /**
   * 多条件查询
   * @param query
   * @return
   */
  @GetMapping("findAllByConditions")
  @ApiOperation(value = "多条件查询")
  public ResponseModel findAllByConditions(DynamicTableQueryDTO query) {
    List<DynamicTableEntity> list = dynamicTableService.findAllByConditions(query);
    return ResponseModelUtils.success(list);
  }

  /**
   * 执行新增动态表数据
   * @param code
   * @param data
   * @return
   */
  @PostMapping("executeInsert")
  @ApiOperation("执行新增动态表数据")
  public ResponseModel executeInsert(@RequestParam @ApiParam("动态表编码") String code, @RequestBody Map<String, Object> data) {
    Map<String, Object> result = dynamicTableService.executeInsert(code, data);
    return ResponseModelUtils.success(result);
  }

  /**
   * 执行更新动态表数据
   * @param code
   * @param data
   * @return
   */
  @PutMapping("executeUpdate")
  @ApiOperation("执行更新动态表数据")
  public ResponseModel executeUpdate(@RequestParam @ApiParam("动态表编码") String code, @RequestBody Map<String, Object> data) {
    Map<String, Object> result = dynamicTableService.executeUpdate(code, data);
    return ResponseModelUtils.success(result);
  }

  /**
   * 执行根据ID查询动态表数据
   * @return
   */
  @GetMapping("executeQueryById")
  @ApiOperation("执行根据ID查询动态表数据")
  public ResponseModel executeQueryById(@RequestParam @ApiParam("动态表编码") String code,
                                        @RequestParam @ApiParam("主键ID") String id) {
    Map<String, Object> result = dynamicTableService.executeQueryById(code, id);
    return ResponseModelUtils.success(result);
  }

  /**
   * 执行动态表查询
   * @return
   */
  @GetMapping("executeQuery")
  @ApiOperation("执行动态表查询")
  public ResponseModel executeQuery(@RequestBody ExecuteModel executeModel) {
    List<Map<String, Object>> result = dynamicTableService.executeQuery(executeModel);
    return ResponseModelUtils.success(result);
  }

  /**
   * 执行动态表查询分页数据
   * @return
   */
  @GetMapping("executeQueryPage")
  @ApiOperation("执行动态表查询分页数据")
  public ResponseModel executeQueryPage(@RequestBody ExecuteModel executeModel) {
    PageInfo result = dynamicTableService.executeQueryPage(executeModel);
    return ResponseModelUtils.success(result);
  }

  /**
   * 执行Excel动态表数据导出
   * @param executeModel
   * @return
   */
  @GetMapping("executeExportQuery")
  @ApiOperation("执行Excel动态表数据导出")
  public ResponseModel executeExportQuery(@RequestBody ExecuteModel executeModel) {
    dynamicTableService.executeExportQuery(executeModel);
    return ResponseModelUtils.success();
  }

}
