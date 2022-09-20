package org.freshwater.boot.common.constants.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 状态枚举
 * @author tuxuchen
 * @date 2022/8/29 10:12
 */
@Getter
public enum NormalStatusEnum {

  /**
   * 状态1 启用
   */
  ENABLE(1, "启用"),

  /**
   * 状态0 禁用
   */
  DISABLE(0, "禁用");

  private Integer status;
  private String desc;

  private NormalStatusEnum(Integer status, String desc) {
    this.status = status;
    this.desc = desc;
  }

  public static NormalStatusEnum valueOfStatus(Integer status) {
    if (status == null) {
      return null;
    } else {
      NormalStatusEnum[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
        NormalStatusEnum value = var1[var3];
        if (value.status.equals(status)) {
          return value;
        }
      }

      return null;
    }
  }

  public static NormalStatusEnum valueOfDesc(String desc) {
    if (StringUtils.isBlank(desc)) {
      return null;
    } else {
      NormalStatusEnum[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
        NormalStatusEnum value = var1[var3];
        if (StringUtils.equals(desc, value.getDesc())) {
          return value;
        }
      }
      return null;
    }
  }
}
