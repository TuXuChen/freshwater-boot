package org.freshwater.boot.file.service;

import java.io.InputStream;

/**
 * 文件服务接口定义
 * 统一的文件服务接口定义,在不同的系统中可能实现的文件存储方式不同,
 * 那么需要各个系统只需要实现这个文件服务接口
 * @author tuxuchen
 * @since 2022-08-03 14:03
 */
public interface FileService {

  /**
   * 保存文件, 需要传入文件的二进制内容,
   * @param path 文件的相对路径 如: /images, 可以为空
   * @param filename  存储的文件名
   * @param content 文件二进制内容
   */
  void save(String path, String filename, byte[] content);

  /**
   * 保存文件, 需要传入文件的二进制内容,
   * @param path 文件的相对路径 如: /images, 可以为空
   * @param filename  存储的文件名
   * @param is 文件输入流
   */
  void save(String path, String filename, InputStream is);

  /**
   * 读取文件的二进制内容,如果读取不到,则返回null
   * @param path 件的相对路径 如: /images, 可以为空字符串,但不能为null
   * @param filename 存储的文件名
   * @return
   */
  byte[] read(String path, String filename);

  /**
   * 删除指定的文件
   * @param path 件的相对路径 如: /images, 可以为空字符串,但不能为null
   * @param filename 存储的文件名
   */
  void delete(String path, String filename);

  /**
   * 对保存文件的校验
   * @param filename
   * @param size
   */
  void valid(String filename, long size);
}
