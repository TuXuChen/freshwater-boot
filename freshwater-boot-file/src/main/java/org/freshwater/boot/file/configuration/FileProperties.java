package org.freshwater.boot.file.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件配置配置信息类
 * @author tuxuchen
 * @date 2022/8/3 14:12
 */
@Data
@Component
@EnableConfigurationProperties({FileProperties.class})
@ConfigurationProperties(prefix = "freshwater.file")
public class FileProperties {

  /**
   * 本地磁盘的文件根目录
   */
  protected String root;

  /**
   * 最大文件大小,单位为M, 默认10
   */
  protected Integer max;

  /**
   * 文件白名单,如:txt,png等,多个用逗号(,)隔开
   */
  protected String[] white;

}
