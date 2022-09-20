package org.freshwater.boot.template.service;

import org.freshwater.boot.template.dto.DynamicTableFieldQueryDTO;
import org.freshwater.boot.template.entity.DynamicTableEntity;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-10 16:22
 */
public interface DynamicTableFieldService {

  /**
   * 保存表字段信息
   * @param table
   * @param fields
   * @return
   */
  void save(DynamicTableEntity table, List<DynamicTableFieldEntity> fields);

  /**
   * 根据表ID查询字段信息
   * @param tableId
   * @return
   */
  List<DynamicTableFieldEntity> findByTableId(String tableId);

  /**
   * 多条件查询
   * @param query
   * @return
   */
  List<DynamicTableFieldEntity> findAllByConditions(DynamicTableFieldQueryDTO query);
}
