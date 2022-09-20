package org.freshwater.boot.file.service.internal;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.freshwater.boot.common.utils.FileUtils;
import org.freshwater.boot.file.configuration.FileProperties;
import org.freshwater.boot.file.service.FileService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * 文件服务的抽象信息
 * 主要将一些文件服务的公共功能抽象到当前类中
 * 业务系统重新实现FileService可以继承当前服务
 * @author tuxuchen
 * @date 2022/8/3 14:08
 */
public abstract class AbstractFileService implements FileService {

  @Autowired
  protected FileProperties fileProperties;

  /**
   * 最大文件大小,单位为Byte
   */
  protected long maxByte;

  /**
   * 文件白名单
   */
  protected Set<String> whiteSet;

  @PostConstruct
  public void init() {
    maxByte = fileProperties.getMax() * 1024L * 1024L;
    whiteSet = Sets.newHashSet(fileProperties.getWhite());
  }

  @Override
  public void valid(String filename, long size) {
    Validate.notBlank(filename, "文件名不能为空");
    Validate.isTrue(size <= maxByte, "文件大小不能超过%sM", fileProperties.getMax());
    String suffix = FileUtils.getFileSuffix(filename);
    Validate.isTrue(whiteSet.stream().anyMatch(s -> s.equalsIgnoreCase(suffix)), "不支持的文件格式:%s", suffix);
  }

  /**
   * 文件保存前校验
   * @param path
   * @param filename
   */
  protected void saveValidation(String path, String filename) {
    Validate.notBlank(filename, "文件名不能为空");
    Validate.matchesPattern(path, "/[a-z|A-Z|0-9|_|/|\\.]+", "文件路径必须以/开头");
    String suffix = FileUtils.getFileSuffix(filename);
    Validate.isTrue(whiteSet.stream().anyMatch(s -> s.equalsIgnoreCase(suffix)), "不支持的文件格式:%s", suffix);
  }

  /**
   * 文件保存前校验
   * @param path
   * @param filename
   * @param content
   */
  protected void saveValidation(String path, String filename, byte[] content) {
    this.saveValidation(path, filename);
    Validate.notNull(content, "文件内容不能为空");
    int fileSize = content.length;
    Validate.isTrue(fileSize <= maxByte, "文件大小不能超过%sM", fileProperties.getMax());
  }

  /**
   * 文件保存前校验
   * @param path
   * @param filename
   * @param is
   */
  protected void saveValidation(String path, String filename, InputStream is) throws IOException {
    this.saveValidation(path, filename);
    Validate.notNull(is, "文件输入流不能为空");
    int fileSize = is.available();
    Validate.isTrue(fileSize <= maxByte, "文件大小不能超过%sM", fileProperties.getMax());
  }
}
