package org.freshwater.boot.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.TimeZone;

/**
 * json处理工具类
 * @author tuxuchen
 * @date 2022/8/29 10:18
 */
public class JsonUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

  private static ObjectMapper objectMapper = new ObjectMapper();

  private JsonUtils() {
    throw new IllegalStateException("静态工具类不能实例化");
  }

  static {
    objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    // 忽略json字符串中不识别的属性
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
    objectMapper.registerModule(new JavaTimeModule());
  }

  /**
   * json转对象，将json字符串转换成指定的对象,此方法主要用户转换单个对象
   *
   * @param json
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T json2Obj(String json, Class<T> clazz) {
    if (StringUtils.isBlank(json)) {
      return null;
    }
    try {
      return objectMapper.readValue(json, clazz);
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException(String.format("json转换失败，请检查json数据：%s", json));
    }
  }

  /**
   * json转对象，将json字符串转换成指定的对象，此方法主要用于转换目标类中有范型定义的类
   * 如转换list集合时：new TypeReference<List<T>>(){}
   *
   * @param json
   * @param typeReference new TypeReference<List<T>>(){}
   * @param <T>
   * @return
   */
  public static <T> T json2Obj(String json, com.fasterxml.jackson.core.type.TypeReference<T> typeReference) {
    if (StringUtils.isBlank(json)) {
      return null;
    }
    T t;
    try {
      t = objectMapper.readValue(json, typeReference);
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException(String.format("json转换失败，请检查json数据：%s", json));
    }
    return t;
  }

  /**
   * 对象转换，将输入的对象转换为指定的对象类型，此方法主要用于单个对象的转换
   *
   * @param obj
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T convert(Object obj, Class<T> clazz) {
    if (obj == null) {
      return null;
    }
    return objectMapper.convertValue(obj, clazz);
  }

  /**
   * 对象转换，输入的对象转换成目标类的对象，此方法主要用于转换目标类中有范型定义的类
   * 如转换list集合时：new TypeReference<List<T>>(){}
   *
   * @param obj
   * @param typeReference typeReference new TypeReference<List<T>>(){}
   * @param <T>
   * @return
   */
  public static <T> T convert(Object obj, TypeReference<T> typeReference) {
    if (obj == null) {
      return null;
    }
    return objectMapper.convertValue(obj, typeReference);
  }

  /**
   * 对象转换，输入的对象转换成目标类的对象，此方法用于转换集合类，
   * 在只有集合的class和集合元素的class时可以用此方法
   * 使用示例：convert(obj, List.class, String.class)
   *
   * @param obj             源对象
   * @param collectionClass 目标集合类
   * @param elementClass    目标集合元素类
   * @param <T>
   * @param <E>
   * @return
   */
  public static <T extends Collection<E>, E> T convert(Object obj, Class<T> collectionClass, Class<E> elementClass) {
    if (obj == null) {
      return null;
    }
    CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    return objectMapper.convertValue(obj, collectionType);
  }

  /**
   * 对象转换，输入的对象转换成目标类的对象，此方法用于转换集合类，
   * parameterizedType参数为参数化集合的类描述，如通过反射得到的List<String>的类
   * parameterizedType一般从java.lang.reflect.Parameter中获取
   *
   * @param obj               源对象
   * @param parameterizedType 参数化集合类型
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T extends Collection<E>, E> T convert(Object obj, ParameterizedType parameterizedType) {
    if (obj == null) {
      return null;
    }
    Validate.notNull(parameterizedType, "参数化集合类型不能为空!!");
    String rawTypeName = parameterizedType.getRawType().getTypeName();
    Class<T> collectionClass;
    try {
      collectionClass = (Class<T>) Class.forName(rawTypeName);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(String.format("未找到类：%s", rawTypeName));
    }
    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
    Validate.notEmpty(actualTypeArguments, "目标集合类非集合类，请检查！！");
    String elementTypeName = actualTypeArguments[0].getTypeName();
    Class<?> elementClass;
    try {
      elementClass = Class.forName(elementTypeName);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(String.format("未找到类：%s", elementTypeName));
    }
    CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    return objectMapper.convertValue(obj, collectionType);
  }

  /**
   * 将一个java对象输出为json字符串，如果传入的对象为null则返回null，
   * 此方法用的jackson的json工具
   *
   * @param obj
   * @return
   */
  public static String obj2JsonString(Object obj) {
    if (obj == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new IllegalArgumentException("对象输出json错误");
    }
  }
}
