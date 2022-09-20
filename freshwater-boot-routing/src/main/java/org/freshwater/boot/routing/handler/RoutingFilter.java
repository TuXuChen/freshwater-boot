package org.freshwater.boot.routing.handler;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.freshwater.boot.common.service.RedisService;
import org.freshwater.boot.routing.constant.RoutingContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static org.freshwater.boot.routing.constant.Constants.DEFAULT_DATASOURCE_CODE;
import static org.freshwater.boot.routing.constant.RedisKeys.DATASOURCE_ROUTING_CODE;

/**
 * 路由拦截器
 * 拦截所有路由 设置动态数据源
 * @author tuxuchen
 * @date 2022/8/23 17:47
 */
@Component
@Order(-2147483597)
public class RoutingFilter extends OncePerRequestFilter {

  @Autowired
  private RedisService redisService;

  /**
   * 设置使用default数据库的url
   */
  private final Set<String> allDefaultUrls = Sets.newHashSet("/v1/dynamicDatasources/**");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String routingCode = null;
      for (String url : allDefaultUrls) {
        AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(url);
        if (requestMatcher.matches(request)) {
          routingCode = DEFAULT_DATASOURCE_CODE;
          break;
        }
      }
      routingCode = StringUtils.defaultIfBlank(routingCode, (String) redisService.getValue(DATASOURCE_ROUTING_CODE));
      RoutingContextHolder.setRoutingCode(routingCode);
      filterChain.doFilter(request, response);
    } finally {
      // 最后将当前线程的上下文信息清除掉
      RoutingContextHolder.clear();
    }
  }

}
