package org.freshwater.boot.routing.constant;

/**
 * reids键定义
 * @author tuxuchen
 * @date 2022/8/23 16:56
 */
public class RedisKeys {

  private RedisKeys() {
    throw new UnsupportedOperationException();
  }

  /**
   * 数据源路由编码
   */
  public static final String DATASOURCE_ROUTING_CODE = "datasource:routing:code";

  /**
   * 动态数据源编码生成索引
   */
  public static final String DYNAMIC_DATASOURCE_CODE_INDEX = "dynamic:datasource:code:index";

}
