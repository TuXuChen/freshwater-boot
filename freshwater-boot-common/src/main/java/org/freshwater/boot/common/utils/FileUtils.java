package org.freshwater.boot.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

/**
 * 文件先关的工具类
 * 如果文件的简单处理,文件格式获取等
 * @author tuxuchen
 * @date 2022/8/29 10:25
 */
public class FileUtils {

  private FileUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 获取文件扩展名
   * @param filename
   * @return
   */
  public static String getFileSuffix(String filename) {
    Validate.notBlank(filename, "文件名不能为空");
    int index = StringUtils.lastIndexOf(filename, ".");
    if(index < 0) {
      return StringUtils.EMPTY;
    }
    String suffix = filename.substring(index + 1);
    suffix = ObjectUtils.defaultIfNull(suffix, StringUtils.EMPTY);
    return suffix;
  }

  /**
   * 根据文件名获取类型
   * @param filename
   * @return
   */
  public static String getContentTypeByFilename(String filename) {
    String suffix = getFileSuffix(filename);
    return getContentTypeBySuffix(suffix);
  }

  /**
   * 根据后缀获取资源类型
   * @param suffix
   * @return
   */
  public static String getContentTypeBySuffix(String suffix) {
    if(StringUtils.isBlank(suffix)) {
      return APPLICATION_OCTET_STREAM_VALUE;
    }
    if(StringUtils.equalsAnyIgnoreCase(suffix, "avi", "mpg", "rm", "rmvb", "wmv", "mp4", "flv", "mkv")) {
      return "video/mp4";
    }
    if ("bmp".equalsIgnoreCase(suffix)) {
      return "image/bmp";
    }
    if ("gif".equalsIgnoreCase(suffix)) {
      return IMAGE_GIF_VALUE;
    }
    if ("jpeg".equalsIgnoreCase(suffix) ||
        "jpg".equalsIgnoreCase(suffix) ||
        "png".equalsIgnoreCase(suffix)) {
      return IMAGE_JPEG_VALUE;
    }
    if ("html".equalsIgnoreCase(suffix)) {
      return "text/html;charset=UTF-8";
    }
    if ("txt".equalsIgnoreCase(suffix)) {
      return "text/plain;charset=UTF-8";
    }
    if ("vsd".equalsIgnoreCase(suffix)) {
      return "application/vnd.visio";
    }
    if ("pptx".equalsIgnoreCase(suffix) ||
        "ppt".equalsIgnoreCase(suffix)) {
      return "application/vnd.ms-powerpoint";
    }
    if ("docx".equalsIgnoreCase(suffix) ||
        "doc".equalsIgnoreCase(suffix)) {
      return "application/msword";
    }
    if ("xml".equalsIgnoreCase(suffix)) {
      return "text/xml;charset=UTF-8";
    }
    return APPLICATION_OCTET_STREAM_VALUE;
  }
}
