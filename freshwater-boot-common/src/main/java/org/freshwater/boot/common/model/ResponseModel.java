package org.freshwater.boot.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用数据返回模型
 * @author tuxuchen
 * @date 2022/6/17 14:38
 */
@Data
@ApiModel("通用数据返回模型")
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel implements Serializable {

  private static final long serialVersionUID = -782280585672184123L;

  /**
   * 请求时间
   */
  @ApiModelProperty("请求时间")
  private Long timestamp = System.currentTimeMillis();

  /**
   * 请求编码
   */
  @ApiModelProperty("请求编码")
  private ResponseCode code;

  /**
   * 请求结果
   */
  @ApiModelProperty("请求结果")
  private Boolean success = true;

  /**
   * 请求数据
   */
  @ApiModelProperty("请求数据")
  private Object data;

  /**
   * 成功消息
   */
  @ApiModelProperty("成功消息")
  private String message;

  /**
   * 异常信息
   */
  @ApiModelProperty("异常信息")
  private String errorMessage;
}
