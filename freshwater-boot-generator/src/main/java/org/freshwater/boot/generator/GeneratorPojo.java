package org.freshwater.boot.generator;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 生成器的相关参数
 * @author tuxuchen
 * @date 2022/7/22 14:35
 */
@Data
@Accessors(chain = true)
public class GeneratorPojo {

  /**
   * 作者,开发者名字
   */
  String author;

  /**
   * 基础包路径
   */
  String rootPackage = "";

  /**
   * java代码路径
   */
  String projectSrcPath = "src/main/java";

  /**
   * 数据源地址
   */
  String datasourceUrl = "";

  /**
   * 数据库用户
   */
  String username;

  /**
   * 数据库密码
   */
  String password;

  /**
   * jdbc驱动
   */
  String driver = "com.mysql.cj.jdbc.Driver";

  /**
   * 表前缀,多个用逗号分割
   */
  String tablePrefixs = "";

  /**
   * 排除的表名,多个以逗号分割
   */
  String excludeTables = "";

  /**
   * 包含的表名,多个以逗号分割
   */
  String includeTables = "";

  /**
   * 上级操作entity的字段集合,多个字段用逗号分割
   */
  String superOpEntityColumns = "creator,creator_name,create_time,modifier,modifier_name,modify_time";

  /**
   * 上级id entity的全路径,如:com.wd.wecode.common.entity.UuidEntity
   */
  String superIdEntityClassPackage = "org.freshwater.boot.common.entity.UuidEntity";

  /**
   * 上级opID entity的全路径,如:com.wd.wecode.common.entity.OpUuidEntity
   */
  String superOpEntityClassPackage = "org.freshwater.boot.common.entity.OpUuidEntity";

  /**
   * EntityUtils 类的全路径,如:com.wd.wecode.cloud.rbac.util.EntityUtils
   */
  String entityUtilsClassPackage = "org.freshwater.boot.utils.EntityUtils";

  /**
   * 是否覆盖文件
   */
  Boolean fileOverride = false;

}
