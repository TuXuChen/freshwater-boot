package org.freshwater.boot.template.mapper;

import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.template.dto.DynamicTableQueryDTO;
import org.freshwater.boot.template.entity.DynamicTableEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 动态表 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-10 16:09
 */
public interface DynamicTableMapper extends BaseMapper<DynamicTableEntity> {

  /**
   * 根据表名进行统计
   * @param tableName
   * @return
   */
  long selectCountByTableName(@Param("tableName") String tableName);

  /**
   * 根据表名进行统计 排除id
   * @param tableName
   * @param id
   * @return
   */
  long selectCountByTableNameAndIdNot(@Param("tableName") String tableName, @Param("id") String id);

  /**
   * 根据编码查询
   * @param code
   * @return
   */
  DynamicTableEntity selectByCode(@Param("code") String code);

  /**
   * 多条件查询
   * @param query
   * @return
   */
  List<DynamicTableEntity> selectByConditions(@Param("query") DynamicTableQueryDTO query);

}
