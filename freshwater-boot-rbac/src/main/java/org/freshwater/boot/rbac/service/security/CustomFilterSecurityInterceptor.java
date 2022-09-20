package org.freshwater.boot.rbac.service.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 自定义的投票组，投票策略、路径资源鉴权处理器、资源-权限读取器
 * @author tuxuchen
 * @date 2022/7/25 13:56
 */
public class CustomFilterSecurityInterceptor extends FilterSecurityInterceptor {

  /**
   * @param securityMetadataSource 资源-权限读取器
   * @param accessDecisionManager  决策器
   * @param authenticationManager  投票策略，一般从WebSecurityConfigurerAdapter总来，投票策略为只要一个鉴权通过就算通过
   */
  public CustomFilterSecurityInterceptor(FilterInvocationSecurityMetadataSource securityMetadataSource, AccessDecisionManager accessDecisionManager, AuthenticationManager authenticationManager) {
    this.setSecurityMetadataSource(securityMetadataSource);
    this.setAccessDecisionManager(accessDecisionManager);
    this.setAuthenticationManager(authenticationManager);
  }

}
