package org.freshwater.boot.file.service;

import org.springframework.web.multipart.MultipartFile;
import org.freshwater.boot.file.entity.FileEntity;

import java.util.List;

/**
 * 文件上传服务,
 * 本服务主要为了给前端提供文件上传的接口服务
 * 通过本服务上传的文件,会将文件信息保存的数据库中
 * @author tuxuchen
 * @date 2022/8/3 14:24
 */
public interface FileUploadService {

  /**
   * 上传文件,支持多个文件上传
   * @param subSystem 子系统名称,主要是为上传的文件做分类处理,通常会拼接在文件路径的最前面
   * @param effective 文件有效天数,如果不传或时间小于等于0,则表示永久有效
   * @param files 文件
   * @return
   */
  List<FileEntity> upload(String subSystem, Integer effective, MultipartFile[] files);

  /**
   * 基于id查询文件信息
   * @param ids
   * @return
   */
  List<FileEntity> findByIds(List<String> ids);

  /**
   * 读取文件的相关信息
   * @param path
   * @param filename
   * @return
   */
  FileEntity findByPathAndFilename(String path, String filename);

  /**
   * 根据保存的文件路径和文件名删除
   * @param path
   * @param filename
   */
  void deleteFile(String path, String filename);

}
