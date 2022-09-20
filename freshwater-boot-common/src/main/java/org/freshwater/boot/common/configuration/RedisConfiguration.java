package org.freshwater.boot.common.configuration;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 配置类
 * @author tuxuchen
 * @date 2022/8/29 10:08
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {

  @Autowired
  private RedisProperties redisProperties;

  /**
   * 配置redis的连接配置
   * @return
   */
  @Bean
  public JedisConnectionFactory getJedisConnectionFactory() {
    Validate.notEmpty(redisProperties.getAddress(), "请配置redis信息");
    String firstAddress = redisProperties.getAddress()[0];
    String password = redisProperties.getPassword();
    String[] address = firstAddress.split(":");
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(address[0], Integer.valueOf(address[1]));
    redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
    if(StringUtils.isNotBlank(password)) {
      redisStandaloneConfiguration.setPassword(password);
    }
    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  /**
   * 配置jedis连接池配置
   * @return
   */
  @Bean
  public JedisPoolConfig getJedisPoolConfig() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    return jedisPoolConfig;
  }

  /**
   * 配置jedis连接池
   * @return
   */
  @Bean
  public JedisPool getJedisPool(JedisPoolConfig jedisPoolConfig) {
    String firstAddress = redisProperties.getAddress()[0];
    String password = redisProperties.getPassword();
    String[] address = firstAddress.split(":");
    JedisPool jedisPool = new JedisPool(jedisPoolConfig, address[0], Integer.valueOf(address[1]), redisProperties.getTimeout(), password);
    log.debug("此时获取到JEDIS POLL，地址:{},端口:{}", address[0], address[1]);
    return jedisPool;
  }

  @Bean
  public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    // 设置value的序列化规则和 key的序列化规则
    redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    // 最好是调用一下这个方法
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
