package org.freshwater.boot.common.configuration;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置
 * @author tuxuchen
 * @date 2022/8/29 10:10
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfiguration {

  /**
   * redis连接地址前缀
   */
  private static final String REDIS_PREFIX = "redis://";

  @Autowired
  private RedisProperties redisProperties;

  /**
   * 配置redisson客户端
   * @return
   */
  @Bean
  public RedissonClient getRedissonClient() {
    Config config = new Config();
    RedissonClient redissonClient = null;
    // 如果条件成立，说明是单节点配置模式
    if(StringUtils.equals(redisProperties.getModel(), RedisProperties.RedisModel.SINGLE.getModel())) {
      SingleServerConfig serverConfig = config.useSingleServer()
          .setAddress(REDIS_PREFIX + redisProperties.getAddress()[0])
          .setDatabase(redisProperties.getDatabase())
          .setTimeout(redisProperties.getTimeout())
          .setRetryAttempts(redisProperties.getRetryAttempts())
          .setRetryInterval(redisProperties.getRetryInterval())
          .setPingConnectionInterval(redisProperties.getPingConnectionInterval())
          .setConnectionPoolSize(redisProperties.getConnectionPoolSize())
          .setConnectionMinimumIdleSize(redisProperties.getConnectionMinimumIdleSize());
      if(!StringUtils.isBlank(redisProperties.getPassword())) {
        serverConfig.setPassword(redisProperties.getPassword());
      }
      redissonClient = Redisson.create(config);
    }

    // 如果条件成立，说明是集群模式配置模式
    if(StringUtils.equals(redisProperties.getModel(), RedisProperties.RedisModel.CLUSTER.getModel())) {
      ClusterServersConfig serverConfig = config.useClusterServers()
          .addNodeAddress(redisProperties.getAddress())
          .setTimeout(redisProperties.getTimeout())
          .setRetryAttempts(redisProperties.getRetryAttempts())
          .setRetryInterval(redisProperties.getRetryInterval())
          .setPingConnectionInterval(redisProperties.getPingConnectionInterval())
          .setMasterConnectionPoolSize(redisProperties.getConnectionPoolSize())
          .setSlaveConnectionPoolSize(redisProperties.getConnectionPoolSize())
          .setMasterConnectionMinimumIdleSize(redisProperties.getConnectionMinimumIdleSize())
          .setSlaveConnectionMinimumIdleSize(redisProperties.getConnectionMinimumIdleSize());
      if(!StringUtils.isBlank(redisProperties.getPassword())) {
        serverConfig.setPassword(redisProperties.getPassword());
      }
      redissonClient = Redisson.create(config);
    }

    // 如果条件成立，说明是哨兵模式配置模式
    if(StringUtils.equals(redisProperties.getModel(), RedisProperties.RedisModel.SENTINEL.getModel())) {
      SentinelServersConfig serverConfig = config.useSentinelServers()
          .addSentinelAddress(redisProperties.getAddress())
          .setDatabase(redisProperties.getDatabase())
          .setTimeout(redisProperties.getTimeout())
          .setRetryAttempts(redisProperties.getRetryAttempts())
          .setRetryInterval(redisProperties.getRetryInterval())
          .setPingConnectionInterval(redisProperties.getPingConnectionInterval())
          .setMasterConnectionPoolSize(redisProperties.getConnectionPoolSize())
          .setSlaveConnectionPoolSize(redisProperties.getConnectionMinimumIdleSize());
      if(!StringUtils.isBlank(redisProperties.getPassword())) {
        serverConfig.setPassword(redisProperties.getPassword());
      }
      redissonClient = Redisson.create(config);
    }
    return redissonClient;
  }
}
