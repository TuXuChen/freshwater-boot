package org.freshwater.boot.template.service.jdbc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 表字段
 * @Author: Paul Chan
 * @Date: 2021/7/23 17:11
 */
@Data
public class TableField {

  /**
   * 字段名
   */
  @NotBlank(message = "字段名不能为空!")
  private String name;

  /**
   * 字段类型, 对应mysql中的字段类型
   */
  @NotBlank(message = "字段类型不能为空!")
  private String type;

  /**
   * 字段长度
   */
  @NotNull(message = "字段长度不能为空!")
  @Min(value = 0, message = "字段长度不能小于0")
  private Integer length;

  /**
   * 小数点
   */
  @NotNull(message = "小数点不能为空!")
  @Min(value = 0, message = "小数点不能小于0")
  private Integer radix;

  /**
   * 是否可以为null
   */
  @NotNull(message = "是否可以为null不能为空!")
  private Boolean nullable;

  /**
   * 是否是主键
   */
  @NotNull(message = "是否是主键不能为空!")
  @ApiModelProperty(value = "是否是主键")
  private Boolean isPrimaryKey;

  /**
   * 是否是唯一
   */
  @NotNull(message = "是否是唯一不能为空!")
  @ApiModelProperty(value = "是否是唯一")
  private Boolean isUnique;

  /**
   * 字段说明
   */
  @NotNull(message = "字段说明不能为空!")
  @ApiModelProperty(value = "字段说明")
  private String desc;

  /**
   * 是否存在
   */
  @NotNull(message = "是否存在不能为空!")
  @ApiModelProperty(value = "是否存在")
  private Boolean isExit;

}
