package org.freshwater.boot.routing.service.database;

/**
 * 数据库服务
 * @author tuxuchen
 * @date 2022/8/24 15:42
 */
public interface DatabaseService {

  /**
   * 查询数据库是否存在
   * @param dbName
   * @return
   */
  boolean dbExist(String dbName);

  /**
   * 获取当前服务实现的数据库类型
   * @return
   */
  String getType();

  /**
   * 创建数据库
   * @param dbName 数据库名称。数据库名称长度可在1~64个字符之间,由字母、数字、中划线、下划线或$组成,$累计总长度小于等于10个字符,(MySQL 8.0不可包含$)。
   * @param characterSet 数据库使用的字符集,例如utf8、utf8mb4、gbk、ascii等MySQL支持的字符集
   * @param collateCharset 排序编码规则
   * @return
   */
  void createDatabase(String dbName, String characterSet, String collateCharset);

  /**
   * 创建数据库用户
   * @param username 数据库用户名称。
   *                 数据库帐号名称在1到32个字符之间,由小写字母、数字、中划线、或下划线组成,不能包含其他特殊字符。
   *                 若数据库版本为MySQL5.6和8.0,帐号长度为1~16个字符。
   *                 若数据库版本为MySQL5.7,帐号长度为1~32个字符。
   * @param password 数据库帐号密码。
   *
   *                 取值范围:
   *
   *                 非空,由大小写字母、数字和特殊符号~!@#%^*-_=+?组成,长度8~32个字符,不能和数据库帐号“name”或“name”的逆序相同。
   *
   *                 建议您输入高强度密码,以提高安全性,防止出现密码被暴力破解等安全风险。
   */
  void createDbUser(String username, String password);

  /**
   * 授权数据库帐号
   * @param dbName  数据库名称。
   * @param username  数据库名。
   */
  void allowDbUserPrivilege(String dbName, String username);

  /**
   * 创建数据库表
   * @param dbName
   */
  void createTables(String dbName);

}
