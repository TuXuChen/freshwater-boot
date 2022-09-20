package org.freshwater.boot.generator;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;
import java.util.Random;

/**
 * 随机数
 * @author tuxuchen
 * @date 2022/7/22 14:54
 */
public class RandomLongMethodModel implements TemplateMethodModelEx {
  @Override
  public Object exec(List arguments) throws TemplateModelException {
    Random random = new Random();
    return random.nextLong();
  }
}
