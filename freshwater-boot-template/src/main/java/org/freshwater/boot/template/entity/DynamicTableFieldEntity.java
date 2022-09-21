package org.freshwater.boot.template.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.freshwater.boot.common.entity.UuidEntity;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * <p>
 * 动态表字段实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-08-10 16:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("freshwater_dynamic_table_field")
@ApiModel(value = "动态表字段对象", description = "动态表字段")
public class DynamicTableFieldEntity extends UuidEntity {

  private static final long serialVersionUID = 5022717881021972304L;

  /**
   * 动态表ID
   */
  @ApiModelProperty(value = "动态表ID")
  @TableField("table_id")
  @Length(max = 255, message = "动态表ID字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String tableId;

  /**
   * 字段名
   */
  @ApiModelProperty(value = "字段名")
  @Length(max = 32, message = "字段名字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "字段名不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String name;

  /**
   * 状态,0:禁用,1:启用
   */
  @ApiModelProperty(value = "状态,0:禁用,1:启用")
  @NotNull(message = "状态,0:禁用,1:启用不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @Range(min = 0, max = 1, message = "状态值异常:状态,0:禁用,1:启用", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer state;

  /**
   * 字段说明
   */
  @ApiModelProperty(value = "字段说明")
  @Length(max = 32, message = "字段说明字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "字段说明不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String description;

  /**
   * 字段类型,文本:string, 整型:int, 长整型:long, 日期:date, 时间:datetime, 浮点型:decimal, 布尔类型:boolean
   */
  @ApiModelProperty(value = "字段类型,文本:string, 整型:int, 长整型:long, 日期:date, 时间:datetime, 浮点型:decimal, 布尔类型:boolean")
  @Length(max = 32, message = "字段类型字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "字段类型不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String type;

  /**
   * 显示类型,1:文本,2图片,3:视频,4:时间,5:省市区,6:日期,7:码值,8:文本域,9:商品,10:url
   */
  @ApiModelProperty(value = "显示类型,1:文本,2图片,3:视频,4:时间,5:省市区,6:日期,7:码值,8:文本域,9:商品,10:url")
  @TableField("show_type")
  @NotNull(message = "显示类型,1:文本,2图片,3:视频,4:时间,5:省市区,6:日期,7:码值,8:文本域,9:商品,10:url不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @Range(min = 1, max = 10, message = "不支持的显示类型,1:文本,2图片,3:视频,4:时间,5:省市区,6:日期,7:码值,8:文本域,9:商品,10:url", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer showType;

  /**
   * 序号
   */
  @ApiModelProperty(value = "序号")
  @TableField("sort_index")
  @NotNull(message = "序号不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer sortIndex;

  /**
   * 长度
   */
  @ApiModelProperty(value = "长度")
  @NotNull(message = "长度不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer length;

  /**
   * 是否可以为null
   */
  @ApiModelProperty(value = "是否可以为null")
  @NotNull(message = "是否可以为null不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean nullable;

  /**
   * 是否是主键
   */
  @ApiModelProperty(value = "是否是主键")
  @TableField("is_primary_key")
  @NotNull(message = "是否是主键不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean isPrimaryKey;

  /**
   * 是否是唯一
   */
  @ApiModelProperty(value = "是否是唯一")
  @TableField("is_unique")
  @NotNull(message = "是否是唯一不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean isUnique;

  /**
   * 是否可编辑
   */
  @ApiModelProperty(value = "是否可编辑")
  @NotNull(message = "是否可编辑不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean editable;

  /**
   * 是否在列表上展示
   */
  @ApiModelProperty(value = "是否在列表上展示")
  @TableField("is_list_view")
  @NotNull(message = "是否在列表上展示不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean isListView;

  /**
   * 是否加入搜索条件
   */
  @ApiModelProperty(value = "是否加入搜索条件")
  @TableField("is_search")
  @NotNull(message = "是否加入搜索条件不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean isSearch;

  /**
   * 是否渲染输入框
   */
  @ApiModelProperty(value = "是否渲染输入框")
  @TableField("is_render_input")
  @NotNull(message = "是否渲染输入框不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean isRenderInput;

}
