package org.freshwater.boot.common.constants;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 静态变量
 * @author tuxuchen
 * @date 2022/8/29 10:11
 */
public class Constants {

  /**
   * 默认分页
   */
  public static final Pageable DEFAULT_PAGEABLE = PageRequest.of(1, 50);

}
