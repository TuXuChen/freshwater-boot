package org.freshwater.boot.template.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页信息的dto
 * @author tuxuchen
 * @date 2022/8/19 14:46
 */
@Data
public class PageRequestModel implements Serializable {
  private static final long serialVersionUID = -704568376593696248L;

  /**
   * 页数
   */
  private int page;

  /**
   * 分页大小
   */
  private int size = 50;

}
