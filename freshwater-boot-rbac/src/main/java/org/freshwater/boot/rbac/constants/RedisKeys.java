package org.freshwater.boot.rbac.constants;

/**
 * redis 的键值信息
 * @author tuxuchen
 * @date 2022/7/25 15:36
 */
public class RedisKeys {

  private RedisKeys() {
    throw new UnsupportedOperationException();
  }

  /**
   * 用户可访问的接口地址前缀
   */
  public static final String USE_URLS_PREFIX = "user:urls:";

  /**
   * 用户可访问的接口地址
   * 第一个参数: 用户账号
   * 第二个参数: 按钮类型
   */
  public static final String USE_URLS = "user:urls:%s:%s";

  /**
   * 角色编码的索引
   */
  public static final String ROLE_CODE_INDEX = "role:code:index";
}
