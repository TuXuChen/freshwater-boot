package org.freshwater.boot.rbac.service.security;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.CollectionUtils;
import org.freshwater.boot.rbac.configuration.RbacProperties;
import org.freshwater.boot.rbac.constants.Constants;
import org.freshwater.boot.rbac.constants.enums.ButtonType;
import org.freshwater.boot.rbac.service.ButtonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 处理每个接口访问时,判断其是否有权限访问
 * @author tuxuchen
 * @date 2022/7/25 15:08
 */
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

  /**
   * 匿名用户鉴权体系
   */
  private static final SecurityConfig ANONYMOUS_CONFIG = new SecurityConfig("ANONYMOUS");

  @Autowired
  private RbacProperties rbacProperties;
  @Autowired
  private ButtonService buttonService;

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    List<ConfigAttribute> configAttributes = Lists.newArrayList();
    FilterInvocation filterInvocation = (FilterInvocation) object;
    HttpServletRequest request = filterInvocation.getRequest();
    // 如果当前访问url是忽略权限的,则通过
    Set<String> allIgnoreUrls = rbacProperties.getAllIgnoreUrls();
    for (String ignoreUrl : allIgnoreUrls) {
      AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(ignoreUrl);
      if (requestMatcher.matches(request)) {
        configAttributes.add(ANONYMOUS_CONFIG);
        return configAttributes;
      }
    }
    // 判断当前登录用户的角色有没有忽略的角色, 如果有则直接返回
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || Constants.ANONYMOUS_USER.equals(authentication.getName())) {
      throw new AccessDeniedException("系统未登录");
    }
    List<String> roleCodes = authentication.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.toList());
    String[] ignoreMethodCheckRoles = rbacProperties.getIgnoreMethodCheckRoles();
    for (String roleCode : roleCodes) {
      if (StringUtils.equalsAnyIgnoreCase(roleCode, ignoreMethodCheckRoles)) {
        configAttributes.add(ANONYMOUS_CONFIG);
        return configAttributes;
      }
    }
    // 判断当前访问接口是不是在登录后忽略的url中 如果当前访问url是忽略权限的,则通过
    Set<String> ignoreUrlsForLogged = rbacProperties.getAllIgnoreUrlsForLogged();
    for (String url : ignoreUrlsForLogged) {
      AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(url);
      if (requestMatcher.matches(request)) {
        configAttributes.add(ANONYMOUS_CONFIG);
        return configAttributes;
      }
    }
    // 判断当前用户是否有访问权限
    String account = authentication.getName();
    HttpSession session = request.getSession();
    Integer buttonType = (Integer) session.getAttribute("BUTTON_TYPE");
    buttonType = ObjectUtils.defaultIfNull(buttonType, ButtonType.BUTTON_WEB.getType());
    Set<String> authUrls = buttonService.findUrlsByUserAccountAndTypeCache(account, buttonType);
    if (CollectionUtils.isEmpty(authUrls)) {
      throw new AccessDeniedException("没有权限访问该接口");
    }
    String currentMethod = request.getMethod();
    for (String authUrl : authUrls) {
      String[] methodUrl = authUrl.split(":");
      if (methodUrl.length < 2) {
        continue;
      }
      String method = methodUrl[0];
      String url = methodUrl[1];
      if (!currentMethod.equalsIgnoreCase(method)) {
        continue;
      }
      AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(url);
      if (requestMatcher.matches(request)) {
        // 如果当前访问url是忽略权限的,则通过
        configAttributes.add(ANONYMOUS_CONFIG);
        return configAttributes;
      }
    }
    if (CollectionUtils.isEmpty(configAttributes)) {
      throw new AccessDeniedException("没有权限访问该接口");
    }
    return configAttributes;
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return Collections.EMPTY_LIST;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return true;
  }

}
