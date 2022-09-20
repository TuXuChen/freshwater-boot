package org.freshwater.boot.template.service.jdbc;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 表定义
 * @Author: Paul Chan
 * @Date: 2021/7/23 17:21
 */
@Data
public class Table {

  /**
   * 表名
   */
  @NotBlank(message = "表名不能为空!")
  private String name;

  /**
   * 表说明
   */
  @NotBlank(message = "表说明不能为空!")
  private String description;

  /**
   * 表字段
   */
  @Valid
  @NotEmpty(message = "表字段不能为空")
  private List<TableField> fields;

}
