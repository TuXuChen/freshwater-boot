package org.freshwater.boot.routing.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * druid配置信息
 * @author tuxuchen
 * @date 2022/8/23 17:20
 */
@Data
@Component
@EnableConfigurationProperties({DruidProperties.class})
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidProperties {

  public final static int DEFAULT_INITIAL_SIZE = 0;
  public final static int DEFAULT_MAX_ACTIVE_SIZE = 8;
  public final static int DEFAULT_MAX_IDLE = 8;
  public final static int DEFAULT_MIN_IDLE = 0;
  public final static int DEFAULT_MAX_WAIT = -1;

  /**
   * 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
   */
  protected volatile int initialSize = DEFAULT_INITIAL_SIZE;

  /**
   * 最大连接池数量
   */
  protected volatile int maxActive = DEFAULT_MAX_ACTIVE_SIZE;

  /**
   * 最小连接池数量
   */
  protected volatile int minIdle = DEFAULT_MIN_IDLE;

  /**
   * 已经不再使用，配置了也没效果
   */
  protected volatile int maxIdle = DEFAULT_MAX_IDLE;

  /**
   * 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
   */
  protected volatile long maxWait = DEFAULT_MAX_WAIT;

}
