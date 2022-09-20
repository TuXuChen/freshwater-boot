package org.freshwater.boot.routing.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.freshwater.boot.routing.constant.Constants.DEFAULT_DATASOURCE_CODE;

/**
 * 动态数据源提供者
 * @author tuxuchen
 * @date 2022/8/23 17:11
 */
@Slf4j
public class DynamicDataSourceProvider {

  /**
   * 数据源集合
   */
  private static Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

  /**
   * 读写锁
   */
  private static ReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * 用于存储lock的condition
   */
  private static final Map<String, Condition> CONDITION_MAP = new ConcurrentHashMap<>();

  private DynamicDataSourceProvider() {
    throw new UnsupportedOperationException();
  }

  /**
   * 等待某一个数据源初始化
   * 包含数据库和表的创建
   * @param name
   */
  public static void waitForDatasource(String name) {
    Validate.notBlank(name, "数据源名称不能为空");
    lock.writeLock().lock();
    try {
      // 如果数据源有, 则不需要等待
      if(exit(name)) {
        return;
      }
      Condition condition = getCondition(name);
      log.info("线程[{}}]等待数据源[{}]初始化", Thread.currentThread().getName(), name);
      condition.await();
    } catch (InterruptedException e) {
      log.error("线程已中断");
      log.error(e.getMessage(), e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * 获取锁的条件
   * @param name
   */
  private static Condition getCondition(String name) {
    Condition condition = CONDITION_MAP.get(name);
    if(condition == null) {
      condition = lock.writeLock().newCondition();
      CONDITION_MAP.put(name, condition);
    }
    return condition;
  }

  /**
   * 等待某一个数据源
   * 包含数据库和表的创建
   * 设置一段时间
   * @param name
   */
  public static void waitForDatasource(String name, long time, TimeUnit unit) {
    Validate.notBlank(name, "数据源名称不能为空");
    lock.writeLock().lock();
    try {
      // 如果数据源有, 则不需要等待
      if(exit(name)) {
        return;
      }
      Condition condition = getCondition(name);
      log.info("线程[{}]等待数据源[{}]初始化, 时间:{}毫秒", Thread.currentThread().getName(), name, unit.toMillis(time));
      condition.await(time, unit);
    } catch (InterruptedException e) {
      log.error("线程已中断");
      log.error(e.getMessage(), e);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * 唤醒所有等待该数据源初始化的线程
   * @param name
   */
  private static void signalAllForWaitDatasource(String name) {
    log.info("唤醒所有等待数据源[{}]初始化的线程", name);
    lock.writeLock().lock();
    try {
      Condition condition = CONDITION_MAP.get(name);
      if(condition != null) {
        condition.signalAll();
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * 根据数据源名称获取数据源
   * @param name
   * @return
   */
  public static DataSource getDatasource(String name) {
    log.debug("获取数据源:{}", name);
    try {
      lock.readLock().lock();
      if(StringUtils.isBlank(name)) {
        return dataSourceMap.get(DEFAULT_DATASOURCE_CODE);
      }
      return dataSourceMap.get(name);
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * 添加一个数据源
   * @param name
   * @param dataSource
   */
  public static void addDatasource(String name, DataSource dataSource) {
    Validate.notBlank(name, "数据源名称不能为空");
    Validate.notNull(dataSource, "数据源不能为空");
    log.debug("添加数据源:{}", name);
    try {
      lock.writeLock().lock();
      Validate.isTrue(!dataSourceMap.containsKey(name), "数据源已存在:%s", name);
      dataSourceMap.put(name, dataSource);
      signalAllForWaitDatasource(name);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * 更新一个数据源
   * @param name
   * @param dataSource
   */
  public static void reloadDatasource(String name, DataSource dataSource) {
    Validate.notBlank(name, "数据源名称不能为空");
    Validate.notNull(dataSource, "数据源不能为空");
    log.debug("更新数据源:{}", name);
    try {
      lock.writeLock().lock();
      remove(name);
      dataSourceMap.put(name, dataSource);
      signalAllForWaitDatasource(name);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * 查看数据源是否存在
   * @param name
   * @return
   */
  public static boolean exit(String name) {
    if(StringUtils.isBlank(name)) {
      return false;
    }
    return dataSourceMap.containsKey(name);
  }

  /**
   * 移除数据源
   * @param name
   */
  public static void remove(String name) {
    Validate.notBlank(name, "数据源名称不能为空");
    log.debug("移除数据源:{}", name);
    try {
      lock.writeLock().lock();
      DataSource dataSource = dataSourceMap.get(name);
      if(dataSource != null) {
        if(dataSource instanceof DruidDataSource) {
          ((DruidDataSource) dataSource).close();
        }
        dataSourceMap.remove(name);
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * 清除数据源,保留默认的数据源
   */
  public static void clear() {
    log.debug("清除所有数据源");
    try {
      lock.writeLock().lock();
      Iterator<Map.Entry<String, DataSource>> iterator = dataSourceMap.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, DataSource> entry = iterator.next();
        if(!DEFAULT_DATASOURCE_CODE.equals(entry.getKey())) {
          DataSource dataSource = entry.getValue();
          if(dataSource instanceof DruidDataSource) {
            ((DruidDataSource) dataSource).close();
          }
          iterator.remove();
        }
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

}
