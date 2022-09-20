package org.freshwater.boot.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代码生成器插件
 * @author tuxuchen
 * @date 2022/7/22 14:55
 */
public class CodeGenerator {

  private GeneratorPojo generatorConfig;

  public CodeGenerator(GeneratorPojo generatorPojo) {
    this.generatorConfig = generatorPojo;
  }

  @SneakyThrows(value = ClassNotFoundException.class)
  public void generate() {
    String projectPath = System.getProperty("user.dir");
    System.out.println("代码生成目录:" + projectPath);
    String projectSrcPath = generatorConfig.projectSrcPath;
    String rootPackage = generatorConfig.rootPackage;
    String author = generatorConfig.author;
    String datasourceUrl = generatorConfig.datasourceUrl;
    String username = generatorConfig.username;
    String password = generatorConfig.password;
    String driver = generatorConfig.driver;
    String tablePrefixs = generatorConfig.tablePrefixs;
    String excludeTables = generatorConfig.excludeTables;
    String includeTables = generatorConfig.includeTables;
    String superOpEntityColumns = generatorConfig.superOpEntityColumns;
    String superIdEntityClassPackage = generatorConfig.superIdEntityClassPackage;
    String superOpEntityClassPackage = generatorConfig.superOpEntityClassPackage;
    String entityUtilsClassPackage = generatorConfig.entityUtilsClassPackage;
    Boolean fileOverride = generatorConfig.fileOverride;
    Validate.notBlank(projectSrcPath, "java代码路径必须填写");
    Validate.notBlank(author, "开发者名字必须填写");
    Validate.notBlank(datasourceUrl, "数据库地址必须填写");
    Validate.notBlank(username, "数据库用户必须填写");
    Validate.notBlank(password, "数据库密码必须填写");
    Validate.notBlank(driver, "jdbc驱动必须填写");
    GlobalConfig.Builder globalConfigBuilder = new GlobalConfig.Builder()
        .outputDir(projectPath + "/" + projectSrcPath)
        .openDir(false)
        .author(author)
        .enableSwagger();
    if(fileOverride) {
      globalConfigBuilder.fileOverride();
    }
    GlobalConfig globalConfig = globalConfigBuilder.build();
    DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(datasourceUrl, username, password)
        .build();
    Class.forName(driver);
    TemplateConfig templateConfig = new TemplateConfig.Builder()
        .entity("/entity.java")
        .service("/service.java", "serviceImpl.java")
        .controller("/controller.java")
        .mapper("/mapper.java")
        .mapperXml("/mapper.xml")
        .build();
    tablePrefixs = ObjectUtils.defaultIfNull(tablePrefixs, "");
    excludeTables = ObjectUtils.defaultIfNull(excludeTables, "");
    includeTables = ObjectUtils.defaultIfNull(includeTables, "");

    String[] currentTablePrefixs = tablePrefixs.split(",");
    String[] currentSuperOpEntityColumns = superOpEntityColumns.split(",");
    String[] currentExcludeTables = null;
    if(StringUtils.isNotBlank(excludeTables)) {
      currentExcludeTables = excludeTables.split(",");
    }
    String[] currentIncludeTables = null;
    if(StringUtils.isNotBlank(includeTables)) {
      currentIncludeTables = includeTables.split(",");
    }

    StrategyConfig.Builder strategyConfigBuilder = new StrategyConfig.Builder()
        .addTablePrefix(currentTablePrefixs)
        .enableCapitalMode();
    if(currentExcludeTables != null && currentExcludeTables.length > 0) {
      strategyConfigBuilder.addExclude(currentExcludeTables);
    }
    if(currentIncludeTables != null && currentIncludeTables.length > 0) {
      strategyConfigBuilder.addInclude(currentIncludeTables);
    }
    StrategyConfig strategyConfig = strategyConfigBuilder.build();

    strategyConfig.entityBuilder()
        .enableLombok()
        .enableSerialVersionUID()
        .enableTableFieldAnnotation()
        //.superClass(UuidEntity.class)
        .convertFileName(entityName -> entityName + "Entity")
        .naming(NamingStrategy.underline_to_camel)
        .columnNaming(NamingStrategy.underline_to_camel);
    strategyConfig.mapperBuilder()
        .enableBaseResultMap()
        .enableBaseColumnList();
    strategyConfig.serviceBuilder()
        .convertServiceFileName(entityName -> entityName + ConstVal.SERVICE);
    strategyConfig.controllerBuilder()
        .enableRestStyle()
        .enableHyphenStyle();

    PackageConfig packageConfig = new PackageConfig.Builder()
        .parent(rootPackage)
        .entity("entity")
        .controller("controller")
        .service("service")
        .serviceImpl("service.internal")
        .mapper("mapper")
        .xml("mapper.xml")
        .build();

    String entityUtilsClass = "";
    if(StringUtils.isNotBlank(entityUtilsClassPackage)) {
      String[] split = entityUtilsClassPackage.split("\\.");
      entityUtilsClass = split[split.length - 1];
    }

    String finalEntityUtilsClass = entityUtilsClass;
    InjectionConfig injectionConfig = new InjectionConfig.Builder()
        .beforeOutputFile( (table, map) -> {
            map.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
    map.put("entityUtilsClass", finalEntityUtilsClass);
    map.put("entityUtilsClassPackage", entityUtilsClassPackage);
    List<String> superEntityColumns = new ArrayList<>();
    superEntityColumns.add("id");
    if(isOp(table)) {
      superEntityColumns.addAll(Arrays.asList(currentSuperOpEntityColumns));
      if(StringUtils.isNotBlank(superOpEntityClassPackage)) {
        String[] splits = superOpEntityClassPackage.split("\\.");
        String superEntityClass = splits[splits.length - 1];
        map.put("superEntityClassPackage", superOpEntityClassPackage);
        map.put("superEntityClass", superEntityClass);
      }
    } else {
      if(StringUtils.isNotBlank(superIdEntityClassPackage)) {
        String[] splits = superIdEntityClassPackage.split("\\.");
        String superEntityClass = (String) splits[splits.length - 1];
        map.put("superEntityClassPackage", superIdEntityClassPackage);
        map.put("superEntityClass", superEntityClass);
      }
    }
    Set<String> commonColumns = Sets.newHashSet(superEntityColumns);
    map.put("superEntityColumns", superEntityColumns);
    String entityClassName = (String) map.get("entity");
    String entityVariableName = StringUtils.removeEnd(entityClassName, "Entity");
    entityVariableName = StringUtils.uncapitalize(entityVariableName);
    map.put("entityVariableName", entityVariableName);
    List<TableField> fields = table.getFields();
    List<TableField> commonFields = table.getCommonFields();
    Iterator<TableField> iterator = fields.iterator();
    while (iterator.hasNext()) {
      TableField field = iterator.next();
      if(commonColumns.contains(field.getColumnName())) {
        commonFields.add(field);
        iterator.remove();
      }
    }
    System.out.println("创建表:" + table.getName());
            }).build();

    AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfig);
    ConfigBuilder configBuilder = new ConfigBuilder(packageConfig, dataSourceConfig, strategyConfig, templateConfig, globalConfig, injectionConfig);
    Map<String, String> pathInfo = configBuilder.getPathInfo();
    putPathInfo(pathInfo, templateConfig.getXml(), ConstVal.XML_PATH, ConstVal.XML, globalConfig.getOutputDir(), packageConfig);
    autoGenerator.config(configBuilder);
    autoGenerator.execute(new CustomFreemarkerTemplateEngine());
  }

  private boolean isOp(TableInfo table) {
    List<TableField> fields = table.getFields();
    Set<String> columnNames = fields.stream().map(t -> t.getColumnName()).collect(Collectors.toSet());
    return columnNames.contains("creator") && columnNames.contains("create_time") && columnNames.contains("modifier");
  }

  private void putPathInfo(Map<String, String> pathInfo, String template, String path, String module, String outputDir, PackageConfig packageConfig) {
    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(template)){
      pathInfo.put(path, joinPath(outputDir, packageConfig.getPackageInfo(module)));
    }
  }

  /**
   * 连接路径字符串
   *
   * @param parentDir   路径常量字符串
   * @param packageName 包名
   * @return 连接后的路径
   */
  private String joinPath(String parentDir, String packageName) {
    if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(parentDir)) {
      parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
    }
    if (!com.baomidou.mybatisplus.core.toolkit.StringUtils.endsWith(parentDir, File.separator)) {
      parentDir += File.separator;
    }
    packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
    return parentDir + packageName;
  }

}
