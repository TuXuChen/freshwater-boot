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
 * 用户角色关联信息实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-07-22 17:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_user_role_mapping")
@ApiModel(value = "用户角色关联信息对象", description = "用户角色关联信息")
public class UserRoleMappingEntity extends UuidEntity {

  private static final long serialVersionUID = -5446102767798559545L;

  /**
   * 用户ID
   */
  @ApiModelProperty(value = "用户ID")
  @TableField("user_id")
  @Length(max = 255, message = "用户ID字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "用户ID不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String userId;

  /**
   * 角色ID
   */
  @ApiModelProperty(value = "角色ID")
  @TableField("role_id")
  @Length(max = 255, message = "角色ID字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "角色ID不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String roleId;


}
