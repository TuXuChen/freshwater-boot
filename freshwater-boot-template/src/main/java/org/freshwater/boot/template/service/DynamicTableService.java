package org.freshwater.boot.template.service;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Pageable;
import org.freshwater.boot.template.dto.DynamicTableCreateDTO;
import org.freshwater.boot.template.dto.DynamicTableQueryDTO;
import org.freshwater.boot.template.dto.DynamicTableUpdateDTO;
import org.freshwater.boot.template.dto.ExecuteModel;
import org.freshwater.boot.template.entity.DynamicTableEntity;
import org.freshwater.boot.template.vo.DynamicTableDetailsVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 动态表 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-10 16:15
 */
public interface DynamicTableService {

  /**
   * 创建动态表
   * <p>
   *   创建数据库表
   *   根据数据库表执行代码生成器 生成文件
   * </p>
   * @param table
   * @return
   */
  DynamicTableEntity create(DynamicTableCreateDTO table);

  /**
   * 更新动态表
   * @param table
   * @return
   */
  DynamicTableEntity update(DynamicTableUpdateDTO table);

  /**
   * 根据ID查询动态表
   * @param id
   * @return
  */
  DynamicTableEntity findById(String id);

  /**
   * 根据ID查询详情信息
   * @param id
   * @return
   */
  DynamicTableDetailsVO findDetailsById(String id);

  /**
   * 根据编码查询基本信息
   * @param code
   * @return
   */
  DynamicTableEntity findByCode(String code);

  /**
   * 根据编码查询详情信息
   * @param code
   * @return
   */
  DynamicTableDetailsVO findDetailsByCode(String code);

  /**
   * 多条件分页查询
   * @param query
   * @param pageable
   * @return
   */
  PageInfo<DynamicTableEntity> findByConditions(DynamicTableQueryDTO query, Pageable pageable);

  /**
   * 多条件查询
   * @param query
   * @return
   */
  List<DynamicTableEntity> findAllByConditions(DynamicTableQueryDTO query);

  /**
   * 执行新增动态表数据
   * @param code
   * @param data
   * @return
   */
  Map<String, Object> executeInsert(String code, Map<String, Object> data);

  /**
   * 执行更新动态表数据
   * @param code
   * @param data
   * @return
   */
  Map<String, Object> executeUpdate(String code, Map<String, Object> data);

  /**
   * 根据ID查询动态表数据
   * @param code
   * @param id
   * @return
   */
  Map<String, Object> executeQueryById(String code, String id);

  /**
   * 执行动态表查询
   * @param executeModel
   * @return
   */
  List<Map<String, Object>> executeQuery(ExecuteModel executeModel);

  /**
   * 执行动态表查询分页数据
   * @param executeModel
   * @return
   */
  PageInfo executeQueryPage(ExecuteModel executeModel);

  /**
   * 执行Excel动态表数据导出
   * @param executeModel
   */
  void executeExportQuery(ExecuteModel executeModel);
}
