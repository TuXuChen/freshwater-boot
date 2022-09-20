package org.freshwater.boot.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

/**
 * 用户注册的dto
 * @author tuxuchen
 * @date 2022/7/28 14:44
 */
@Data
@ApiModel("用户注册的dto")
public class UserRegisterDTO {

  /**
   * 手机号
   */
  @ApiModelProperty(value = "手机号")
  @Length(max = 11, message = "手机号字符长度不能超过11!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "用户登录账号(手机号)不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String mobile;

  /**
   * 登录密码
   */
  @ApiModelProperty(value = "登录密码")
  @Length(min = 6, max = 255, message = "登录密码字符长度不能小于6或超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "登录密码不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String password;

}
