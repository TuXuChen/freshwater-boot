package org.freshwater.boot.routing.service;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Pageable;
import org.freshwater.boot.routing.dto.DynamicDatasourceDTO;
import org.freshwater.boot.routing.entity.DynamicDatasourceEntity;

/**
 * <p>
 * 动态数据源 服务类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-23 16:47
 */
public interface DynamicDatasourceService {

  /**
   * 创建动态数据源
   * @param dynamicDatasource
   * @return
   */
  DynamicDatasourceEntity create(DynamicDatasourceDTO dynamicDatasource);

  /**
   * 更新动态数据源
   * @param dynamicDatasource
   * @return
   */
  DynamicDatasourceEntity update(DynamicDatasourceDTO dynamicDatasource);

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 根据ID查询动态数据源
   * @param id
   * @return
  */
  DynamicDatasourceEntity findById(String id);

  /**
   * 分页查询
   * @param pageable
   * @return
   */
  PageInfo<DynamicDatasourceEntity> findPage(Pageable pageable);

  /**
   * 根据编码启用数据源
   * @param code
   * @return
   */
  String enableByCode(String code);

}
