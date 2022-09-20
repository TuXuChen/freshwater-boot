package org.freshwater.boot.routing.service.database.internal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.freshwater.boot.common.utils.ResourceUtils;
import org.freshwater.boot.routing.service.database.DatabaseService;
import org.freshwater.boot.routing.utils.JdbcUtils;

/**
 * 数据库服务实现, 这是一个抽象类，不同的数据库需要自己去实现
 * @author tuxuchen
 * @date 2022/8/24 15:49
 */
public abstract class AbstractDatabaseServiceImpl implements DatabaseService {

  @Autowired
  protected JdbcTemplate jdbcTemplate;
  @Autowired
  protected DataSourceProperties dataSourceProperties;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createTables(String dbName) {
    Validate.notBlank(dbName, "数据库名不能为空");
    String sql = ResourceUtils.read("sql/create_table.sql");
    sql = String.format("use %s;", dbName) + sql;
    this.execute(sql);
    this.switchDefaultDatabase();
  }

  /**
   * 切换回默认数据库
   */
  public void switchDefaultDatabase() {
    String dbName = JdbcUtils.getDbNameByUrl(dataSourceProperties.getUrl());
    String sql = String.format("use %s;", dbName);
    this.execute(sql);
  }

  /**
   * 修改数据库的排序 编码
   * @param dbName
   * @param collateCharset
   */
  protected void alterCollateCharset(String dbName,  String collateCharset) {
    Validate.notBlank(dbName, "数据库名不能为空");
    Validate.notBlank(collateCharset, "数据库排序编码不能为空");
    String sql = "ALTER DATABASE `%s` COLLATE '%s'";
    sql = String.format(sql, dbName, collateCharset);
    this.execute(sql);
  }

  /**
   * 执行sql
   * @param sql
   */
  protected void execute(String sql) {
    String[] sqls = sql.split(";");
    for (String s : sqls) {
      if(StringUtils.isNotBlank(s)) {
        jdbcTemplate.execute(s);
      }
    }
  }
}
