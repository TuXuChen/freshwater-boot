package org.freshwater.boot.template.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.freshwater.boot.common.constants.enums.NormalStatusEnum;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.common.utils.WdCollectionUtils;
import org.freshwater.boot.template.constant.enums.ShowType;
import org.freshwater.boot.template.constant.enums.TableFieldType;
import org.freshwater.boot.template.dto.DynamicTableFieldQueryDTO;
import org.freshwater.boot.template.entity.DynamicTableEntity;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;
import org.freshwater.boot.template.mapper.DynamicTableFieldMapper;
import org.freshwater.boot.template.service.DynamicTableFieldService;
import org.freshwater.boot.template.service.jdbc.JdbcService;
import org.freshwater.boot.template.service.jdbc.Table;
import org.freshwater.boot.template.service.jdbc.TableField;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.freshwater.boot.template.constant.Constants.DEFAULT_FIELD_LENGTH;
import static org.freshwater.boot.template.constant.Constants.FIELD_ID;
import static org.freshwater.boot.template.constant.Constants.FIELD_OPERATOR;
import static org.freshwater.boot.template.constant.Constants.FIXATE_FIELD_NAME;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-10 16:22
 */
@Service
public class DynamicTableFieldServiceImpl extends ServiceImpl<DynamicTableFieldMapper, DynamicTableFieldEntity> implements DynamicTableFieldService {

  @Autowired
  private JdbcService jdbcService;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(DynamicTableEntity table, List<DynamicTableFieldEntity> fields) {
    this.saveValidation(table, fields);
    List<DynamicTableFieldEntity> dbFields = baseMapper.selectByTableId(table.getId());
    boolean isCreate = CollectionUtils.isEmpty(dbFields);
    this.handleDefaultFields(fields, isCreate);
    int index = 0;
    for (DynamicTableFieldEntity field : fields) {
      field.setSortIndex(index);
      index++;
    }
    Map<String, DynamicTableFieldEntity> fieldsMap = fields.stream().collect(Collectors.toMap(DynamicTableFieldEntity::getId, v -> v, (v1, v2) -> v1));
    List<DynamicTableFieldEntity> deletes = Lists.newArrayList();
    List<DynamicTableFieldEntity> updates = Lists.newArrayList();
    List<DynamicTableFieldEntity> creates = Lists.newArrayList();
    WdCollectionUtils.collectionDiscrepancy(fields, dbFields, DynamicTableFieldEntity::getId, deletes, updates, creates);
    // 需要更新数据库字段的集合
    List<DynamicTableFieldEntity> modifyFields = Lists.newArrayList();
    // 需要删除数据库字段的集合
    List<DynamicTableFieldEntity> deleteFields = Lists.newArrayList();
    for (DynamicTableFieldEntity delete : deletes) {
      if(!FIXATE_FIELD_NAME.contains(delete.getName())) {
        deleteFields.add(delete);
        baseMapper.deleteById(delete);
      }
    }
    for (DynamicTableFieldEntity update : updates) {
      DynamicTableFieldEntity field = fieldsMap.get(update.getId());
      update.setSortIndex(field.getSortIndex());
      if(!update.getDescription().equals(field.getDescription())) {
        modifyFields.add(update);
      }
      update.setDescription(field.getDescription());
      update.setShowType(field.getShowType());
      update.setIsListView(field.getIsListView());
      update.setIsSearch(field.getIsSearch());
      update.setState(field.getState());
      baseMapper.updateById(update);
    }
    for (DynamicTableFieldEntity create : creates) {
      if(!FIXATE_FIELD_NAME.contains(create.getName())) {
        create.setLength(this.getLength(create.getType(), create.getShowType()));
        create.setNullable(true);
        create.setIsPrimaryKey(false);
        create.setEditable(true);
        create.setIsUnique(false);
        create.setIsRenderInput(true);
        create.setState(NormalStatusEnum.ENABLE.getStatus());
      }
      create.setTableId(table.getId());
      baseMapper.insert(create);
      dbFields.add(create);
    }
    if(isCreate) {
      jdbcService.createTable(this.getJdbcTable(table, dbFields));
    } else {
      // 如果字段有更新
      if(!CollectionUtils.isEmpty(modifyFields) || !CollectionUtils.isEmpty(creates)) {
        Table jdbcTable = this.getJdbcTable(table, modifyFields, creates);
        jdbcService.modifyTable(jdbcTable);
      }
      // 如果字段有删除
      if (!CollectionUtils.isEmpty(deleteFields)) {
        jdbcService.deleteTable(this.getJdbcTable(table, deleteFields));
      }
    }
  }

  @Override
  public List<DynamicTableFieldEntity> findByTableId(String tableId) {
    if(StringUtils.isBlank(tableId)) {
      return Lists.newArrayList();
    }
    return baseMapper.selectByTableId(tableId);
  }

  @Override
  public List<DynamicTableFieldEntity> findAllByConditions(DynamicTableFieldQueryDTO query) {
    return baseMapper.selectByConditions(query);
  }

  /**
   * 保存前数据校验
   * @param table
   * @param fields
   */
  private void saveValidation(DynamicTableEntity table, List<DynamicTableFieldEntity> fields) {
    Validate.notNull(table, "表信息不能为空");
    Validate.notBlank(table.getId(), "表ID不能为空");
    ValidationUtils.validate(fields);
    for (DynamicTableFieldEntity field : fields) {
      TableFieldType tableFieldType = TableFieldType.valueOfType(field.getType());
      Validate.notNull(tableFieldType, "不支持的字段类型:%s", field.getType());
      ShowType showType = ShowType.valueOfType(field.getShowType());
      Validate.notNull(showType, "不支持的显示类型:%s", field.getShowType());
    }
  }

  /**
   * 创建默认字段
   * @param fields 动态表字段实体
   * @param isCreate 数据库是否存在旧数据
   */
  private void handleDefaultFields(List<DynamicTableFieldEntity> fields, boolean isCreate) {
    if (!isCreate) {
      return;
    }
    List<DynamicTableFieldEntity> preFields = Lists.newArrayList();
    preFields.add(this.copyField(FIELD_ID));
    fields.addAll(0, preFields);
    List<DynamicTableFieldEntity> operators = this.copyFields(FIELD_OPERATOR);
    fields.addAll(operators);
  }

  /**
   * 复制对象
   * @param fields
   * @return
   */
  private List<DynamicTableFieldEntity> copyFields(List<DynamicTableFieldEntity> fields) {
    List<DynamicTableFieldEntity> copyFields = Lists.newArrayList();
    for (DynamicTableFieldEntity field : fields) {
      DynamicTableFieldEntity copyField = new DynamicTableFieldEntity();
      BeanUtils.copyProperties(field, copyField);
      copyFields.add(copyField);
    }
    return copyFields;
  }

  /**
   * 复制对象
   * @param field
   * @return
   */
  private DynamicTableFieldEntity copyField(DynamicTableFieldEntity field) {
    DynamicTableFieldEntity copyField = new DynamicTableFieldEntity();
    BeanUtils.copyProperties(field, copyField);
    return copyField;
  }

  /**
   * 获取长度
   * @param type
   * @return
   */
  private int getLength(String type, Integer showType) {
    TableFieldType fieldType = TableFieldType.valueOfType(type);
    switch (fieldType) {
      case STRING:
        ShowType showTypeEnum = ShowType.valueOfType(showType);
        switch (showTypeEnum) {
          case IMAGE:
          case VIDEO:
            return 1000;
          default:
            return DEFAULT_FIELD_LENGTH;
        }
      case BOOLEAN:
        return 1;
      case INT:
        return 11;
      case LONG:
        return 20;
      case DECIMAL:
        return 65;
      case TEXT:
      case DATE:
      case DATETIME:
        return 0;
      default:
        throw new UnsupportedOperationException();
    }
  }

  /**
   * 获取jdbc的表结构
   * @param table
   * @param fields
   * @return
   */
  private Table getJdbcTable(DynamicTableEntity table, List<DynamicTableFieldEntity> fields) {
    return this.getJdbcTable(table, Lists.newArrayList(), fields);
  }

  /**
   * 获取table信息
   * @param table
   * @param modifyFields
   * @param createFields
   * @return
   */
  private Table getJdbcTable(DynamicTableEntity table, List<DynamicTableFieldEntity> modifyFields, List<DynamicTableFieldEntity> createFields) {
    Table jdbcTable = new Table();
    jdbcTable.setName(table.getTableName());
    jdbcTable.setDescription(table.getName());
    List<TableField> jdbcFields = Lists.newArrayList();
    for (DynamicTableFieldEntity field : modifyFields) {
      TableField jdbcField = this.getTableField(field, true);
      jdbcFields.add(jdbcField);
    }
    for (DynamicTableFieldEntity field : createFields) {
      TableField jdbcField = this.getTableField(field, false);
      jdbcFields.add(jdbcField);
    }
    jdbcTable.setFields(jdbcFields);
    return jdbcTable;
  }

  /**
   * 获取表字段信息
   * @param field
   * @param isExit
   * @return
   */
  private TableField getTableField(DynamicTableFieldEntity field, boolean isExit) {
    TableField jdbcField = new TableField();
    TableFieldType fieldType = TableFieldType.valueOfType(field.getType());
    jdbcField.setName(field.getName());
    jdbcField.setType(fieldType.getDatabaseType());
    jdbcField.setRadix(0);
    jdbcField.setNullable(field.getNullable());
    jdbcField.setIsPrimaryKey(field.getIsPrimaryKey());
    jdbcField.setIsUnique(field.getIsUnique());
    jdbcField.setDesc(field.getDescription());
    jdbcField.setIsExit(isExit);
    jdbcField.setLength(field.getLength());
    return jdbcField;
  }

}
