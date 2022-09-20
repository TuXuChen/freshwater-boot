package org.freshwater.boot.generator;

/**
 * 代码生成器
 * @author tuxuchen
 * @date 2022/7/22 16:07
 */
public class CodeGeneratorMain {

  public static void main(String[] args) {
    CodeGeneratorMain generatorMain = new CodeGeneratorMain();
    GeneratorPojo generatorConfig = generatorMain.loadGeneratorConfig();
    CodeGenerator generator = new CodeGenerator(generatorConfig);
    generator.generate();
  }

  public GeneratorPojo loadGeneratorConfig() {
    GeneratorPojo generatorPojo = new GeneratorPojo();
    generatorPojo
        // 数据库地址
        .setDatasourceUrl("jdbc:mysql://localhost:3306/freshwater?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true")
        // 数据库用户
        .setUsername("root")
        // 数据库密码
        .setPassword("root")
        // 生成地址
        .setProjectSrcPath("freshwater-routing/src/main/java")
        // 注释在类上的作者
        .setAuthor("tuxuchen")
        // 基本包路径
        .setRootPackage("org.freshwater.boot.routing")
        // 表前缀,多个用逗号分割
        .setTablePrefixs("freshwater_")
        // 包含的表名,与excludeTables二选一
        .setIncludeTables("freshwater_dynamic_datasource")
        // 父类操作实体的字段
        .setSuperOpEntityColumns("creator,creator_name,create_time,modifier,modifier_name,modify_time")
        // IdEntity的路径
        .setSuperIdEntityClassPackage("org.freshwater.boot.common.entity.UuidEntity")
        // OpUuidEntity的路径
        .setSuperOpEntityClassPackage("org.freshwater.boot.common.entity.OpUuidEntity")
        // EntityUtils 类的全路径
        .setEntityUtilsClassPackage("org.freshwater.boot.utils.EntityUtils")
        // 是否覆盖以前的文件
        .setFileOverride(false);
    return generatorPojo;
  }

}
