package org.freshwater.boot.routing.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.freshwater.boot.routing.configuration.DruidProperties;
import org.freshwater.boot.routing.entity.DynamicDatasourceEntity;

import java.util.Map;

import static org.freshwater.boot.routing.constant.Constants.DEFAULT_DATASOURCE_CODE;

/**
 * 数据源工厂类
 * @author tuxuchen
 * @date 2022/8/23 17:09
 */
public class DatasourceBuilder {

  private static volatile DatasourceBuilder instance;

  private DatasourceBuilder() {

  }

  /**
   * 构建数据源
   * @param datasource
   * @return
   */
  public DruidDataSource build(DynamicDatasourceEntity datasource) {
    Validate.notNull(datasource, "数据源配置不能为空");
    DruidDataSource druidDataSource = new DruidDataSource();
    druidDataSource.setName(DEFAULT_DATASOURCE_CODE);
    druidDataSource.setUrl(datasource.getUrl());
    druidDataSource.setUsername(datasource.getUsername());
    druidDataSource.setPassword(datasource.getPassword());
    druidDataSource.setDriverClassName(datasource.getDriverClassName());
    druidDataSource.setInitialSize(datasource.getInitialSize());
    druidDataSource.setMaxActive(datasource.getMaxActive());
    druidDataSource.setMinIdle(datasource.getMinIdle());
    druidDataSource.setMaxWait(datasource.getMaxWait());
    return druidDataSource;
  }

  /**
   * 构建数据源
   * @param druidProperties
   * @param dataSourceProperties
   * @return
   */
  public DruidDataSource build(DruidProperties druidProperties, DataSourceProperties dataSourceProperties) {
    Validate.notNull(druidProperties, "druid配置信息不能为空");
    Validate.notNull(dataSourceProperties, "数据源配置不能为空");
    DynamicDatasourceEntity datasource = new DynamicDatasourceEntity();
    BeanUtils.copyProperties(dataSourceProperties, datasource);
    datasource.setInitialSize(druidProperties.getInitialSize());
    datasource.setMaxActive(druidProperties.getMaxActive());
    datasource.setMinIdle(druidProperties.getMinIdle());
    Long maxWait = druidProperties.getMaxWait();
    datasource.setMaxWait(maxWait.intValue());
    return this.build(datasource);
  }

  /**
   * 构建数据源
   * @param result
   * @return
   */
  public DruidDataSource build(Map<String, Object> result) {
    Validate.notNull(result, "数据源配置不能为空");
    DruidDataSource dataSource = new DruidDataSource();
    dataSource.setName((String) result.get("code"));
    dataSource.setUrl((String) result.get("url"));
    dataSource.setPassword((String) result.get("password"));
    dataSource.setUsername((String) result.get("username"));
    dataSource.setDriverClassName((String) result.get("driver_class_name"));
    dataSource.setInitialSize((Integer) result.get("initial_size"));
    dataSource.setMaxActive((Integer) result.get("max_active"));
    dataSource.setMinIdle((Integer) result.get("min_idle"));
    dataSource.setMaxWait((Integer) result.get("max_wait"));
    return dataSource;
  }

  public static DatasourceBuilder getInstance() {
    if (instance == null) {
      synchronized (DatasourceBuilder.class) {
        if (instance == null) {
          instance = new DatasourceBuilder();
        }
      }
    }
    return instance;
  }

}
