package org.freshwater.boot.routing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.freshwater.boot.common.entity.UuidEntity;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * 动态数据源DTO
 * @author tuxuchen
 * @date 2022/8/24 16:07
 */
@Data
@ApiModel(value = "动态数据源DTO", description = "动态数据源DTO")
public class DynamicDatasourceDTO extends UuidEntity {

  private static final long serialVersionUID = 7056639005738894544L;

  /**
   * 数据库地址
   */
  @ApiModelProperty(value = "数据库地址")
  @Length(max = 32, message = "数据库地址字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String host;

  /**
   * 数据库端口
   */
  @ApiModelProperty(value = "数据库端口")
  @Length(max = 16, message = "数据库端口字符长度不能超过16!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String port;

  /**
   * 用户名
   */
  @ApiModelProperty(value = "用户名")
  @Length(max = 255, message = "用户名字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "用户名不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String username;

  /**
   * 密码
   */
  @ApiModelProperty(value = "密码")
  @Length(max = 255, message = "密码字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "密码不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String password;

  /**
   * 初始化大小
   */
  @ApiModelProperty(value = "初始化大小")
  private Integer initialSize = 0;

  /**
   * 最大连接池
   */
  @ApiModelProperty(value = "最大连接池")
  private Integer maxActive = 10;

  /**
   * 最小连接池
   */
  @ApiModelProperty(value = "最小连接池")
  private Integer minIdle = 0;

  /**
   * 等待时间
   */
  @ApiModelProperty(value = "等待时间")
  private Integer maxWait = 6000;

  /**
   * 数据库驱动类
   */
  @ApiModelProperty(value = "数据库驱动类")
  @Length(max = 255, message = "数据库驱动类字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String driverClassName = "com.mysql.cj.jdbc.Driver";

  /**
   * 是否启用
   */
  @ApiModelProperty(value = "是否启用")
  @NotNull(message = "是否启用不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean enabled;

}
