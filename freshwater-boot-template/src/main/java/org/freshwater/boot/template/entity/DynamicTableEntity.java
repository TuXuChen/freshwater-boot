package org.freshwater.boot.template.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.freshwater.boot.common.entity.OpUuidEntity;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * <p>
 * 动态表实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-08-10 16:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_dynamic_table")
@ApiModel(value = "动态表对象", description = "动态表")
public class DynamicTableEntity extends OpUuidEntity {

  private static final long serialVersionUID = 5620789681701630349L;

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
  private Integer state;


}
