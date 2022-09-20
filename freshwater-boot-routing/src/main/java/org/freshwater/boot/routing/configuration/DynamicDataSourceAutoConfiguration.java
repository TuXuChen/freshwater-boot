package org.freshwater.boot.routing.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.freshwater.boot.routing.datasource.DatasourceBuilder;
import org.freshwater.boot.routing.datasource.DynamicDataSourceProvider;
import org.freshwater.boot.routing.datasource.DynamicRoutingDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.freshwater.boot.routing.constant.Constants.DEFAULT_DATASOURCE_CODE;

/**
 *  动态数据源配置
 * @author tuxuchen
 * @date 2022/8/23 17:21
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class, DruidProperties.class})
public class DynamicDataSourceAutoConfiguration {

  @Autowired
  private DruidProperties druidProperties;
  @Autowired
  private DataSourceProperties dataSourceProperties;

  @PostConstruct
  public void init() {
    DatasourceBuilder builder = DatasourceBuilder.getInstance();
    DruidDataSource defaultDatasource = builder.build(druidProperties, dataSourceProperties);
    DynamicDataSourceProvider.addDatasource(DEFAULT_DATASOURCE_CODE, defaultDatasource);
    try {
      List<Map<String, Object>> results = JdbcUtils.executeQuery(defaultDatasource, "select * from freshwater_dynamic_datasource where enabled = 1");
      if (CollectionUtils.isEmpty(results)) {
        return;
      }
      for (Map<String, Object> result : results) {
        DataSource dataSource = builder.build(result);
        DynamicDataSourceProvider.addDatasource((String) result.get("code"), dataSource);
      }
    } catch (SQLException e) {
      log.error(e.getMessage());
    }
  }

  @Bean
  public DynamicRoutingDataSource getDynamicRoutingDataSource() {
    return new DynamicRoutingDataSource();
  }

}
