package org.freshwater.boot.template.service.internal;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.freshwater.boot.common.constants.enums.NormalStatusEnum;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.service.RedisService;
import org.freshwater.boot.common.utils.DateUtils;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.generator.CodeGenerator;
import org.freshwater.boot.generator.GeneratorPojo;
import org.freshwater.boot.rbac.entity.MenuEntity;
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.service.MenuService;
import org.freshwater.boot.rbac.utils.EntityUtils;
import org.freshwater.boot.rbac.utils.SecurityUtils;
import org.freshwater.boot.template.constant.RedisKeys;
import org.freshwater.boot.template.constant.enums.TableFieldType;
import org.freshwater.boot.template.dto.DynamicTableCreateDTO;
import org.freshwater.boot.template.dto.DynamicTableFieldQueryDTO;
import org.freshwater.boot.template.dto.DynamicTableQueryDTO;
import org.freshwater.boot.template.dto.DynamicTableUpdateDTO;
import org.freshwater.boot.template.dto.ExecuteModel;
import org.freshwater.boot.template.entity.DynamicTableEntity;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;
import org.freshwater.boot.template.mapper.DynamicTableMapper;
import org.freshwater.boot.template.service.DynamicTableFieldService;
import org.freshwater.boot.template.service.DynamicTableService;
import org.freshwater.boot.template.service.jdbc.JdbcService;
import org.freshwater.boot.template.vo.DynamicTableDetailsVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.freshwater.boot.common.constants.Constants.DEFAULT_PAGEABLE;
import static org.freshwater.boot.template.constant.Constants.TABLE_CODE_PREFIX;

/**
 * <p>
 * ????????? ???????????????
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-10 16:15
 */
@Slf4j
@Service
public class DynamicTableServiceImpl extends ServiceImpl<DynamicTableMapper, DynamicTableEntity> implements DynamicTableService {

  @Autowired
  private JdbcService jdbcService;
  @Autowired
  private DataSourceProperties dataSourceProperties;
  @Autowired
  private RedisService redisService;
  @Autowired
  private MenuService menuService;
  @Autowired
  private DynamicTableFieldService dynamicTableFieldService;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public DynamicTableEntity create(DynamicTableCreateDTO table) {
    ValidationUtils.validate(table, CreateValidate.class);
    long countByTableName = baseMapper.selectCountByTableName(table.getTableName());
    Validate.isTrue(countByTableName == 0, "????????????");
    String tableCode = redisService.getAndIncrement(RedisKeys.DYNAMIC_TABLE_CODE_INDEX, 1, 4);
    tableCode = StringUtils.join(TABLE_CODE_PREFIX, tableCode);
    table.setCode(tableCode);
    DynamicTableEntity dynamicTable = new DynamicTableEntity();
    BeanUtils.copyProperties(table, dynamicTable);
    dynamicTable.setCode(tableCode);
    dynamicTable.setState(NormalStatusEnum.ENABLE.getStatus());
    EntityUtils.initCreatorInfo(dynamicTable);
    baseMapper.insert(dynamicTable);
    dynamicTableFieldService.save(dynamicTable, table.getTableFields());
    this.tableCodeGenerator(table);
    this.createMenuByTable(table);
    return dynamicTable;
  }

  @Override
  @Transactional
  public DynamicTableEntity update(DynamicTableUpdateDTO table) {
    ValidationUtils.validate(table, UpdateValidate.class);
    long countByNameAndIdNot = baseMapper.selectCountByTableNameAndIdNot(table.getTableName(), table.getId());
    Validate.isTrue(countByNameAndIdNot == 0, "???????????????");
    DynamicTableEntity dbDynamicTable = baseMapper.selectById(table.getId());
    Validate.notNull(dbDynamicTable, "???????????????????????????");
    EntityUtils.initModifierInfo(dbDynamicTable);
    dbDynamicTable.setCode(table.getCode());
    dbDynamicTable.setTableName(table.getTableName());
    dbDynamicTable.setName(table.getName());
    dbDynamicTable.setIcon(table.getIcon());
    dbDynamicTable.setState(table.getState());
    baseMapper.updateById(dbDynamicTable);
    if (!CollectionUtils.isEmpty(table.getTableFields())) {
      dynamicTableFieldService.save(dbDynamicTable, table.getTableFields());
    }
    return dbDynamicTable;
  }

  @Override
  public DynamicTableEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  @Override
  public DynamicTableDetailsVO findDetailsById(String id) {
    DynamicTableEntity table = this.findById(id);
    DynamicTableDetailsVO dynamicTableDetailsVO = new DynamicTableDetailsVO();
    if(table != null) {
      BeanUtils.copyProperties(table, dynamicTableDetailsVO);
      List<DynamicTableFieldEntity> fields = dynamicTableFieldService.findByTableId(table.getId());
      fields = fields.stream().filter(f -> f.getState().equals(NormalStatusEnum.ENABLE.getStatus())).collect(Collectors.toList());
      dynamicTableDetailsVO.setTableFields(fields);
    }
    return dynamicTableDetailsVO;
  }

  @Override
  public DynamicTableEntity findByCode(String code) {
    if(StringUtils.isBlank(code)) {
      return null;
    }
    return baseMapper.selectByCode(code);
  }

  @Override
  public DynamicTableDetailsVO findDetailsByCode(String code) {
    if(StringUtils.isBlank(code)) {
      return null;
    }
    DynamicTableDetailsVO dynamicTableDetailsVO = new DynamicTableDetailsVO();
    DynamicTableEntity table = baseMapper.selectByCode(code);
    if (table != null) {
      BeanUtils.copyProperties(table, dynamicTableDetailsVO);
      List<DynamicTableFieldEntity> fields = dynamicTableFieldService.findByTableId(table.getId());
      fields = fields.stream().filter(f -> NormalStatusEnum.ENABLE.getStatus().equals(f.getState())).collect(Collectors.toList());
      dynamicTableDetailsVO.setTableFields(fields);
    }
    return dynamicTableDetailsVO;
  }

  @Override
  public PageInfo<DynamicTableEntity> findByConditions(DynamicTableQueryDTO query, Pageable pageable) {
    pageable = ObjectUtils.defaultIfNull(pageable, DEFAULT_PAGEABLE);
    PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
    List<DynamicTableEntity> tables = baseMapper.selectByConditions(query);
    PageHelper.clearPage();
    return PageInfo.of(tables);
  }

  @Override
  public List<DynamicTableEntity> findAllByConditions(DynamicTableQueryDTO query) {
    return baseMapper.selectByConditions(query);
  }

  @Override
  public Map<String, Object> executeInsert(String code, Map<String, Object> data) {
    Validate.notBlank(code, "???????????????????????????");
    DynamicTableDetailsVO table = this.findDetailsByCode(code);
    Validate.notNull(table, "????????????:%s???????????????????????????", code);
    Validate.notNull(data, "?????????????????????????????????");
    UserEntity user = SecurityUtils.getCurrentUser();
    LocalDateTime now = LocalDateTime.now();
    data.put("id", UUID.randomUUID().toString().replace("-", ""));
    data.put("creator", user.getUserAccount());
    data.put("creator_name", user.getRealName());
    data.put("create_time", now);
    data.put("modifier", user.getUserAccount());
    data.put("modifier_name", user.getRealName());
    data.put("modify_time", now);
    this.dataValidation(table, data);
    return jdbcService.insert(table.getTableName(), data);
  }

  @Override
  public Map<String, Object> executeUpdate(String code, Map<String, Object> data) {
    Validate.notBlank(code, "???????????????????????????");
    DynamicTableDetailsVO table = this.findDetailsByCode(code);
    Validate.notNull(table, "????????????:%s???????????????????????????", code);
    Validate.notNull(data, "?????????????????????????????????");
    this.dataValidation(table, data);
    String id = (String) data.get("id");
    Map<String, Object> obj = jdbcService.queryMapById(table.getTableName(), id);
    Validate.notNull(obj, "?????????????????????");
    UserEntity user = SecurityUtils.getCurrentUser();
    data.remove("creator");
    data.remove("creator_name");
    data.remove("create_time");
    data.put("modifier", user.getUserAccount());
    data.put("modifier_name", user.getRealName());
    data.put("modify_time", LocalDateTime.now());
    jdbcService.updateById(table.getTableName(), id, data);
    return data;
  }

  @Override
  public Map<String, Object> executeQueryById(String code, String id) {
    if (StringUtils.isBlank(code) || StringUtils.isBlank(id)) {
      log.warn("???????????????????????????ID??????");
      return null;
    }
    DynamicTableDetailsVO table = this.findDetailsByCode(code);
    if (table == null) {
      log.warn("????????????????????????????????????:{}", code);
      return null;
    }
    return jdbcService.queryMapById(table.getTableName(), id);
  }

  @Override
  public List<Map<String, Object>> executeQuery(ExecuteModel executeModel) {
    if (executeModel == null || StringUtils.isBlank(executeModel.getCode())) {
      return Lists.newArrayList();
    }
    DynamicTableDetailsVO table = this.findDetailsByCode(executeModel.getCode());
    if (table == null || CollectionUtils.isEmpty(table.getTableFields())) {
      log.warn("????????????????????????????????????:{}", executeModel.getCode());
      return Lists.newArrayList();
    }
    String columns = table.getTableFields().stream().map(DynamicTableFieldEntity::getName).collect(Collectors.joining(","));
    String sql = String.format("select %s from %s ", columns, table.getTableName());
    Sort sort = Sort.by(Sort.Direction.DESC, "create_time");
    return jdbcService.queryForList(sql, executeModel.getInputParams(), null, sort);
  }

  @Override
  public PageInfo executeQueryPage(ExecuteModel executeModel) {
    if (executeModel == null || StringUtils.isBlank(executeModel.getCode())) {
      return PageInfo.of(Lists.newArrayList());
    }
    Pageable pageable = ObjectUtils.defaultIfNull(executeModel.getPageable(), DEFAULT_PAGEABLE);
    DynamicTableDetailsVO table = this.findDetailsByCode(executeModel.getCode());
    if (table == null || CollectionUtils.isEmpty(table.getTableFields())) {
      log.warn("????????????????????????????????????:{}", executeModel.getCode());
      return PageInfo.of(new Page<>(pageable.getPageNumber(), pageable.getPageSize(), true));
    }
    this.handleParams(executeModel, table.getTableFields());
    String columns = table.getTableFields().stream().map(DynamicTableFieldEntity::getName).collect(Collectors.joining(","));
    String sql = String.format("select %s from %s ", columns, table.getTableName());
    String countSql = String.format("select %s from %s ", "count(*)", table.getTableName());
    List<Map<String, Object>> countResult = jdbcService.queryForList(countSql, executeModel.getInputParams(), null, null);
    Long count = (Long) countResult.get(0).get("count(*)");
    if(count == null || count == 0) {
      return PageInfo.of(new Page<>(pageable.getPageNumber(), pageable.getPageSize(), true));
    }
    Sort sort = Sort.by(Sort.Direction.DESC, "create_time");
    List<Map<String, Object>> results = jdbcService.queryForList(sql, executeModel.getInputParams(), pageable, sort);
    Page<Object> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize(), true);
    if(!CollectionUtils.isEmpty(results)) {
      page.setTotal(count);
      for (Map<String, Object> result : results) {
        page.add(result);
      }
    }
    return PageInfo.of(page);
  }

  @Override
  public void executeExportQuery(ExecuteModel executeModel) {
    boolean parameter = executeModel != null || StringUtils.isNotBlank(executeModel.getCode());
    Validate.isTrue(parameter, "?????????????????????????????????!");
    DynamicTableEntity tableInfo = this.findByCode(executeModel.getCode());
    Validate.notNull(tableInfo, "????????????????????????????????????:%s", executeModel.getCode());
    DynamicTableFieldQueryDTO tableFieldQuery = new DynamicTableFieldQueryDTO();
    tableFieldQuery.setTableId(tableInfo.getId());
    tableFieldQuery.setState(NormalStatusEnum.ENABLE.getStatus());
    tableFieldQuery.setIsListView(true);
    List<DynamicTableFieldEntity> tableFields = dynamicTableFieldService.findAllByConditions(tableFieldQuery);
    Validate.notEmpty(tableFields, "????????????????????????");
    List<String> fieldKeys = tableFields.stream().map(DynamicTableFieldEntity::getName).collect(Collectors.toList());
    String columns = tableFields.stream().map(DynamicTableFieldEntity::getName).collect(Collectors.joining(","));
    String sql = String.format("select %s from %s ", columns, tableInfo.getTableName());
    Sort sort = Sort.by(Sort.Direction.DESC, "create_time");
    List<Map<String, Object>> data = jdbcService.queryForList(sql, executeModel.getInputParams(), null, sort);
    List<List<Object>> resultExcel = Lists.newArrayList();
    //???????????????????????????
    for (Map<String, Object> map : data) {
      List<Object> result = Lists.newArrayList();
      for (String fieldKey : fieldKeys) {
        result.add(map.get(fieldKey));
      }
      resultExcel.add(result);
    }
    // ??????
    List<List<String>> headExcel = tableFields.stream().map(tableField -> {
      List<String> head = Lists.newArrayList();
      head.add(tableField.getDescription());
      return head;
    }).collect(Collectors.toList());
    try {
      HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setCharacterEncoding("utf-8");
      response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(tableInfo.getName(), "UTF-8") + ".xlsx");
      EasyExcel.write(response.getOutputStream()).head(headExcel).sheet(tableInfo.getName()).doWrite(resultExcel);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  /**
   * ????????????????????????
   * @param table
   */
  private void tableCodeGenerator(DynamicTableCreateDTO table) {
    if (!table.getIsProjectFile()) {
      return;
    }
    Validate.notBlank(table.getProjectSrcPath(), "java????????????????????????");
    Validate.notBlank(table.getRootPackage(), "???????????????????????????");
    GeneratorPojo generatorPojo = new GeneratorPojo();
    generatorPojo.setDatasourceUrl(dataSourceProperties.getUrl())
        .setUsername(dataSourceProperties.getUsername())
        .setPassword(dataSourceProperties.getPassword())
        .setAuthor(StringUtils.defaultIfBlank(table.getAuthor(), ""))
        .setTablePrefixs(StringUtils.defaultIfBlank(table.getTablePrefixs(), null))
        .setDriver(dataSourceProperties.getDriverClassName())
        .setProjectSrcPath(table.getProjectSrcPath())
        .setRootPackage(table.getRootPackage())
        .setIncludeTables(table.getTableName());
    CodeGenerator generator = new CodeGenerator(generatorPojo);
    generator.generate();
  }

  /**
   * ????????????????????????
   * @param table
   */
  private void createMenuByTable(DynamicTableCreateDTO table) {
    if (!table.getIsMenuRoute()) {
      return;
    }
    MenuEntity menu = new MenuEntity();
    menu.setUrl(table.getMenuUrl());
    menu.setType(0);
    menu.setName(table.getName());
    menu.setCode(table.getCode());
    menu.setIcon(table.getIcon());
    menu.setSortIndex(0);
    menu.setState(NormalStatusEnum.ENABLE.getStatus());
    menuService.create(menu);
  }

  /**
   * ??????SQl???????????????
   * @param table
   * @param data
   */
  private void dataValidation(DynamicTableDetailsVO table, Map<String, Object> data) {
    Validate.notNull(data, "??????????????????");
    List<DynamicTableFieldEntity> fields = table.getTableFields();
    Set<String> columnSet = Sets.newHashSet();
    for (DynamicTableFieldEntity field : fields) {
      columnSet.add(field.getName());
      if(!field.getNullable()) {
        Validate.notNull(data.get(field.getName()), "%s????????????", field.getDescription());
      }
    }
    data.forEach((k, v) -> Validate.isTrue(columnSet.contains(k), "???????????????:%s", k));
  }

  /**
   * ????????????
   * @param executeModel
   */
  private void handleParams(ExecuteModel executeModel, List<DynamicTableFieldEntity> fields) {
    Map<String, Object> inputParams = ObjectUtils.defaultIfNull(executeModel.getInputParams(), Maps.newHashMap());
    Map<String, DynamicTableFieldEntity> fieldsMap = fields.stream().collect(Collectors.toMap(DynamicTableFieldEntity::getName, v -> v));
    Iterator<Map.Entry<String, Object>> iterator = inputParams.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, Object> entry = iterator.next();
      String key = entry.getKey();
      Object value = entry.getValue();
      DynamicTableFieldEntity field = fieldsMap.get(key);
      if(field == null) {
        continue;
      }
      if(this.iterableable(value) && field.getType().equals(TableFieldType.DATETIME.getType())) {
        // ???????????????
        List<Object> values = this.obj2List(value);
        Validate.isTrue(values.size() == 2, "?????????????????????????????????");
        LocalDate startDate = LocalDate.parse(values.get(0).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(values.get(1).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime startTime = DateUtils.getStartTime(startDate, DateUtils.RangeType.DAY);
        LocalDateTime endTime = DateUtils.getEndTime(endDate, DateUtils.RangeType.DAY);
        inputParams.put(key, Lists.newArrayList(startTime, endTime));
      }
    }
  }

  /**
   * ???????????????
   * @param obj
   * @return
   */
  private List<Object> obj2List(Object obj) {
    List<Object> list = Lists.newArrayList();
    if (obj instanceof Collection) {
      Collection collection = (Collection) obj;
      list.addAll(collection);
    } else if(obj.getClass().isArray()) {
      Object[] objs = (Object[]) obj;
      list.addAll(Lists.newArrayList(objs));
    }
    return list;
  }

  /**
   * ?????????????????????
   * @param obj
   * @return
   */
  private boolean iterableable(Object obj) {
    if(obj == null) {
      return false;
    }
    if (obj instanceof Collection) {
      return true;
    }
    if(obj.getClass().isArray()) {
      return true;
    }
    return false;
  }

}
