package org.freshwater.boot.rbac.constants.enums;

/**
 * 按钮类型
 *  类型:1:web,2:APP
 * @author tuxuchen
 * @date 2022/7/25 15:20
 */
public enum ButtonType {

  /**
   * 按钮类型:1 web
   */
  BUTTON_WEB(1, "web平台"),

  /**
   * 按钮类型:2 APP
   */
  BUTTON_APP(2, "APP");

  private Integer type;

  private String desc;

  ButtonType(Integer type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  /**
   * 根据类型值获取枚举
   *
   * @param type
   * @return
   */
  public static ButtonType valueOfType(Integer type) {
    if (type == null) {
      return null;
    }
    for (ButtonType value : values()) {
      if (value.type.equals(type)) {
        return value;
      }
    }
    return null;
  }

  public Integer getType() {
    return type;
  }

  public String getDesc() {
    return desc;
  }

}
