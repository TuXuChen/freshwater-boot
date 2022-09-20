package org.freshwater.boot.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 资源加载工具类
 * @author tuxuchen
 * @date 2022/8/29 10:28
 */
public class ResourceUtils {

  /**
   * 换行符
   */
  public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

  private ResourceUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 从指定的resource文件中读取文件
   * @param fileName
   * @return
   */
  public static String read(String fileName) {
    if(StringUtils.isBlank(fileName)) {
      return StringUtils.EMPTY;
    }
    return read(fileName, ResourceUtils.class);
  }

  /**
   * 读取资源二进制文件
   * @param fileName
   * @return
   */
  public static byte[] readBytes(String fileName) {
    if(StringUtils.isBlank(fileName)) {
      return new byte[0];
    }
    return readBytes(fileName, ResourceUtils.class);
  }

  /**
   * 读取资源二进制文件
   * @param fileName
   * @param clazz
   * @return
   */
  public static byte[] readBytes(String fileName, Class<?> clazz) {
    Validate.notNull(clazz, "参考类不能为空");
    if(StringUtils.isBlank(fileName)) {
      return new byte[0];
    }
    try (InputStream is = clazz.getClassLoader().getResourceAsStream(fileName)) {
      return FileCopyUtils.copyToByteArray(is);
    } catch (IOException e) {
      throw new RuntimeException("读取资源文件错误");
    }
  }

  /**
   * 从指定的resource文件中读取文件
   * @param fileName
   * @return
   */
  public static String read(String fileName, Class<?> clazz) {
    Validate.notNull(clazz, "参考类不能为空");
    if(StringUtils.isBlank(fileName)) {
      return StringUtils.EMPTY;
    }
    try (InputStream inputStream = clazz.getClassLoader().getResourceAsStream(fileName);
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      StringBuilder sb = new StringBuilder();
      String line = bufferedReader.readLine();
      while (line != null){
        sb.append(line).append(LINE_SEPARATOR);
        line = bufferedReader.readLine();
      }
      return sb.toString();
    } catch (IOException e) {
      throw new RuntimeException("读取资源文件错误");
    }
  }
}
