package org.freshwater.boot.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 主启动类
 * @author tuxuchen
 * @date 2022/8/26 17:47
 */
@SpringBootApplication
@ComponentScan("org.freshwater.boot")
@MapperScan("org.freshwater.boot.**.mapper")
public class FreshwaterApplication {

  public static void main(String[] args) {
    SpringApplication.run(FreshwaterApplication.class, args);
  }

}
