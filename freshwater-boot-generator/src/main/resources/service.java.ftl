package ${package.Service};

import ${package.Entity}.${entity};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${datetime}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} {

<#-- 开始生成默认的curd代码-->
  /**
   * 创建${table.comment!}
   * @param ${entityVariableName}
   * @return
   */
  ${entity} create(${entity} ${entityVariableName});

  /**
   * 更新${table.comment!}
   * @param ${entityVariableName}
   * @return
   */
  ${entity} update(${entity} ${entityVariableName});

  /**
   * 根据主键ID删除
   * @param id
   */
  void deleteById(String id);

  /**
   * 根据ID查询${table.comment!}
   * @param id
   * @return
  */
  ${entity} findById(String id);
}
</#if>
