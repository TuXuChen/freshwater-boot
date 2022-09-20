package org.freshwater.boot.routing.mapper;

import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.routing.entity.DynamicDatasourceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 动态数据源 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-23 16:47
 */
public interface DynamicDatasourceMapper extends BaseMapper<DynamicDatasourceEntity> {

  /**
   * 根据编码统计
   * @param code
   * @return
   */
  long selectCountByCode(@Param("code") String code);

  /**
   * 根据编码查询
   * @param code
   * @return
   */
  DynamicDatasourceEntity selectByCode(@Param("code") String code);
}
