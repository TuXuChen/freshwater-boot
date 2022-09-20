package ${package.Entity};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import org.hibernate.validator.constraints.Length;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
<#if superEntityClassPackage!?length gt 0>
import ${superEntityClassPackage};
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
    <#if chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>

/**
 * <p>
 * ${table.comment!}实体
 * </p>
 *
 * @author: ${author}
 * @Date: ${datetime}
 */
<#if entityLombokModel>
@Data
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
    <#if chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#if table.convert>
@TableName("${schemaName}${table.name}")
</#if>
<#if swagger>
@ApiModel(value = "${table.comment!}对象", description = "${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#else>
public class ${entity} implements Serializable {
</#if>

<#if entitySerialVersionUID>
  private static final long serialVersionUID = ${randomLong()?c}L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
  <#if field.keyFlag>
      <#assign keyPropertyName="${field.propertyName}"/>
  </#if>

  <#if field.comment!?length gt 0>
  /**
   * ${field.comment}
   */
      <#if swagger>
  @ApiModelProperty(value = "${field.comment}")
      </#if>
  </#if>
  <#if field.keyFlag>
      <#-- 主键 -->
      <#if field.keyIdentityFlag>
  @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
      <#elseif idType??>
  @TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
      <#elseif field.convert>
  @TableId("${field.annotationColumnName}")
      </#if>
      <#-- 普通字段 -->
  <#elseif field.fill??>
  <#-- -----   存在字段填充设置   ----->
      <#if field.convert>
  @TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
      <#else>
  @TableField(fill = FieldFill.${field.fill})
      </#if>
  <#elseif field.convert>
  @TableField("${field.annotationColumnName}")
  </#if>
  <#-- 乐观锁注解 -->
  <#if field.versionField>
  @Version
  </#if>
  <#-- 逻辑删除注解 -->
  <#if field.logicDeleteField>
  @TableLogic
  </#if>
  <#if ',Date,Time,Timestamp,LocalDateTime,LocalDate,LocalTime'?contains(',' + field.columnType.type)>
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  </#if>
  <#if ',String,Short'?contains(',' + field.columnType.type)>
  @Length(max = ${field.metaInfo.length?c}, message = "${field.comment}字符长度不能超过${field.metaInfo.length?c}!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  </#if>
  <#--  打上不能为空的注解  -->
  <#if !field.metaInfo.nullable>
      <#if ',String,Short'?contains(',' + field.columnType.type)>
  @NotBlank(message = "${field.comment}不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
      </#if>
      <#if ',Integer,Boolean,Double,Float,Date,Time,Timestamp,LocalDateTime,LocalDate,LocalTime,Date,BigInteger,BigDecimal'?contains(',' + field.columnType.type)>
  @NotNull(message = "${field.comment}不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
      </#if>
  </#if>
  private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

<#if !entityLombokModel>
  <#list table.fields as field>
      <#if field.propertyType == "boolean">
          <#assign getprefix="is"/>
      <#else>
          <#assign getprefix="get"/>
      </#if>
  public ${field.propertyType} ${getprefix}${field.capitalName}() {
      return ${field.propertyName};
  }

  <#if chainModel>
  public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
  <#else>
  public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
  </#if>
      this.${field.propertyName} = ${field.propertyName};
      <#if chainModel>
      return this;
      </#if>
  }
  </#list>
</#if>

<#if entityColumnConstant>
  <#list table.fields as field>
  public static final String ${field.name?upper_case} = "${field.name}";

  </#list>
</#if>
<#if activeRecord>
  @Override
  protected Serializable pkVal() {
  <#if keyPropertyName??>
      return this.${keyPropertyName};
  <#else>
      return null;
  </#if>
  }

</#if>
<#if !entityLombokModel>
  @Override
  public String toString() {
      return "${entity}{" +
  <#list table.fields as field>
      <#if field_index==0>
          "${field.propertyName}=" + ${field.propertyName} +
      <#else>
          ", ${field.propertyName}=" + ${field.propertyName} +
      </#if>
  </#list>
      "}";
  }
</#if>
}
