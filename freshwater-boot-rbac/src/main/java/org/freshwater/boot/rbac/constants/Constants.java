package org.freshwater.boot.rbac.constants;

/**
 * 静态变量
 * @author tuxuchen
 * @date 2022/7/25 10:05
 */
public class Constants {

  private Constants() {
    throw new UnsupportedOperationException();
  }

  /**
   * 匿名用户
   */
  public static final String ANONYMOUS_USER = "anonymousUser";

  /**
   * 默认用户密码
   */
  public static final String USER_DEFAULT_PASSWORD = "1234WD";

  /**
   * 管理员用户名
   */
  public static final String USER_ACCOUNT = "admin";

  /**
   * admin角色编码
   */
  public static final String ROLE_ADMIN_CODE = "ADMIN";

}
