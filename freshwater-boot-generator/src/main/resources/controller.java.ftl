package ${package.Controller};

import ${package.Entity}.${entity};
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ${package.Service}.${table.serviceName};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(value = "${table.comment!}管理", tags = {"${table.comment!}管理"})
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/v1/${entityVariableName}s")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

  @Autowired
  private ${table.serviceName} ${table.serviceName?uncap_first};

<#-- 开始生成默认的curd代码-->
  /**
   * 创建${table.comment!}
   * @param ${entityVariableName}
   * @return
   */
  @PostMapping("")
  @ApiOperation("创建${table.comment!}")
  public ResponseModel create(@RequestBody ${entity} ${entityVariableName}) {
    ${entity} saved${entityVariableName?cap_first} = ${table.serviceName?uncap_first}.create(${entityVariableName});
    return ResponseModelUtils.success(saved${entityVariableName?cap_first});
  }

 /**
  * 更新${table.comment!}
  * @param ${entityVariableName}
  * @return
  */
  @PutMapping("")
  @ApiOperation("更新${table.comment!}")
  public ResponseModel update(@RequestBody ${entity} ${entityVariableName}) {
    ${entity} saved${entityVariableName?cap_first} = ${table.serviceName?uncap_first}.update(${entityVariableName});
    return ResponseModelUtils.success(saved${entityVariableName?cap_first});
  }

  /**
   * 根据主键ID删除数据
   * @param id
   * @return
   */
  @DeleteMapping("{id}")
  @ApiOperation("根据主键ID删除数据")
  public ResponseModel deleteById(@PathVariable String id) {
    ${table.serviceName?uncap_first}.deleteById(id);
    return ResponseModelUtils.success();
  }

  /**
   * 根据ID查询${table.comment!}基本信息
   * @param id
   * @return
   */
  @GetMapping("{id}")
  @ApiOperation("根据ID查询${table.comment!}基本信息")
  public ResponseModel findById(@PathVariable String id) {
    ${entity} ${entityVariableName} = ${table.serviceName?uncap_first}.findById(id);
    return ResponseModelUtils.success(${entityVariableName});
  }

}
</#if>
