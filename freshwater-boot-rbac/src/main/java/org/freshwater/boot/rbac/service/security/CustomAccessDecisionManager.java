package org.freshwater.boot.rbac.service.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;
import org.freshwater.boot.rbac.configuration.RbacProperties;

import java.util.Collection;

/**
 * 权限决定的管理器
 * @author tuxuchen
 * @date 2022/7/25 13:57
 */
public class CustomAccessDecisionManager implements AccessDecisionManager {

  @Autowired
  private RbacProperties rbacProperties;

  @Override
  public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
    /**
     * 这里就是进行当前url请求需要的权限，和当前登录操作者所具备的权限进行对比
     * 1、当前url主要的权限从CustomFilterInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回
     * ,就是这个方法中的configAttributes参数。该集合中一定有值，如果没有则报错，如果有任何被写成了ANONYMOUS的权限角色，则允许操作
     * 2、当前登陆者所具有的权限为authentication
     * ,就是CustomUserSecurityDetailsService中循环添加到 GrantedAuthority 对象中的权限信息集合
     * 3、object 包含客户端发起的请求的requset信息
     *
     * 处理过程是：
     * 1、如果当前登录者具备超级管理员ADMIN的权限，就不需要进行判断，直接通过
     * 2、如果当前configAttributes没有任何角色信息，说明当前url并不需要权限控制，也直接通过
     * 3、否则就以configAttributes为标准进行循环，依次到authentication中进行判断
     * */
    // 1、=================
    if (CollectionUtils.isEmpty(configAttributes)) {
      throw new AccessDeniedException("not author!");
    }
    for (ConfigAttribute configAttribute : configAttributes) {
      String roleCode = configAttribute.getAttribute();
      if (StringUtils.equals(roleCode, "ANONYMOUS")) {
        return;
      }
    }
    // 2、=================
    Collection<? extends GrantedAuthority> currentAuthors = authentication.getAuthorities();
    if (CollectionUtils.isEmpty(currentAuthors)) {
      throw new AccessDeniedException("not found any author from this single in user!");
    }
    // 发现在配置文件中，设定了不需要进行方法级验证的角色，就可以直接通过
    String[] ignoreMethodCheckRoles = rbacProperties.getIgnoreMethodCheckRoles();
    for (GrantedAuthority grantedAuthority : currentAuthors) {
      for (String ignoreMethodCheckRole : ignoreMethodCheckRoles) {
        if (StringUtils.equalsIgnoreCase(grantedAuthority.getAuthority(), ignoreMethodCheckRole)) {
          return;
        }
      }
    }
    // 3、================
    for (ConfigAttribute securityConfig : configAttributes) {
      for (GrantedAuthority grantedAuthority : currentAuthors) {
        // 如果条件成立，则说明当前登陆者具备访问这个url的权限
        if (StringUtils.equalsIgnoreCase(securityConfig.getAttribute(), grantedAuthority.getAuthority())) {
          return;
        }
      }
    }
    throw new AccessDeniedException("not author!");
  }

  @Override
  public boolean supports(ConfigAttribute configAttribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return true;
  }

}
