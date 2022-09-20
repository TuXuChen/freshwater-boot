package org.freshwater.boot.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * redis配置信息
 * @author tuxuchen
 * @date 2022/8/29 10:08
 */
@Data
@EnableConfigurationProperties({RedisProperties.class})
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

  /**
   * redis的连接模式</br>
   * 一共三种 单点模式(single)，集群模式(cluster)，哨兵模式(sentinel)
   */
  private String model;
  /**
   * redis的一个或者多个连接地址
   */
  private String[] address;
  /**
   * 数据库
   */
  private int database = 0;
  /**
   * 命令反馈超时时间（单位毫秒）
   */
  private int timeout = 3000;
  /**
   * 重试尝试次数
   */
  private int retryAttempts = 10;
  /**
   * 重试连接间隔时间
   */
  private int retryInterval = 1500;
  /**
   * 心跳间隔
   */
  private int pingConnectionInterval = 1000 * 60 * 2;
  /**
   * 各种环境里共用的连接数大小，包括master、salve、订阅和发布等环境
   */
  private int connectionPoolSize = 10;
  /**
   * 各种环境里共用的最小空闲连接数大小，包括master、salve、订阅和发布等环境
   */
  private int connectionMinimumIdleSize = 5;
  /**
   * 可能存在的redis登录密码
   */
  private String password;

  /**
   * redis的连接模式
   *
   * @Author: Paul Chan
   * @Date: 2021/6/4 9:24
   */
  public enum RedisModel {
    SINGLE("single"),
    CLUSTER("cluster"),
    SENTINEL("sentinel");

    private String model;

    RedisModel(String model) {
      this.model = model;
    }

    public String getModel() {
      return model;
    }
  }
}
