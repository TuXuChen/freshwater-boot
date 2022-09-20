package org.freshwater.boot.rbac.utils;

import org.freshwater.boot.common.entity.OpUuidEntity;
import org.freshwater.boot.rbac.entity.UserEntity;

import java.time.LocalDateTime;

/**
 * 实体工具类
 * @author tuxuchen
 * @date 2022/7/22 15:35
 */
public class EntityUtils {

  private EntityUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 初始化创建人信息
   * @param entity
   */
  public static void initCreatorInfo(OpUuidEntity entity) {
    UserEntity loginUser = SecurityUtils.getCurrentUser();
    LocalDateTime now = LocalDateTime.now();
    entity.setCreator(loginUser.getUserAccount());
    entity.setCreatorName(loginUser.getRealName());
    entity.setCreateTime(now);
    entity.setModifier(loginUser.getUserAccount());
    entity.setModifierName(loginUser.getRealName());
    entity.setModifyTime(now);
  }

  /**
   * 初始化修改人信息
   * @param entity
   */
  public static void initModifierInfo(OpUuidEntity entity) {
    UserEntity loginUser = SecurityUtils.getCurrentUser();
    LocalDateTime now = LocalDateTime.now();
    entity.setModifier(loginUser.getUserAccount());
    entity.setModifierName(loginUser.getRealName());
    entity.setModifyTime(now);
  }

}
