package org.freshwater.boot.template.constant.enums;

import lombok.Getter;

/**
 * 显示类型,1:文本,2图片,3:视频,4:时间,5:省市区,6:日期,7:码值,8:文本域,9:商品,10:URL,11:电话号码
 * @author tuxuchen
 * @date 2022/8/16 10:49
 */
@Getter
public enum ShowType {

  /**
   * 显示类型,1:文本,2图片,3:视频,4:时间,5:省市区,6:日期,7:码值,8:文本域,9:商品,10:URL,11:电话号码
   */
  TEXT(1),
  IMAGE(2),
  VIDEO(3),
  DATETIME(4),
  REGION(5),
  DATE(6),
  CODE_VALUE(7),
  TEXT_AREA(8),
  PRODUCT(9),
  URL(10),
  PHONE(11);

  private Integer type;

  ShowType(Integer type) {
    this.type = type;
  }

  /**
   * 根据类型值获取枚举
   * @param type
   * @return
   */
  public static ShowType valueOfType(Integer type) {
    if(type == null) {
      return null;
    }
    for (ShowType value : values()) {
      if(value.type.equals(type)) {
        return value;
      }
    }
    return null;
  }

}
