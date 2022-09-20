package org.freshwater.boot.template.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;
import org.freshwater.boot.template.service.DynamicTableFieldService;

import java.util.List;

/**
 * <p>
 *  动态表字段前端控制器
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-10
 */
@Api(value = "动态表字段管理", tags = {"动态表字段管理"})
@RestController
@RequestMapping("/v1/dynamicTableFields")
public class DynamicTableFieldController {

  @Autowired
  private DynamicTableFieldService dynamicTableFieldService;

  /**
   * 根据表ID查询字段信息
   * @param tableId
   * @return
   */
  @GetMapping("findByTableId")
  @ApiOperation("根据表ID查询字段信息")
  public ResponseModel findByTableId(@RequestParam @ApiParam("tableId") String tableId) {
    List<DynamicTableFieldEntity> fields = dynamicTableFieldService.findByTableId(tableId);
    return ResponseModelUtils.success(fields);
  }

}
