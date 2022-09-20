package org.freshwater.boot.routing.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.freshwater.boot.common.entity.UuidEntity;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDateTime;

/**
 * <p>
 * 动态数据源实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-08-23 16:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_dynamic_datasource")
@ApiModel(value = "动态数据源对象", description = "动态数据源")
public class DynamicDatasourceEntity extends UuidEntity {

  private static final long serialVersionUID = 7542056051551381793L;

  /**
   * 数据源编码
   */
  @ApiModelProperty(value = "数据源编码")
  @Length(max = 255, message = "数据源编码字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String code;

  /**
   * 数据库地址
   */
  @ApiModelProperty(value = "数据库地址")
  @Length(max = 255, message = "数据库地址字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "数据库地址不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String url;

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
  @TableField("initial_size")
  @NotNull(message = "初始化大小不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer initialSize;

  /**
   * 最大连接池
   */
  @ApiModelProperty(value = "最大连接池")
  @TableField("max_active")
  @NotNull(message = "最大连接池不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer maxActive;

  /**
   * 最小连接池
   */
  @ApiModelProperty(value = "最小连接池")
  @TableField("min_idle")
  @NotNull(message = "最小连接池不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer minIdle;

  /**
   * 等待时间
   */
  @ApiModelProperty(value = "等待时间")
  @TableField("max_wait")
  @NotNull(message = "等待时间不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer maxWait;

  /**
   * 是否启用
   */
  @ApiModelProperty(value = "是否启用")
  @NotNull(message = "是否启用不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Boolean enabled;

  /**
   * 数据库驱动类
   */
  @ApiModelProperty(value = "数据库驱动类")
  @TableField("driver_class_name")
  @Length(max = 255, message = "数据库驱动类字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "数据库驱动类不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String driverClassName;

  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间")
  @TableField("create_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  /**
   * 最后修改时间
   */
  @ApiModelProperty(value = "最后修改时间")
  @TableField("modify_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime modifyTime;

}
