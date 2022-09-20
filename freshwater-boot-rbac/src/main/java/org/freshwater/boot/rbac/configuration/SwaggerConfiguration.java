package org.freshwater.boot.rbac.configuration;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 接口文档配置
 * @author tuxuchen
 * @date 2022/7/25 16:38
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .enable(true)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
        .paths(PathSelectors.any())
        .build();
  }

  /**
   * API基础信息定义
   * @return
   */
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("接口文档")
        .description("在知识的海洋里,我竟然是一条淡水鱼")
        .termsOfServiceUrl("https://www.baidu.com/")
        .license("tu_xu_chen")
        .version("1.0")
        .build();
  }

}
