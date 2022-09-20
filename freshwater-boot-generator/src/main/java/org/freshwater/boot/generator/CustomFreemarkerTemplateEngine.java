package org.freshwater.boot.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * 自定义模板引擎
 * @author tuxuchen
 * @date 2022/7/22 14:53
 */
public class CustomFreemarkerTemplateEngine extends AbstractTemplateEngine {

  private Configuration configuration;

  @Override
  public CustomFreemarkerTemplateEngine init(ConfigBuilder configBuilder) {
    configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    configuration.setDefaultEncoding(ConstVal.UTF8);
    configuration.setClassForTemplateLoading(CustomFreemarkerTemplateEngine.class, StringPool.SLASH);
    return this;
  }


  @Override
  public void writer(Map<String, Object> objectMap, String templatePath, File outputFile) throws Exception {
    Template template = configuration.getTemplate(templatePath);
    objectMap.put("randomLong", new RandomLongMethodModel());
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(outputFile);
      template.process(objectMap, new OutputStreamWriter(fileOutputStream, ConstVal.UTF8));
    } finally  {
      if(fileOutputStream != null) {
        fileOutputStream.close();
      }
    }
  }


  @Override
  public String templateFilePath(String filePath) {
    return filePath + ".ftl";
  }
}
