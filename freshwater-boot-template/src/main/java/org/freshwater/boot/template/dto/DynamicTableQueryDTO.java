package org.freshwater.boot.template.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 动态表查询条件的DTO
 * @author tuxuchen
 * @date 2022/8/19 14:04
 */
@Data
public class DynamicTableQueryDTO {

  /**
   * 编码
   */
  @ApiModelProperty(value = "编码")
  private String code;

  /**
   * 名称
   */
  @ApiModelProperty(value = "名称")
  private String name;

  /**
   * 状态,0:禁用,1:启用
   */
  @ApiModelProperty(value = "状态,0:禁用,1:启用")
  private Integer state;

  /**
   * 开始时间
   */
  @ApiModelProperty(value = "开始时间")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  /**
   * 结束时间
   */
  @ApiModelProperty(value = "结束时间")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

}
