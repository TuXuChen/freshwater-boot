package org.freshwater.boot.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.freshwater.boot.common.entity.UuidEntity;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

/**
 * <p>
 * 角色菜单关联信息实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-07-22 17:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_role_menu_mapping")
@ApiModel(value = "角色菜单关联信息对象", description = "角色菜单关联信息")
public class RoleMenuMappingEntity extends UuidEntity {

  private static final long serialVersionUID = -6666474185253816219L;

  /**
   * 角色ID
   */
  @ApiModelProperty(value = "角色ID")
  @TableField("role_id")
  @Length(max = 255, message = "角色ID字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "角色ID不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String roleId;

  /**
   * 菜单ID
   */
  @ApiModelProperty(value = "菜单ID")
  @TableField("menu_id")
  @Length(max = 255, message = "菜单ID字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "菜单ID不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String menuId;


}
