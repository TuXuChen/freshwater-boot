package org.freshwater.boot.routing.constant;

/**
 * 静态变量类
 * @author tuxuchen
 * @date 2022/8/23 17:45
 */
public class Constants {

  private Constants() {
    throw new UnsupportedOperationException();
  }

  /**
   * 默认数据源名称
   */
  public static final String DEFAULT_DATASOURCE_CODE = "default_datasource";

  /**
   * 动态数据源编码前缀
   */
  public static final String ROUTING_PREFIX = "routing_";

  /**
   * 数据库名前缀
   */
  public static final String DB_NAME_PREFIX = "freshwater_";

  /**
   * 默认数据库编码
   */
  public static final String DEFAULT_DATABASE_CHARSET = "utf8mb4";

  /**
   * 默认数据库排序规则
   */
  public static final String DEFAULT_DATABASE_COLLATE_CHARSET = "utf8mb4_general_ci";

  /**
   * mysql连接地址模板
   */
  public static final String MYSQL_URL_TEMPLATE = "jdbc:mysql://%s:%s/%s?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true";

}
