package org.freshwater.boot.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;

/**
 * 通用数据返回请求编码
 * @author tuxuchen
 * @date 2022/6/17 14:40
 */
@ApiModel("通用数据返回请求编码")
public enum ResponseCode {

  E0(0, "请求成功"),
  E3001(3001, "请求次数已经超过本周期设置的最大值"),
  E3002(3002, "请求频率已超过设定的最大值"),
  E3003(3003, "该接口工作繁忙，产生拥堵，请稍后再试。"),
  E3004(3004, ""),
  E4001(4001, "规定的必传入项没有传入"),
  E4002(4002, "传入的参数项格式不符合规定"),
  E4003(4003, "传入参数项至少有一项超出规定的长度"),
  E4004(4004, "没有查询到符合条件的数据"),
  E4005(4005, "查询到重复数据"),
  E4006(4006, "传入的参数编码格式失效"),
  E4007(4007, "未查询到指定信息"),
  E5001(5001, "服务器内部错误"),
  E5002(5002, "插入操作错误"),
  E5003(5003, "更新操作错误"),
  E5004(5004, "XMPP服务连接暂时失效"),
  E5005(5005, "数据结构性错误"),
  E5008(5008, "验证码错误"),
  E4008(4008, "用户名参数错误，导致登录失败"),
  E4009(4009, "密码参数错误，导致登录失败"),
  E6000(6000, "发出的请求有错误，服务器没有进行新建或修改数据的操作。"),
  E6001(6001, "用户没有登录，或登录失效（令牌、用户名、密码错误）。"),
  E6002(6002, "用户没有得到授权，访问是被禁止的。"),
  E6003(6003, "用户得到授权，但是访问是被禁止的。"),
  E6004(6004, "发出的请求针对的是不存在的记录，服务器没有进行操作。"),
  E6006(6006, "请求的格式不可得。"),
  E6010(6010, "请求的资源被永久删除，且不会再得到的。"),
  E6022(6022, "当创建一个对象时，发生一个验证错误。"),
  E7000(7000, "服务器发生错误，请检查服务器。"),
  E7002(7002, "网关错误"),
  E7003(7003, "服务不可用，服务器暂时过载或维护。"),
  E7004(7004, "网关超时。");

  ResponseCode(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  /**
   * 标记编码
   */
  private Integer code;

  /**
   * 说明
   */
  private String desc;

  /**
   * 根据编码获取具体的值
   * @param code
   * @return
   */
  @JsonCreator
  public static ResponseCode valueOfCode(Integer code) {
    if(code == null) {
      return null;
    }
    for (ResponseCode value : values()) {
      if(value.code.equals(code)) {
        return value;
      }
    }
    return null;
  }

  @JsonValue
  public Integer getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

}
