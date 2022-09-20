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
 * 用户实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-07-22 16:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_user")
@ApiModel(value = "用户对象", description = "用户")
public class UserEntity extends OpUuidEntity {

  private static final long serialVersionUID = -3194364123908364217L;

  /**
   * 用户登录账号
   */
  @ApiModelProperty(value = "用户登录账号")
  @TableField("user_account")
  @Length(max = 32, message = "用户登录账号字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "用户登录账号不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String userAccount;

  /**
   * 手机号
   */
  @ApiModelProperty(value = "手机号")
  @Length(max = 32, message = "手机号字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "手机号不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String mobile;

  /**
   * 密码
   */
  @ApiModelProperty(value = "密码")
  @Length(max = 255, message = "密码字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "密码不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String password;

  /**
   * 用户姓名
   */
  @ApiModelProperty(value = "用户姓名")
  @TableField("real_name")
  @Length(max = 255, message = "用户姓名字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "用户姓名不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String realName;

  /**
   * 邮箱
   */
  @ApiModelProperty(value = "邮箱")
  @Length(max = 255, message = "邮箱字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String email;

  /**
   * 状态:0禁用,1:启用
   */
  @ApiModelProperty(value = "状态:0禁用,1:启用")
  @NotNull(message = "状态:0禁用,1:启用不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer state;

}
