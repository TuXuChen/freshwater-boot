package org.freshwater.boot.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

/**
 * servlet的输出工具类
 * @author tuxuchen
 * @date 2022/8/29 10:29
 */
@Slf4j
public class ServletResponseUtils {

  private ServletResponseUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 输出json数据
   * @param response
   * @param result
   * @throws IOException
   */
  public static void writeJson(HttpServletResponse response, Object result) throws IOException {
    String content = "";
    if(result != null) {
      content = JsonUtils.obj2JsonString(result);
    }
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(content);
  }

  /**
   * 输出图片
   * @param request
   * @param response
   * @param bytes
   * @param filename
   */
  public static void writeImage(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String filename) {
    writeFile(request, response, bytes, filename, IMAGE_JPEG_VALUE, false);
  }

  /**
   * 输出文件流
   * @param request
   * @param response
   * @param bytes
   * @param filename
   * @param contentType
   */
  private static void writeFile(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String filename, String contentType, boolean requiredFilename) {
    String currentFileName = null;
    if(StringUtils.isBlank(filename) && requiredFilename) {
      currentFileName = "temp";
    }
    try {
      if(StringUtils.isNotBlank(filename)) {
        currentFileName = URLEncoder.encode(filename, StandardCharsets.UTF_8.name());
      }
    } catch(UnsupportedEncodingException e) {
      log.error(e.getMessage() , e);
      currentFileName = "temp";
    }

    response.setContentType(contentType);
    response.addHeader("Accept-Ranges", "bytes");
    response.addHeader("Content-Length", String.valueOf(bytes.length));
    if(StringUtils.isNotBlank(currentFileName)) {
      // 设置文件名
      response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
      response.addHeader("Content-Disposition", "attachment;fileName=" + encodeFilename(request, currentFileName));
    }
    OutputStream out;
    try {
      out = response.getOutputStream();
      out.write(bytes);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new IllegalArgumentException(e.getMessage() , e);
    }
  }

  /**
   * 设置流文件信息
   * @param request
   * @param response
   * @param filename
   */
  public static void setStreamFile(HttpServletRequest request, HttpServletResponse response, String filename) {
    filename = StringUtils.defaultIfBlank(filename, "response.bin");
    String contentType = FileUtils.getContentTypeByFilename(filename);
    response.setContentType(contentType);
    response.addHeader("Accept-Ranges", "bytes");
    // 设置文件名
    response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
    response.addHeader("Content-Disposition", "attachment;fileName=" + encodeFilename(request, filename));
  }

  /**
   * 输出一个文件流
   * @param response
   * @param bytes
   * @param filename
   */
  public static void writeFile(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String filename) {
    String contentType = FileUtils.getContentTypeByFilename(filename);
    writeFile(request, response, bytes, filename, contentType, true);
  }

  /**
   * 输出视频视频
   * @param request
   * @param response
   * @param bytes
   * @param fileName
   */
  public static void writeVideo(HttpServletRequest request, HttpServletResponse response, byte[] bytes, String fileName) {
    String range = request.getHeader("Range");
    int start = 0, end = 0;
    long contentLength = bytes.length;
    if(range != null && range.startsWith("bytes=")){
      String[] values = range.split("=")[1].split("-");
      start = Integer.parseInt(values[0]);
      if(values.length > 1){
        end = Integer.parseInt(values[1]);
      }
    }
    int requestSize = 0;
    if(end != 0 && end > start){
      requestSize = end - start + 1;
    } else {
      requestSize = Integer.MAX_VALUE;
    }

    response.setContentType("video/mp4");
    response.setHeader("Accept-Ranges", "bytes");
    response.setHeader("ETag", fileName);
    response.setHeader("Last-Modified", new Date().toString());
    //第一次请求只返回content length来让客户端请求多次实际数据
    if(range == null){
      response.setHeader("Content-length", String.valueOf(contentLength));
    }else{
      //以后的多次以断点续传的方式来返回视频数据
      response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);// 206
      long requestStart = 0, requestEnd = 0;
      String[] ranges = range.split("=");
      if(ranges.length > 1){
        String[] rangeDatas = ranges[1].split("-");
        requestStart = Integer.parseInt(rangeDatas[0]);
        if(rangeDatas.length > 1){
          requestEnd = Integer.parseInt(rangeDatas[1]);
        }
      }
      long length;
      if(requestEnd > 0){
        length = requestEnd - requestStart + 1;
        response.setHeader("Content-length", "" + length);
        response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
      }else{
        length = bytes.length - requestStart;
        response.setHeader("Content-length", "" + length);
        response.setHeader("Content-Range", "bytes "+ requestStart + "-" + (contentLength - 1) + "/" + contentLength);
      }
    }
    int needSize = requestSize;
    OutputStream out;
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes, start, (int) (contentLength - start))) {
      out = response.getOutputStream();
      byte[] buffer = new byte[4096];
      while (needSize > 0) {
        int len = bais.read(buffer);
        if(needSize < buffer.length) {
          out.write(buffer, 0, needSize);
        } else {
          out.write(buffer, 0, len);
          if (len < buffer.length) {
            break;
          }
        }
        needSize -= buffer.length;
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new IllegalArgumentException(e.getMessage() , e);
    }
  }

  /**
   * 编译文件名，解决中文乱码问题
   * @param fileName
   * @return
   */
  public static String encodeFilename(HttpServletRequest request, String fileName){
    String agent = request.getHeader("User-Agent");
    if(StringUtils.isBlank(agent)) {
      try {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
      } catch (UnsupportedEncodingException e) {
        log.warn(e.getMessage(), e);
      }
    }
    // 如果条件成立，说明是Firefox或者Safari浏览器
    if(agent.contains("Firefox") || agent.contains("Safari")){
      fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    } else if(agent.contains("Chrome")) {
      try {
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
      } catch (UnsupportedEncodingException e) {
        log.warn(e.getMessage(), e);
      }
    }
    return fileName;
  }
}
