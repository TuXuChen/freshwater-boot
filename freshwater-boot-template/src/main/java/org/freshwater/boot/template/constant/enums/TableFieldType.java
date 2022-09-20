package org.freshwater.boot.template.constant.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 字段类型,文本:string, 整型:int, 长整型:long, 日期:date, 时间:datetime, 浮点型:decimal, 布尔类型:boolean
 * @Author: Paul Chan
 * @Date: 2021/7/22 20:17
 */
@Getter
public enum TableFieldType {
  /**
   * 字段类型,文本:string, 整型:int, 长整型:long, 日期:date, 时间:datetime, 浮点型:decimal, 布尔类型:boolean
   */
  STRING("string", "varchar"),
  TEXT("text", "text"),
  INT("int", "int"),
  LONG("long", "bigint"),
  DATE("date", "date"),
  DATETIME("datetime", "datetime"),
  DECIMAL("decimal", "decimal"),
  BOOLEAN("boolean", "bit");

  private String type;

  private String databaseType;

  TableFieldType(String type, String databaseType) {
    this.type = type;
    this.databaseType = databaseType;
  }

  /**
   * 根据类型值获取枚举
   * @param type
   * @return
   */
  public static TableFieldType valueOfType(String type) {
    if(StringUtils.isBlank(type)) {
      return null;
    }
    for (TableFieldType value : values()) {
      if(value.type.equals(type)) {
        return value;
      }
    }
    return null;
  }

}
