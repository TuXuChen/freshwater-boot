package org.freshwater.boot.template.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 动态表字段多条件查询
 * @author tuxuchen
 * @date 2022/8/22 11:42
 */
@Data
public class DynamicTableFieldQueryDTO {

  /**
   * 动态表ID
   */
  @ApiModelProperty(value = "动态表ID")
  private String tableId;

  /**
   * 字段名
   */
  @ApiModelProperty(value = "字段名")
  private String name;

  /**
   * 字段说明
   */
  @ApiModelProperty(value = "字段说明")
  private String description;

  /**
   * 是否在列表上展示
   */
  @ApiModelProperty(value = "是否在列表上展示")
  private Boolean isListView;

  /**
   * 状态,0:禁用,1:启用
   */
  @ApiModelProperty(value = "状态,0:禁用,1:启用")
  private Integer state;

}
