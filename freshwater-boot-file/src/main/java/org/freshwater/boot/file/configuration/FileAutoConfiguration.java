package org.freshwater.boot.file.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 文件服务的自动配置类
 * @author tuxuchen
 * @date 2022/8/3 14:05
 */
@Configuration
@ComponentScan("org.freshwater.boot.file")
@MapperScan("org.freshwater.boot.file.mapper")
public class FileAutoConfiguration {
}
