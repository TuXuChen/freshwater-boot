package org.freshwater.boot.rbac.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色查询dto
 * @author tuxuchen
 * @date 2022/7/28 14:17
 */
@Data
@ApiModel("角色查询dto")
public class RoleQueryDTO {

  /**
   * 角色编码
   */
  @ApiModelProperty("角色编码")
  private String code;

  /**
   * 角色名称
   */
  @ApiModelProperty("角色名称")
  private String name;

  /**
   * 状态:0禁用,1:启用
   */
  @ApiModelProperty("状态:0禁用,1:启用")
  private Integer state;

}
