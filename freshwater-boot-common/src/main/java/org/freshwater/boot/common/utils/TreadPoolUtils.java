package org.freshwater.boot.common.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池对象
 * @author tuxuchen
 * @date 2022/8/29 10:31
 */
public class TreadPoolUtils {

  /**
   * 获取一个线程池
   *
   * @return 线程池对象
   */
  public ThreadPoolExecutor getThreadPoolExecutor() {
    // 核心线程数
    int corePoolSize = 8;
    // 最大线程数
    int maxPoolSize = 16;
    // 超过核心线程数corePoolSize 线程数量的最大线程空闲时间
    long keepAliveTime = 60;
    // 以秒为时间单位
    TimeUnit unit = TimeUnit.SECONDS;
    // 创建工作队列
    LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(500);
    ThreadPoolExecutor threadPoolExecutor = null;
    // 创建线程池
    threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
        maxPoolSize,
        keepAliveTime,
        unit,
        workQueue,
        new ThreadPoolExecutor.AbortPolicy());
    return threadPoolExecutor;
  }
}
