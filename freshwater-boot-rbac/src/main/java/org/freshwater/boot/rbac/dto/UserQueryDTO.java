package org.freshwater.boot.rbac.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户查询参数DTO
 * @author tuxuchen
 * @date 2022/7/25 10:50
 */
@Data
public class UserQueryDTO {

  /**
   * 账号
   */
  @ApiModelProperty(value = "账号")
  private String userAccount;

  /**
   * 账号(手机号)
   */
  @ApiModelProperty(value = "账号(手机号)")
  private String mobile;

  /**
   * 用户姓名
   */
  @ApiModelProperty(value = "用户姓名")
  private String realName;

  /**
   * 用户状态,0:禁用,1:启用
   */
  @ApiModelProperty(value = "用户状态,0:禁用,1:启用")
  private Integer state;

}
