package org.freshwater.boot.rbac.service.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录信息承载模型
 * @author tuxuchen
 * @date 2022/7/25 11:17
 */
@Getter
@Setter
@ToString
public class LoginDetails extends WebAuthenticationDetails {

  /**
   * 登陆用户名
   */
  private String username;

  /**
   * 登录输入的密码
   */
  private String password;

  /**
   * 获取登录信息
   * @param request
   */
  public LoginDetails(HttpServletRequest request) {
    super(request);
    this.username = request.getParameter("username");
    this.password = request.getParameter("password");
  }

}
