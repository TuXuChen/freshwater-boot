package org.freshwater.boot.routing.service.database.internal;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.freshwater.boot.common.utils.ResourceUtils;

/**
 * 原生数据库服务实现
 * @author tuxuchen
 * @date 2022/8/24 15:56
 */
@Slf4j
@Service
public class NativeDatabaseServiceImpl extends AbstractDatabaseServiceImpl {

  @Override
  public String getType() {
    return "native";
  }

  @Override
  public boolean dbExist(String dbName) {
    if(StringUtils.isBlank(dbName)) {
      return false;
    }
    String sql = "SELECT count(*) FROM information_schema.SCHEMATA where SCHEMA_NAME = ?";
    Long count = jdbcTemplate.queryForObject(sql, Long.class, dbName);
    this.switchDefaultDatabase();
    return count != null && count > 0;
  }

  /**
   * 创建数据库
   * @param dbName
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createDatabase(String dbName, String characterSet, String collateCharset) {
    /**
     * 创建数据库schema，需要保证当前应用连接数据的账号有创建schema、用户、授权的权限
     * 否则执行sql可能会没有权限
     * */
    Validate.notBlank(dbName, "数据库名不能为空");
    Validate.notBlank(characterSet, "数据库编码不能为空");
    String sql = ResourceUtils.read("sql/create_database.sql");
    sql = sql.replace("{dbName}", dbName);
    sql = sql.replace("{charset}", characterSet);
    sql = sql.replace("{collate_charset}", collateCharset);
    this.execute(sql);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createDbUser(String username, String password) {
    Validate.notBlank(username, "用户名不能为空");
    Validate.notBlank(password, "密码不能为空");
    Validate.isTrue(password.length() >= 8, "密码不能小于8位");
    String sql = ResourceUtils.read("sql/create_user.sql");
    sql = sql.replace("{username}", username);
    sql = sql.replace("{password}", password);
    this.execute(sql);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void allowDbUserPrivilege(String dbName, String username) {
    Validate.notBlank(dbName, "数据库名不能为空！");
    Validate.notEmpty(username, "用户信息不能为空！");
    String sql = ResourceUtils.read("sql/allow_db_user_privilege.sql");
    sql = sql.replace("{dbName}", dbName);
    sql = sql.replace("{username}", username);
    this.execute(sql);
  }
}
