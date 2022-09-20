package org.freshwater.boot.template.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/**
 * 数据库动态表更新DTO
 * @author tuxuchen
 * @date 2022/8/19 11:17
 */
@Data
@ApiModel(value = "数据库动态表更新DTO", description = "数据库动态表更新DTO")
public class DynamicTableUpdateDTO {

  /**
   * id
   */
  @ApiModelProperty(value = "id")
  @Length(max = 255, message = "id长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "id不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String id;

  /**
   * 编码
   */
  @ApiModelProperty(value = "编码")
  @Length(max = 32, message = "编码字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String code;

  /**
   * 表名
   */
  @ApiModelProperty(value = "表名")
  @TableField("table_name")
  @Length(max = 32, message = "表名字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "表名不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String tableName;

  /**
   * 名称
   */
  @ApiModelProperty(value = "名称")
  @Length(max = 32, message = "名称字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "名称不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String name;

  /**
   * 图标
   */
  @ApiModelProperty(value = "图标")
  @Length(max = 255, message = "图标字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String icon;

  /**
   * 状态,0:禁用,1:启用
   */
  @ApiModelProperty(value = "状态,0:禁用,1:启用")
  @NotNull(message = "状态,0:禁用,1:启用不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @Range(min = 0, max = 1, message = "状态值异常0:禁用,1:启用", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer state;

  /**
   * 动态表字段
   */
  @ApiModelProperty(value = "动态表字段")
  private List<DynamicTableFieldEntity> tableFields;
}
