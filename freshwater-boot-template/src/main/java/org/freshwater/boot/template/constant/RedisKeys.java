package org.freshwater.boot.template.constant;

/**
 * redis 键值信息
 * @author tuxuchen
 * @date 2022/8/16 10:36
 */
public class RedisKeys {

  private RedisKeys() {
    throw new UnsupportedOperationException();
  }

  /**
   * 动态表编码
   */
  public static final String DYNAMIC_TABLE_CODE_INDEX = "dynamic:table:code:index";
}
