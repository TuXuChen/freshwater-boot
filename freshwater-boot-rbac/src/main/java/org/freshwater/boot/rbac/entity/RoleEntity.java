package org.freshwater.boot.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.freshwater.boot.common.entity.OpUuidEntity;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * <p>
 * 角色实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-07-22 17:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_role")
@ApiModel(value = "角色对象", description = "角色")
public class RoleEntity extends OpUuidEntity {

  private static final long serialVersionUID = -4405765043871483801L;

  /**
   * 角色编码
   */
  @ApiModelProperty(value = "角色编码")
  @Length(max = 64, message = "角色编码字符长度不能超过64!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "角色编码不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String code;

  /**
   * 角色名称
   */
  @ApiModelProperty(value = "角色名称")
  @Length(max = 32, message = "角色名称字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "角色名称不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String name;

  /**
   * 状态:0禁用,1启用
   */
  @ApiModelProperty(value = "状态:0禁用,1启用")
  @NotNull(message = "状态:0禁用,1启用不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer state;

  /**
   * 是否是系统角色
   */
  @ApiModelProperty(value = "是否是系统角色")
  @TableField("is_system")
  @NotNull(message = "是否是系统角色不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean isSystem;


}
