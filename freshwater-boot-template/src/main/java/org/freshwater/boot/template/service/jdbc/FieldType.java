package org.freshwater.boot.template.service.jdbc;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据库字段类型
 * @Author: Paul Chan
 * @Date: 2021/7/23 17:41
 */
@Getter
public enum FieldType {

  VARCHAR("varchar"),
  TEXT("text"),
  INT("int"),
  BIGINT("bigint"),
  DATE("date"),
  DATETIME("datetime"),
  DECIMAL("decimal"),
  BIT("bit");

  /**
   * 类型
   */
  private String type;

  FieldType(String type) {
    this.type = type;
  }

  /**
   * 根据类型值获取枚举
   * @param type
   * @return
   */
  public static FieldType valueOfType(String type) {
    if(StringUtils.isBlank(type)) {
      return null;
    }
    for (FieldType value : values()) {
      if(value.type.equals(type)) {
        return value;
      }
    }
    return null;
  }

}
