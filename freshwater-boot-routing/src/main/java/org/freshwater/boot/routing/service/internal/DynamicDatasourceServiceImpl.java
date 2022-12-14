package org.freshwater.boot.routing.service.internal;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.service.RedisService;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.routing.constant.RedisKeys;
import org.freshwater.boot.routing.datasource.DatasourceBuilder;
import org.freshwater.boot.routing.datasource.DynamicDataSourceProvider;
import org.freshwater.boot.routing.dto.DynamicDatasourceDTO;
import org.freshwater.boot.routing.entity.DynamicDatasourceEntity;
import org.freshwater.boot.routing.mapper.DynamicDatasourceMapper;
import org.freshwater.boot.routing.service.DynamicDatasourceService;
import org.freshwater.boot.routing.service.database.DatabaseService;

import java.time.LocalDateTime;
import java.util.List;

import static org.freshwater.boot.common.constants.Constants.DEFAULT_PAGEABLE;
import static org.freshwater.boot.routing.constant.Constants.DB_NAME_PREFIX;
import static org.freshwater.boot.routing.constant.Constants.DEFAULT_DATABASE_CHARSET;
import static org.freshwater.boot.routing.constant.Constants.DEFAULT_DATABASE_COLLATE_CHARSET;
import static org.freshwater.boot.routing.constant.Constants.DEFAULT_DATASOURCE_CODE;
import static org.freshwater.boot.routing.constant.Constants.MYSQL_URL_TEMPLATE;
import static org.freshwater.boot.routing.constant.Constants.ROUTING_PREFIX;
import static org.freshwater.boot.routing.constant.RedisKeys.DATASOURCE_ROUTING_CODE;

/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-23 16:47
 */
@Service
public class DynamicDatasourceServiceImpl extends ServiceImpl<DynamicDatasourceMapper, DynamicDatasourceEntity> implements DynamicDatasourceService {

  @Autowired
  private RedisService redisService;
  @Autowired
  private DatabaseService databaseService;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public DynamicDatasourceEntity create(DynamicDatasourceDTO dynamicDatasource) {
    ValidationUtils.validate(dynamicDatasource, CreateValidate.class);
    DynamicDatasourceEntity datasource = new DynamicDatasourceEntity();
    BeanUtils.copyProperties(dynamicDatasource, datasource);
    String code = redisService.getAndIncrement(RedisKeys.DYNAMIC_DATASOURCE_CODE_INDEX, 1, 4);
    code = StringUtils.join(ROUTING_PREFIX, code);
    String dbName = StringUtils.join(DB_NAME_PREFIX, code);
    String url = String.format(MYSQL_URL_TEMPLATE, dynamicDatasource.getHost(), dynamicDatasource.getPort(), dbName);
    datasource.setUrl(url);
    datasource.setCode(code);
    datasource.setEnabled(true);
    datasource.setCreateTime(LocalDateTime.now());
    datasource.setModifyTime(LocalDateTime.now());
    DynamicDataSourceProvider.addDatasource(code, this.builderDatasource(datasource));
    boolean isExist = databaseService.dbExist(dbName);
    if (!isExist) {
      databaseService.createDatabase(dbName, DEFAULT_DATABASE_CHARSET, DEFAULT_DATABASE_COLLATE_CHARSET);
      databaseService.createTables(dbName);
    }
    baseMapper.insert(datasource);
    return datasource;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public DynamicDatasourceEntity update(DynamicDatasourceDTO dynamicDatasource) {
    ValidationUtils.validate(dynamicDatasource, UpdateValidate.class);
    DynamicDatasourceEntity dbDynamicDatasource = baseMapper.selectById(dynamicDatasource.getId());
    Validate.notNull(dbDynamicDatasource, "?????????????????????????????????");
    String routingCode = (String) redisService.getValue(DATASOURCE_ROUTING_CODE);
    Validate.isTrue(!StringUtils.equals(routingCode, dbDynamicDatasource.getCode()), "??????????????????????????????????????????");
    String dbName = StringUtils.join(DB_NAME_PREFIX, dbDynamicDatasource.getCode());
    String url = String.format(MYSQL_URL_TEMPLATE, dynamicDatasource.getHost(), dynamicDatasource.getPort(), dbName);
    dbDynamicDatasource.setUrl(url);
    dbDynamicDatasource.setUsername(dynamicDatasource.getUsername());
    dbDynamicDatasource.setPassword(dynamicDatasource.getPassword());
    dbDynamicDatasource.setInitialSize(dynamicDatasource.getInitialSize());
    dbDynamicDatasource.setMaxActive(dynamicDatasource.getMaxActive());
    dbDynamicDatasource.setMinIdle(dynamicDatasource.getMinIdle());
    dbDynamicDatasource.setMaxWait(dynamicDatasource.getMaxWait());
    dbDynamicDatasource.setEnabled(dynamicDatasource.getEnabled());
    dbDynamicDatasource.setDriverClassName(dynamicDatasource.getDriverClassName());
    dbDynamicDatasource.setModifyTime(LocalDateTime.now());
    baseMapper.updateById(dbDynamicDatasource);
    DynamicDataSourceProvider.remove(dbDynamicDatasource.getCode());
    DynamicDataSourceProvider.addDatasource(dbDynamicDatasource.getCode(), this.builderDatasource(dbDynamicDatasource));
    return dbDynamicDatasource;
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    Validate.notBlank(id, "??????ID????????????");
    DynamicDatasourceEntity dbDynamicDatasource = baseMapper.selectById(id);
    Validate.notNull(dbDynamicDatasource, "?????????????????????????????????");
    String routingCode = (String) redisService.getValue(DATASOURCE_ROUTING_CODE);
    Validate.isTrue(!StringUtils.equals(routingCode, dbDynamicDatasource.getCode()), "??????????????????????????????????????????");
    DynamicDataSourceProvider.remove(dbDynamicDatasource.getCode());
    baseMapper.deleteById(id);
  }

  @Override
  public DynamicDatasourceEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  @Override
  public PageInfo<DynamicDatasourceEntity> findPage(Pageable pageable) {
    pageable = ObjectUtils.defaultIfNull(pageable, DEFAULT_PAGEABLE);
    PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
    List<DynamicDatasourceEntity> datasource = baseMapper.selectList(new QueryWrapper<>());
    PageHelper.clearPage();
    return PageInfo.of(datasource);
  }

  @Override
  public String enableByCode(String code) {
    if (StringUtils.isBlank(code)) {
      code = DEFAULT_DATASOURCE_CODE;
      redisService.setValue(DATASOURCE_ROUTING_CODE, code);
      return code;
    }
    DynamicDatasourceEntity datasource = baseMapper.selectByCode(code);
    Validate.notNull(datasource, "????????????????????????!");
    Validate.isTrue(datasource.getEnabled(), "???????????????????????????!");
    redisService.setValue(DATASOURCE_ROUTING_CODE, code);
    return code;
  }

  /**
   * ???????????????
   * @param dynamicDatasource
   * @return
   */
  private DruidDataSource builderDatasource(DynamicDatasourceEntity dynamicDatasource) {
    DatasourceBuilder instance = DatasourceBuilder.getInstance();
    return instance.build(dynamicDatasource);
  }
}
