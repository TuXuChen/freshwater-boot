package org.freshwater.boot.file.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.freshwater.boot.file.service.FileService;
import org.freshwater.boot.file.service.FileUploadService;
import org.freshwater.boot.file.service.internal.FileUploadServiceImpl;
import org.freshwater.boot.file.service.internal.SimpleFileServiceImpl;

/**
 * 文件服务的配置信息
 * @author tuxuchen
 * @date 2022/8/3 14:28
 */
@Configuration
public class FileConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public FileService getFileService() {
    return new SimpleFileServiceImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public FileUploadService getFileUploadService() {
    return new FileUploadServiceImpl();
  }

}
