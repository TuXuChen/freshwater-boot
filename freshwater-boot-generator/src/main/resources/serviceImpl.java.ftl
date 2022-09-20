package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
<#if entityUtilsClassPackage?? && entityUtilsClassPackage != ''>
import ${entityUtilsClassPackage};
</#if>
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${datetime}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

<#-- 开始生成默认的curd代码-->
  @Override
  @Transactional
  public ${entity} create(${entity} ${entityVariableName}) {
    ValidationUtils.validate(${entityVariableName}, CreateValidate.class);
    <#if superEntityClass == 'OpUuidEntity'>
    ${entityUtilsClass}.initCreatorInfo(${entityVariableName});
    </#if>
    // TODO: ${date} 开发人员进行业务逻辑处理
    baseMapper.insert(${entityVariableName});
    return ${entityVariableName};
  }

  @Override
  @Transactional
  public ${entity} update(${entity} ${entityVariableName}) {
    ValidationUtils.validate(${entityVariableName}, UpdateValidate.class);
    ${entity} db${entityVariableName?cap_first} = baseMapper.selectById(${entityVariableName}.getId());
    Validate.notNull(db${entityVariableName?cap_first}, "更新的${table.comment!}不存在");
    <#if superEntityClass == 'OpUuidEntity'>
    ${entityUtilsClass}.initModifierInfo(db${entityVariableName?cap_first});
    </#if>
    <#list table.fields as field>
    db${entityVariableName?cap_first}.set${field.propertyName?cap_first}(${entityVariableName}.get${field.propertyName?cap_first}());
    </#list>
    // TODO: ${date} 开发人员进行业务逻辑处理
    baseMapper.updateById(db${entityVariableName?cap_first});
    return db${entityVariableName?cap_first};
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    Validate.notBlank(id, "主键ID不能为空");
    baseMapper.deleteById(id);
  }

  @Override
  public ${entity} findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

}
</#if>
