package org.freshwater.boot.template.mapper;

import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.template.dto.DynamicTableFieldQueryDTO;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-10 16:22
 */
public interface DynamicTableFieldMapper extends BaseMapper<DynamicTableFieldEntity> {

  /**
   * 根据表ID查询字段
   * @param tableId
   * @return
   */
  List<DynamicTableFieldEntity> selectByTableId(@Param("tableId") String tableId);

  /**
   * 多条件查询
   * @param query
   * @return
   */
  List<DynamicTableFieldEntity> selectByConditions(@Param("query") DynamicTableFieldQueryDTO query);

}
