package org.freshwater.boot.routing.constant;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 路由上下文信息
 * @author tuxuchen
 * @date 2022/8/23 17:01
 */
public class RoutingContextHolder  {

  /**
   * 路由编码
   */
  public static final String ROUTING_CODE = "routingCode";

  /**
   * 使用线程的上下文存储租户信息
   */
  private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

  private RoutingContextHolder() {
    throw new UnsupportedOperationException();
  }

  /**
   * 获取上下文信息
   * @return
   */
  public static Map<String, Object> get() {
    Map<String, Object> holder = THREAD_LOCAL.get();
    if(holder == null) {
      holder = Maps.newHashMap();
      THREAD_LOCAL.set(holder);
    }
    return holder;
  }

  /**
   * 根据key获取值信息
   * @param key
   * @return
   */
  public static Object get(String key) {
    return get().get(key);
  }

  /**
   * 清空上下文
   */
  public static void clear() {
    THREAD_LOCAL.remove();
  }

  /**
   * 移除特定的值
   * @param key
   * @return
   */
  public static Object remove(String key) {
    Map<String, Object> holder = get();
    Object value = holder.remove(key);
    THREAD_LOCAL.set(holder);
    return value;
  }

  /**
   * 往上下文中设置一个值
   * @param key
   * @param value
   */
  public static void put(String key, Object value) {
    Map<String, Object> holder = get();
    holder.put(key, value);
    THREAD_LOCAL.set(holder);
  }

  /**
   * 往上下文中设置一个值集合
   * @param map
   */
  public static void putAll(Map<String, Object> map) {
    if(map != null) {
      Map<String, Object> holder = get();
      holder.putAll(map);
      THREAD_LOCAL.set(holder);
    }
  }

  /**
   * 获取路由编码
   * @return
   */
  public static String getRoutingCode() {
    return (String) get(ROUTING_CODE);
  }

  /**
   * 设置路由编码
   * @param routingCode
   */
  public static void setRoutingCode(String routingCode) {
    put(ROUTING_CODE, routingCode);
  }

}
