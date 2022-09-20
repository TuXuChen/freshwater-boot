package org.freshwater.boot.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.freshwater.boot.common.entity.UuidEntity;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDateTime;

/**
 * <p>
 * 按钮实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-07-22 17:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_button")
@ApiModel(value = "按钮对象", description = "按钮")
public class ButtonEntity extends UuidEntity {

  private static final long serialVersionUID = 7217581740161980227L;

  /**
   * 状态,0:禁用,1:启用
   */
  @ApiModelProperty(value = "状态,0:禁用,1:启用")
  @Range(min = 0, max = 1, groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotNull(message = "状态,0:禁用,1:启用不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer state;

  /**
   * 类型:1:web平台,2:APP
   */
  @ApiModelProperty(value = "类型:1:web平台,2:APP")
  @Range(min = 1, max = 2, groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotNull(message = "类型:1:web平台,2:APP不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer type;

  /**
   * 按钮编码
   */
  @ApiModelProperty(value = "按钮编码")
  @Length(max = 32, message = "按钮编码字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "按钮编码不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String code;

  /**
   * 按钮名称
   */
  @ApiModelProperty(value = "按钮名称")
  @Length(max = 64, message = "按钮名称字符长度不能超过64!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "按钮名称不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String name;

  /**
   * 菜单ID
   */
  @ApiModelProperty(value = "菜单ID")
  @TableField("menu_id")
  @Length(max = 255, message = "菜单ID字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "菜单ID不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String menuId;

  /**
   * 可访问的url
   */
  @ApiModelProperty(value = "可访问的url")
  @Length(max = 2000, message = "可访问的url字符长度不能超过2000!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String urls;

  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间")
  @TableField("create_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  /**
   * 最后修改时间
   */
  @ApiModelProperty(value = "最后修改时间")
  @TableField("modify_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime modifyTime;

  /**
   * 是否是选中状态
   */
  @TableField(exist = false)
  @ApiModelProperty(value = "是否是选中状态")
  private Boolean checked;

}
