package org.freshwater.boot.template.service.jdbc;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * jdbc相关服务
 * @Author: Paul Chan
 * @Date: 2021/7/23 17:07
 */
public interface JdbcService {

  /**
   * 创建数据库表
   * @param table
   */
  void createTable(Table table);

  /**
   * 更新表结构
   * @param table
   */
  void modifyTable(Table table);

  /**
   * 删除表结构
   * @param table
   */
  void deleteTable(Table table);

  /**
   * 插入一条数据
   * @param tableName
   * @param data
   * @return
   */
  Map<String, Object> insert(String tableName, Map<String, Object> data);

  /**
   * 更新一条数据
   * @param tableName
   * @param id
   * @param data
   * @return
   */
  Map<String, Object> updateById(String tableName, String id, Map<String, Object> data);

  /**
   * 查询列表数据
   * @param sql
   * @param params
   * @param pageable
   * @return
   */
  List<Map<String, Object>> queryForList(String sql, Map<String, Object> params, Pageable pageable, Sort sort);

  /**
   * 查询列表数据
   * @param sql
   * @param args
   * @return
   */
  List<Map<String, Object>> queryForList(String sql, List<Object> args);

  /**
   * 根据主键ID查询
   * @param tableName
   * @param id
   * @return
   */
  Map<String, Object> queryMapById(String tableName, String id);
}
