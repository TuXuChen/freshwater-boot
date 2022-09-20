package org.freshwater.boot.rbac.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.freshwater.boot.rbac.configuration.JwtProperties;
import org.freshwater.boot.rbac.utils.SecurityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 自定义一个过滤器用来进行识别jwt。
 * @author tuxuchen
 * @date 2022/8/3 17:16
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

  @Autowired
  private JwtProperties jwtProperties;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader(jwtProperties.getHeader());
    if (StringUtils.isBlank(header)) {
      chain.doFilter(request, response);
      return;
    }
    Claims token = jwtProperties.getClaimsByToken(header);
    if (Objects.isNull(token)) {
      throw new JwtException("token异常");
    }
    if (jwtProperties.isTokenExpired(token)) {
      log.info("jwt过期");
      throw new JwtException("token过期");
    }
    String username = token.getSubject();
    List<? extends GrantedAuthority> authorities = SecurityUtils.getAuthorityByUsername(username);
    // 获取用户权限等信息
    UsernamePasswordAuthenticationToken authentication =
        // 参数: 用户名 密码 权限信息
        new UsernamePasswordAuthenticationToken(username, null, authorities);
    // 后续security就能获取到当前登录的用户信息了，也就完成了用户认证。
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

}
