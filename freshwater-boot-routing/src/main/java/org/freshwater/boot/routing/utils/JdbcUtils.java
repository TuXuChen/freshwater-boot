package org.freshwater.boot.routing.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * 数据库jdbc工具库
 * @author tuxuchen
 * @date 2022/8/24 15:52
 */
public class JdbcUtils {

  /**
   * 根据url接续数据库名
   * @param jdbcUrl
   * @return
   */
  public static String getDbNameByUrl(String jdbcUrl) {
    String database = null;
    int pos, pos1;
    String connUri;
    Validate.notBlank(jdbcUrl, "Invalid JDBC url.");
    jdbcUrl = jdbcUrl.toLowerCase();
    if (jdbcUrl.startsWith("jdbc:impala")) {
      jdbcUrl = jdbcUrl.replace(":impala", "");
    }
    if (!jdbcUrl.startsWith("jdbc:")
        || (pos1 = jdbcUrl.indexOf(':', 5)) == -1) {
      throw new IllegalArgumentException("Invalid JDBC url.");
    }
    connUri = jdbcUrl.substring(pos1 + 1);
    if (connUri.startsWith("//")) {
      if ((pos = connUri.indexOf('/', 2)) != -1) {
        database = connUri.substring(pos + 1);
      }
    } else {
      database = connUri;
    }
    if (database.contains("?")) {
      database = database.substring(0, database.indexOf("?"));
    }
    if (database.contains(";")) {
      database = database.substring(0, database.indexOf(";"));
    }
    if (StringUtils.isBlank(database)) {
      throw new IllegalArgumentException("Invalid JDBC url.");
    }
    return database;
  }

}
