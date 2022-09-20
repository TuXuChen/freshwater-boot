package org.freshwater.boot.rbac.service.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义的登录信息服务
 * @author tuxuchen
 * @date 2022/7/25 11:30
 */
public class SimpleAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

  @Override
  public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
    return new LoginDetails(context);
  }

}
