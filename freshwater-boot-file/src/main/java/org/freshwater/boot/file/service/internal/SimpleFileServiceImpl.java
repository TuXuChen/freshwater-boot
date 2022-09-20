package org.freshwater.boot.file.service.internal;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基本的文件服务实现,采用本地磁盘存储文件
 * 使用该服务需要在配置文件中指定一个本地磁盘的文件夹路径:wecode.file.root,用于存储保存的文件
 * @author tuxuchen
 * @date 2022/8/3 14:21
 */
@Slf4j
public class SimpleFileServiceImpl extends AbstractFileService {

  @Override
  public void save(String path, String filename, byte[] content) {
    path = StringUtils.defaultIfBlank(path, "/");
    this.saveValidation(path, filename, content);
    String absPath = StringUtils.join(fileProperties.getRoot(), path);
    File absPathFile = new File(absPath);
    if(!absPathFile.exists()) {
      Validate.isTrue(absPathFile.mkdirs(), "创建文件夹[%s]失败,可能是没有操作权限,请检查文件目录!", absPath);
    }
    String absFilename = StringUtils.join(absPath, "/", filename);
    try (FileOutputStream fos = new FileOutputStream(absFilename);
         ByteArrayInputStream bais = new ByteArrayInputStream(content)) {
      FileCopyUtils.copy(bais, fos);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("保存文件失败", e);
    }
  }

  @Override
  public void save(String path, String filename, InputStream is) {
    path = StringUtils.defaultIfBlank(path, "/");
    try {
      this.saveValidation(path, filename, is);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("保存文件失败", e);
    }
    String absPath = StringUtils.join(fileProperties.getRoot(), path);
    File absPathFile = new File(absPath);
    if(!absPathFile.exists()) {
      Validate.isTrue(absPathFile.mkdirs(), "创建文件夹[%s]失败,可能是没有操作权限,请检查文件目录!", absPath);
    }
    String absFilename = StringUtils.join(absPath, "/", filename);
    try (FileOutputStream fos = new FileOutputStream(absFilename)) {
      FileCopyUtils.copy(is, fos);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("保存文件失败", e);
    }
  }

  @Override
  public byte[] read(String path, String filename) {
    if(StringUtils.isBlank(filename)) {
      return null;
    }
    path = StringUtils.defaultIfBlank(path, "/");
    String absFilename = StringUtils.join(fileProperties.getRoot(), path, "/", filename);
    File file = new File(absFilename);
    if(!file.exists()) {
      return null;
    }
    try {
      return FileCopyUtils.copyToByteArray(file);
    } catch (IOException e) {
      log.error("读取文件失败!!!");
      log.error(e.getMessage(), e);
      return new byte[0];
    }
  }

  @Override
  public void delete(String path, String filename) {
    Validate.notBlank(filename, "文件名不能为空");
    path = StringUtils.defaultIfBlank(path, "/");
    String absFilename = StringUtils.join(fileProperties.getRoot(), path, "/", filename);
    File file = new File(absFilename);
    if(!file.exists()) {
      return;
    }
    Validate.isTrue(file.delete(), "删除文件[%s]失败, 请检查文件操作权限");
  }

}
