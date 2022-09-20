package org.freshwater.boot.template.service.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.freshwater.boot.common.utils.JsonUtils;
import org.freshwater.boot.common.utils.Md5Utils;
import org.freshwater.boot.common.utils.ValidationUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.freshwater.boot.template.constant.Constants.WHERE;


/**
 * jdbc相关服务实现
 * @Author: Paul Chan
 * @Date: 2021/7/23 17:08
 */
@Slf4j
@Service
public class JdbcServiceImpl implements JdbcService {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  @Transactional
  public void createTable(Table table) {
    ValidationUtils.validate(table);
    String sql = this.generateCreateTableSql(table);
    jdbcTemplate.execute(sql);
  }

  @Override
  @Transactional
  public void modifyTable(Table table) {
    ValidationUtils.validate(table);
    String sql = this.generateModifyTableSql(table);
    jdbcTemplate.execute(sql);
  }

  @Override
  @Transactional
  public void deleteTable(Table table) {
    ValidationUtils.validate(table);
    String sql = this.generateDeleteTableSql(table);
    jdbcTemplate.execute(sql);
  }

  @Override
  @Transactional
  public Map<String, Object> insert(String tableName, Map<String, Object> data) {
    Validate.notBlank(tableName, "表名不能为空");
    Validate.notNull(data, "插入数据不能为空");
    Object id = data.get("id");
    if(id == null) {
      data.put("id", UUID.randomUUID().toString().replace("-", ""));
    }
    List<Object> args = Lists.newArrayList();
    List<String> columnList = Lists.newArrayList();
    List<String> preArgList = Lists.newArrayList();
    data.forEach((k, v) -> {
      columnList.add(k);
      args.add(v);
      preArgList.add("?");
    });
    String columns = StringUtils.join(columnList, ",");
    String preArgs = StringUtils.join(preArgList, ",");
    String sql = String.format("INSERT INTO `%s` (%s) VALUES (%s)", tableName, columns, preArgs);
    jdbcTemplate.update(sql, args.toArray());
    return data;
  }

  @Override
  @Transactional
  public Map<String, Object> updateById(String tableName, String id, Map<String, Object> data) {
    Validate.notBlank(tableName, "表名不能为空");
    Validate.notBlank(id, "主键ID不能为空");
    Validate.notNull(data, "插入数据不能为空");
    data.remove("id");
    List<Object> args = Lists.newArrayList();
    List<String> columnList = Lists.newArrayList();
    data.forEach((k, v) -> {
      columnList.add(StringUtils.join(k, " = ? "));
      args.add(v);
    });
    args.add(id);
    String columns = StringUtils.join(columnList, ",");
    String sql = String.format("UPDATE `%s` SET %s WHERE `id` = ? ", tableName, columns);
    jdbcTemplate.update(sql, args.toArray());
    return data;
  }

  @Override
  public List<Map<String, Object>> queryForList(String sql, Map<String, Object> params, Pageable pageable, Sort sort) {
    Validate.notBlank(sql, "sql不能为空");
    params = ObjectUtils.defaultIfNull(params, Maps.newHashMap());
    List<Object> args = Lists.newArrayList();
    String whereSql = this.generateWhereSql(sql, params, args);
    sql += whereSql;
    if(sort != null && sort.isSorted()) {
      String sortSql = sort.stream().map(o -> o.getProperty() + " " + o.getDirection().name()).collect(Collectors.joining(","));
      sql += "order by " + sortSql;
    }
    if(pageable != null) {
      int offset = (pageable.getPageNumber() - 1) * pageable.getPageSize();
      sql += " limit ?,? ";
      args.add(offset);
      args.add(pageable.getPageSize());
    }
    return this.queryForList(sql, args);
  }

  @Override
  public List<Map<String, Object>> queryForList(String sql, List<Object> args) {
    Validate.notBlank(sql, "sql不能为空");
    args = ObjectUtils.defaultIfNull(args, Lists.newArrayList());
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
    if(log.isDebugEnabled()) {
      log.debug("Executing prepared SQL Parameters:{}", JsonUtils.obj2JsonString(args));
    }
    return list;
  }

  @Override
  public Map<String, Object> queryMapById(String tableName, String id) {
    if(StringUtils.isAnyBlank(tableName, id)) {
      return null;
    }
    String sql = String.format("select * from %s where id = ?", tableName);
    Map<String, Object> result = null;
    try {
      result = jdbcTemplate.queryForMap(sql, id);
    } catch (IncorrectResultSizeDataAccessException e) {
      log.error("数据库没有查到数据, 但是不需要处理这个异常, 直接吃掉");
    }
    if(log.isDebugEnabled()) {
      log.debug("Executing prepared SQL Parameters:{}", id);
    }
    return result;
  }

  /**
   * 生成where语句
   * @param params
   * @return
   */
  private String generateWhereSql(String sql, Map<String, Object> params, List<Object> args) {
    StringBuilder whereSql = new StringBuilder();
    if(!StringUtils.contains(sql, WHERE)) {
      whereSql.append(" where 1=1 ");
    }
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      String k = entry.getKey();
      Object v = entry.getValue();
      if (v != null) {
        if (Collection.class.isAssignableFrom(v.getClass())) {
          Collection collection = (Collection) v;
          Validate.isTrue(collection.size() == 2, "区间参数数量必须为2");
          Iterator iterator = collection.iterator();
          Object start = iterator.next();
          Object end = iterator.next();
          whereSql.append(StringUtils.join(" and ", k, " >= ? and ", k, " <= ? "));
          args.add(start);
          args.add(end);
        } else if(v.getClass().isArray()) {
          Object[] objs = (Object[]) v;
          Validate.isTrue(objs.length == 2, "区间参数数量必须为2");
          whereSql.append(StringUtils.join(" and ", k, " >= ? and ", k, " <= ? "));
          args.addAll(Arrays.asList(objs));
        } else {
          whereSql.append(StringUtils.join(" and ", k, " like concat('%', ?, '%') "));
          args.add(v);
        }
      }
    }
    return whereSql.toString();
  }

  /**
   * 生成删除表结构的sql
   * @param table
   * @return
   */
  private String generateDeleteTableSql(Table table) {
    String sql = "ALTER TABLE `%s` \n" +
        "%s;";
    List<TableField> fields = table.getFields();
    List<String> sqls = Lists.newArrayList();
    for (TableField field : fields) {
      String dropSql = StringUtils.join("DROP COLUMN", field.getName());
      sqls.add(dropSql);
    }
    String fieldSql = StringUtils.join(sqls, ",");
    sql = String.format(sql, table.getName(), fieldSql);
    return sql;
  }

  /**
   * 生成修改表结构的sql
   * @param table
   * @return
   */
  private String generateModifyTableSql(Table table) {
    String sql = "ALTER TABLE `%s` \n" +
        "%s;";
    String fieldSql = this.generateModifyTableFieldSql(table.getFields());
    sql = String.format(sql, table.getName(), fieldSql);
    return sql;
  }

  /**
   * 生成修改字段的sql片段
   * @param fields
   * @return
   */
  private String generateModifyTableFieldSql(List<TableField> fields) {
    List<String> sqls = Lists.newArrayList();
    List<TableField> modifyFields = fields.stream().filter(f -> f.getIsExit()).collect(Collectors.toList());
    List<TableField> newFields = fields.stream().filter(f -> !f.getIsExit()).collect(Collectors.toList());
    for (TableField field : modifyFields) {
      String sql = StringUtils.join("MODIFY COLUMN ", this.generateFieldSql(field));
      sqls.add(sql);
    }
    for (TableField field : newFields) {
      String sql = StringUtils.join("ADD COLUMN ", this.generateFieldSql(field));
      sqls.add(sql);
    }
    return StringUtils.join(sqls, ",");
  }

  /**
   * 生成创建表的sql
   * @param table
   * @return
   */
  private String generateCreateTableSql(Table table) {
    String sql = "CREATE TABLE `%s`  (\n" +
        "  %s " +
        ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '%s' ROW_FORMAT = Dynamic;";
    String fieldSql = this.generateCreateTableFieldSql(table, table.getFields());
    sql = String.format(sql, table.getName(), fieldSql, table.getDescription());
    return sql;
  }

  /**
   * 生成字段属性的sql片段
   * @param field
   * @return
   */
  private String generateFieldSql(TableField field) {
    String fieldType = this.getFieldType(field);
    String nullable = field.getNullable() ? "NULL" : "NOT NULL";
    return String.format("`%s` %s %s COMMENT '%s' \n", field.getName(), fieldType, nullable, field.getDesc());
  }

  /**
   * 生成创建表的字段sql片段
   * @param table
   * @param fields
   * @return
   */
  private String generateCreateTableFieldSql(Table table, List<TableField> fields) {
    TableField primaryKey = null;
    List<TableField> uniqueFields = Lists.newArrayList();
    List<String> sqls = Lists.newArrayList();
    for (TableField field : fields) {
      if(field.getIsPrimaryKey()) {
        primaryKey = field;
      } else if(field.getIsUnique()) {
        uniqueFields.add(field);
      }
      sqls.add(this.generateFieldSql(field));
    }
    if(primaryKey != null) {
      sqls.add(String.format("PRIMARY KEY (`%s`)\n", primaryKey.getName()));
    }
    if(!CollectionUtils.isEmpty(uniqueFields)) {
      for (TableField field : uniqueFields) {
        String uniqueName = this.generateUniqueName(table.getName(), field.getName());
        sqls.add(String.format("UNIQUE INDEX `%s`(`%s`) USING BTREE\n", uniqueName, field.getName()));
      }
    }
    return StringUtils.join(sqls, ",");
  }

  /**
   * 生成唯一索引name
   * @param tableName
   * @param fieldName
   * @return
   */
  private String generateUniqueName(String tableName, String fieldName) {
    String content = StringUtils.join(tableName, "_", fieldName);
    String encode = Md5Utils.encode(content, 35);
    return StringUtils.join("UK", encode);
  }

  /**
   * 获取字段类型sql片段
   * @param field
   * @return
   */
  private String getFieldType(TableField field) {
    String type = field.getType();
    FieldType fieldType = FieldType.valueOfType(type);
    Validate.notNull(fieldType, "不支持的字段类型:%s", type);
    switch (fieldType) {
      case VARCHAR:
      case BIT:
      case INT:
      case BIGINT:
        return StringUtils.join(type, "(", field.getLength(), ")");
      case DECIMAL:
        return StringUtils.join(type, "(", field.getLength(), ",", field.getRadix(), ")");
      case DATE:
      case TEXT:
      case DATETIME:
        return type;
      default:
        throw new UnsupportedOperationException();
    }
  }

}
