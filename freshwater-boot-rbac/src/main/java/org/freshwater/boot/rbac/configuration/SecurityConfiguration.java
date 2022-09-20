package org.freshwater.boot.rbac.configuration;

import org.freshwater.boot.rbac.configuration.handle.SimpleAccessDeniedHandler;
import org.freshwater.boot.rbac.configuration.handle.SimpleAuthenticationEntryPoint;
import org.freshwater.boot.rbac.configuration.handle.SimpleAuthenticationFailureHandler;
import org.freshwater.boot.rbac.configuration.handle.SimpleAuthenticationSuccessHandler;
import org.freshwater.boot.rbac.configuration.handle.SimpleLogoutSuccessHandler;
import org.freshwater.boot.rbac.service.security.CustomAccessDecisionManager;
import org.freshwater.boot.rbac.service.security.CustomFilterInvocationSecurityMetadataSource;
import org.freshwater.boot.rbac.service.security.CustomFilterSecurityInterceptor;
import org.freshwater.boot.rbac.service.security.SimpleAuthenticationDetailsSource;
import org.freshwater.boot.rbac.service.security.SimpleAuthenticationProvider;
import org.freshwater.boot.rbac.service.security.UserSecurityDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 登陆和访问权限的相关配置
 * @author tuxuchen
 * @date 2022/7/25 16:27
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(RbacProperties.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private RbacProperties rbacProperties;
  @Autowired
  private AccessDeniedHandler accessDeniedHandler;
  @Autowired
  private LogoutSuccessHandler logoutSuccessHandler;
  @Autowired
  private AccessDecisionManager accessDecisionManager;
  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;
  @Autowired
  private AuthenticationSuccessHandler authenticationSuccessHandler;
  @Autowired
  private AuthenticationFailureHandler authenticationFailureHandler;
  @Autowired
  private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;
  @Autowired
  private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CorsConfigurationSource corsConfigurationSource = this.corsConfigurationSource();
    CustomFilterSecurityInterceptor filterSecurityInterceptor = new CustomFilterSecurityInterceptor(filterInvocationSecurityMetadataSource, accessDecisionManager, super.authenticationManager());
    Set<String> allIgnoreUrls = rbacProperties.getAllIgnoreUrls();
    http
        .addFilterAt(filterSecurityInterceptor, FilterSecurityInterceptor.class)
        // 允许登录操作时跨域
        .cors().configurationSource(corsConfigurationSource).and()
        // 允许iframe嵌入
        .headers().frameOptions().disable().and()
        .sessionManagement()
        .enableSessionUrlRewriting(true).and()
        // 禁用Session 不生成session
        //.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        // 自定义JWT识别过滤器
        // .addFilter(jwtAuthenticationFilter())
        .authorizeRequests()
        //对preflight放行
        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
        // 系统中的“登录页面”在被访问时，不进行权限控制
        .antMatchers(allIgnoreUrls.toArray(new String[allIgnoreUrls.size()])).permitAll()
        // 其它访问都要验证权限
        .anyRequest().authenticated().and()
        // =================== 无权限访问资源或者用户登录信息过期时，会出发这个异常处理器
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .accessDeniedHandler(accessDeniedHandler).and()
        // ==================== 设定登录页的url地址，它不进行权限控制
        .formLogin()
        // 由于后端提供的都是restful接口，并没有直接跳转的页面
        // 所以只要访问的url没有通过权限认证，就跳到这个请求上，并直接排除权限异常
        .loginPage("/v1/rbac/loginFail")
        // 登录请求点
        .loginProcessingUrl(rbacProperties.getLoginUrl())
        // 一旦权限验证成功，则执行这个处理器
        .successHandler(authenticationSuccessHandler)
        // 一旦权限验证过程出现错误，则执行这个处理器
        .failureHandler(authenticationFailureHandler)
        // 自定义的登录表单信息（默认的登录表单结构只有两个字段，用户名和输入的密码）
        .authenticationDetailsSource(this.authenticationDetailsSource)
        // 由于使用前后端分离时，设定了failureHandler，所以failureForwardUrl就不需要设定了
        // 由于使用前后端分离时，设定了successHandler，所以successForwardUrl就不需要设定了
        .permitAll().and()
        // ===================== 设定登出后的url地址
        .logout()
        // 登出页面
        .logoutUrl(rbacProperties.getLogoutUrl())
        .logoutSuccessHandler(logoutSuccessHandler)
        .permitAll().and()
        // 由于使用前后端分离时，设定了logoutSuccessHandler，所以logoutSuccessUrl就不需要设定了
        // 登录成功后
        // ===================== 关闭csrf
        .csrf()
        .disable();
  }

  /**
   * 密码加密工具
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public PasswordEncoder getPasswordEncoder() {
    return new Pbkdf2PasswordEncoder();
  }

  /**
   * 权限决定的管理器
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public AccessDecisionManager getAccessDecisionManager() {
    return new CustomAccessDecisionManager();
  }

  /**
   * 用户信息的查询服务
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public UserDetailsService getUserDetailsService() {
    return new UserSecurityDetailsService();
  }

  /**
   * 处理每个接口访问时,判断其是否有权限访问
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public FilterInvocationSecurityMetadataSource getFilterInvocationSecurityMetadataSource() {
    return new CustomFilterInvocationSecurityMetadataSource();
  }

  /**
   * 自定义的登录信息服务
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  public AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> getAuthenticationDetailsSource() {
    return new SimpleAuthenticationDetailsSource();
  }

  /**
   * 自定义JWT识别
   * @return
   * @throws Exception
   */
/*  @Bean
  @ConditionalOnMissingBean
  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    return new JwtAuthenticationFilter(authenticationManager());
  }*/

  @Bean
  @ConditionalOnMissingBean
  public AuthenticationProvider getAuthenticationProvider() {
    return new SimpleAuthenticationProvider();
  }

  @Bean
  @ConditionalOnMissingBean
  public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
    return new SimpleAuthenticationSuccessHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public AuthenticationFailureHandler getAuthenticationFailureHandler() {
    return new SimpleAuthenticationFailureHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public LogoutSuccessHandler getLogoutSuccessHandler() {
    return new SimpleLogoutSuccessHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public AccessDeniedHandler getAccessDeniedHandler() {
    return new SimpleAccessDeniedHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return new SimpleAuthenticationEntryPoint();
  }

  /**
   * 配置权限的跨域操作
   *
   * @return
   */
  private CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    // 同源配置，*表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
    corsConfiguration.addAllowedOrigin("*");
    // header，允许哪些header，可将*替换为token；
    corsConfiguration.addAllowedHeader("*");
    // 允许的请求方法，PSOT、GET等
    corsConfiguration.addAllowedMethod("*");
    // 配置允许跨域访问的url
    source.registerCorsConfiguration("/**",corsConfiguration);
    return source;
  }

}
