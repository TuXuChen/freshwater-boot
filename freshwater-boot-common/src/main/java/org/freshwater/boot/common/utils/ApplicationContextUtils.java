package org.freshwater.boot.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring上下文的工具类，此工具类在系统启动时会将spring上下文初始化到静态变量中，
 * 方便在应用脱离Spring的代码中获取spring的上下文
 * @author tuxuchen
 * @date 2022/8/29 10:22
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

  /**
   * spring 上下文
   */
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ApplicationContextUtils.applicationContext = applicationContext;
  }

  /**
   * 获取spring上下文
   * @return
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
