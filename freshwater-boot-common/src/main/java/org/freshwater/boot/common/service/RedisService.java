package org.freshwater.boot.common.service;

import java.util.concurrent.TimeUnit;

/**
 * 提供redis通用的一些服务
 * 方便在业务中快速的使用redis
 * 提供的功能有redis锁,自增值,快速设置k-v
 * @Author: Paul Chan
 * @Date: 2021/6/17 15:34
 */
public interface RedisService {

  /**
   * 按照指定的key值，启动基于redis的分布式锁。使用该方式
   * @param key
   */
  void lock(String key);
  /**
   * 尝试按照指定的key值，基于redis获得分布式锁下的操作权。
   * @param key
   * @param unit 尝试时间的单位
   * @param timeout 尝试时间
   * @return 在尝试获取锁的阶段，该线程将进入阻塞状态，一旦获取到锁（返回true）或者尝试超过了指定时间（返回false），线程将打开阻塞状态
   */
  boolean tryLock(String key, TimeUnit unit, int timeout);
  /**
   * 尝试按照指定的key值，基于redis获得分布式锁下的操作权。
   * @param key 锁键值
   * @param unit 尝试时间的单位
   * @param timeout 尝试时间
   * @param tryTime 尝试次数（假如在尝试时间内获取不到锁，则重新执行tryLock方法，指导尝试次数用完为止）
   *                默认值为：1
   * @return 在尝试获取锁的阶段，该线程将进入阻塞状态，一旦获取到锁（返回true）或者尝试超过了指定时间（返回false），线程将打开阻塞状态
   */
  boolean tryLock(String key, TimeUnit unit, int timeout,int tryTime);
  /**
   * 使用该方法参照指定的key值，基于redis释放本进程对分布式锁的所有权
   * @param lockKey
   */
  void unlock(String lockKey);
  /**
   * 判断当前进程是否基于指定的key值，拥有对应的分布式锁下的操作权限
   * @param key
   * @return
   */
  boolean isLocked(String key);

  /**
   * 使用Redis客户端，在服务器完成一次原子性质的获取和增加操作
   * @param code
   * @return
   */
  long getAndIncrement(String code, long min);

  /**
   * 根据编码从redis获取一个自增长的数字，并且设置一个过期时间
   * @param code
   * @param min
   * @param expire
   * @param unit
   * @return
   */
  long getAndIncrement(String code, long min, long expire, TimeUnit unit);

  /**
   * 获取当前自增的数据,如果没有则返回null
   * @param code
   * @return
   */
  Long getIncrement(String code);

  /**
   * 根据code获取一个数字的字符串并自增长一个数字，如果位数不够则在前面补零，并且设置一个过期时间
   * @param code
   * @param min
   * @param mixStrLen
   * @param expire
   * @param unit
   * @return
   */
  String getAndIncrement(String code, long min, Integer mixStrLen, long expire, TimeUnit unit);

  /**
   * 根据code获取一个数字的字符串并自增长一个数字，如果位数不够则在前面补零
   * @param code
   * @param min
   * @param strLength
   * @return
   */
  String getAndIncrement(String code, long min, Integer strLength);

  /**
   * 设置一个值
   * @param key
   * @param value
   * @return
   */
  void setValue(String key, Object value);

  /**
   * 设置一个值, 并设置过期时间
   * @param key
   * @param value
   * @param timeout
   * @param unit
   */
  void setValue(String key, Object value, long timeout, TimeUnit unit);

  /**
   * 根据key获取一个值
   * @param key
   */
  Object getValue(String key);

  /**
   * 根据key移除redis中的值
   * @param key
   * @return
   */
  boolean delete(String key);

}
