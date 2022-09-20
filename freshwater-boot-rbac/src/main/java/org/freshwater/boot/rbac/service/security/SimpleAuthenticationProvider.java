package org.freshwater.boot.rbac.service.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.freshwater.boot.common.constants.enums.NormalStatusEnum;
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.service.UserService;
import org.freshwater.boot.rbac.utils.SecurityUtils;

import java.util.List;

/**
 * 登录认证实现
 * @author tuxuchen
 * @date 2022/7/25 11:33
 */
@Slf4j
public class SimpleAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    try {
      LoginDetails loginDetails = (LoginDetails) authentication.getDetails();
      log.info("用户正在登录:{}", loginDetails.toString());
      String username = loginDetails.getUsername();
      String password = loginDetails.getPassword();
      Validate.notBlank(username, "用户名不能为空!");
      Validate.notBlank(password, "密码不能为空!");
      UserEntity user = userService.findByUserAccount(username);
      Validate.notNull(user, "当前用户不存在!");
      Validate.isTrue(NormalStatusEnum.ENABLE.getStatus().equals(user.getState()), "账号已禁用，请联系管理员!");
      boolean matches = passwordEncoder.matches(password, user.getPassword());
      Validate.isTrue(matches, "密码错误!");
      List<? extends GrantedAuthority> authorities = SecurityUtils.getAuthorityByUserId(user.getId());
      return new UsernamePasswordAuthenticationToken(user.getUserAccount(), password, authorities);
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
      throw new AuthenticationCredentialsNotFoundException(e.getMessage(), e);
    }
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return true;
  }
}
