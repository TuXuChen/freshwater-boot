package org.freshwater.boot.routing.datasource;

import org.apache.commons.lang3.Validate;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.freshwater.boot.routing.constant.RoutingContextHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 自定义数据源实现,实现多数据源
 * @author tuxuchen
 * @date 2022/8/23 17:16
 */
public class DynamicRoutingDataSource extends AbstractDataSource {

  @Override
  public Connection getConnection() throws SQLException {
    String routingCode = RoutingContextHolder.getRoutingCode();
    DataSource datasource = DynamicDataSourceProvider.getDatasource(routingCode);
    Validate.notNull(datasource, "未找到数据源:%s", routingCode);
    return datasource.getConnection();
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    String routingCode = RoutingContextHolder.getRoutingCode();
    DataSource datasource = DynamicDataSourceProvider.getDatasource(routingCode);
    Validate.notNull(datasource, "未找到数据源:%s", routingCode);
    return datasource.getConnection();
  }

}
