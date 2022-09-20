package org.freshwater.boot.rbac.configuration;

import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * rbac配置信息类
 * @author tuxuchen
 * @date 2022/7/25 11:50
 */
@Data
@Component
@EnableConfigurationProperties({RbacProperties.class})
@ConfigurationProperties(prefix = "rbac")
public class RbacProperties {

  /**
   * 默认的忽略的url
   */
  private Set<String> allIgnoreUrls = Sets.newHashSet("/default/**", "/static/**", "/doc.html/**", "/favicon.ico", "/v2/api-docs",
      "/swagger-ui/**", "/webjars/**", "/swagger-resources/**", "/v3/api-docs", "/v1/users/register", "/v1/files/image/view/**");

  /**
   * 登录后忽略的url
   */
  private Set<String> allIgnoreUrlsForLogged = Sets.newHashSet();

  /**
   * 忽略的访问地址
   */
  private String[] ignoreUrls = new String[0];

  /**
   * 登录后忽略的访问地址
   */
  private String[] ignoreUrlsForLogged = new String[0];

  /**
   * 登录地址
   */
  private String loginUrl = "/v1/rbac/login";

  /**
   * 登出地址
   */
  private String logoutUrl = "/v1/rbac/logout";

  /**
   * 鉴权忽略的角色
   */
  private String[] ignoreMethodCheckRoles = new String[]{"ADMIN"};

  @PostConstruct
  public void init() {
    allIgnoreUrls.addAll(Sets.newHashSet(ignoreUrls));
    allIgnoreUrls.add(logoutUrl);
    allIgnoreUrlsForLogged.addAll(Sets.newHashSet(ignoreUrlsForLogged));
  }

}
