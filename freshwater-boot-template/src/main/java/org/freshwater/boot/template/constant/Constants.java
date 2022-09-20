package org.freshwater.boot.template.constant;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.freshwater.boot.template.constant.enums.TableFieldType;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;

import java.util.List;
import java.util.Set;

/**
 * 静态变量类
 * @author tuxuchen
 * @date 2022/8/10 16:43
 */
public class Constants {

  private Constants() {
    throw new UnsupportedOperationException();
  }

  /**
   * 动态表编码前缀
   */
  public static final String TABLE_CODE_PREFIX = "dynamic_";

  /**
   * where
   */
  public static final String WHERE = "where";

  /**
   * 默认字段长度
   */
  public static final int DEFAULT_FIELD_LENGTH = 255;

  /**
   * 主键ID
   */
  public static final DynamicTableFieldEntity FIELD_ID = new DynamicTableFieldEntity(null, "id", 1, "主键ID", TableFieldType.STRING.getType(), 1, 1, DEFAULT_FIELD_LENGTH, false, true, true, false, false, false, false);

  /**
   * 默认字段
   */
  public static final List<DynamicTableFieldEntity> FIELD_OPERATOR = Lists.newArrayList(
      new DynamicTableFieldEntity(null, "creator", 1, "创建人账号", TableFieldType.STRING.getType(), 1, 100, DEFAULT_FIELD_LENGTH, false, false, false, false, false, false, false),
      new DynamicTableFieldEntity(null, "creator_name", 1, "创建人名称", TableFieldType.STRING.getType(), 1, 101, DEFAULT_FIELD_LENGTH, false, false, false, false, false, true, true),
      new DynamicTableFieldEntity(null, "create_time", 1, "创建时间", TableFieldType.DATETIME.getType(), 1, 102, 0, false, false, false, false, false, false, false),
      new DynamicTableFieldEntity(null, "modifier", 1, "修改人账号", TableFieldType.STRING.getType(), 1, 103, DEFAULT_FIELD_LENGTH, false, false, false, false, false, false, false),
      new DynamicTableFieldEntity(null, "modifier_name", 1, "修改人名称", TableFieldType.STRING.getType(), 1, 104, DEFAULT_FIELD_LENGTH, false, false, false, false, false, true, true),
      new DynamicTableFieldEntity(null, "modify_time", 1, "修改时间", TableFieldType.DATETIME.getType(), 1, 105, 0, false, false, false, false, false, false, false)
  );

  /**
   * 固定字段
   */
  public static final Set<String> FIXATE_FIELD_NAME = Sets.newHashSet("id", "creator", "creator_name", "create_time", "modifier", "modifier_name", "modify_time");

}
