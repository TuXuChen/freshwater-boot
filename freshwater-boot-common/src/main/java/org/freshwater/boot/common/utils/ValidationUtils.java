package org.freshwater.boot.common.utils;

import org.apache.commons.lang3.Validate;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * 数据校验
 * @author tuxuchen
 * @date 2022/8/29 10:31
 */
public class ValidationUtils {

  /**
   * 校验器
   */
  private static Validator validator = ((HibernateValidatorConfiguration) ((HibernateValidatorConfiguration) Validation.byProvider(HibernateValidator.class).configure()).failFast(true)).buildValidatorFactory().getValidator();

  private ValidationUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 校验对象, 进行快速错误校验,遇到错误就会结束校验
   * 如果传入是一个集合, 则会遍历校验集合中的每一个对象
   * @param o
   */
  public static void validate(Object o, Class<?>... groups) {
    Validate.notNull(o, "对象不能为空", new Object[0]);
    if (Collection.class.isAssignableFrom(o.getClass())) {
      Collection collection = (Collection) o;
      Iterator var3 = collection.iterator();
      while (var3.hasNext()) {
        Object obj = var3.next();
        Set<ConstraintViolation<Object>> validations = validator.validate(obj, groups);
        if (!CollectionUtils.isEmpty(validations)) {
          ConstraintViolation<Object> validation = (ConstraintViolation) validations.iterator().next();
          throw new IllegalArgumentException(validation.getMessage());
        }
      }
    } else {
      Set<ConstraintViolation<Object>> validations = validator.validate(o, groups);
      if (!CollectionUtils.isEmpty(validations)) {
        ConstraintViolation<Object> validation = (ConstraintViolation) validations.iterator().next();
        throw new IllegalArgumentException(validation.getMessage());
      }
    }
  }
}
