package org.freshwater.boot.template.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.groups.Default;
import java.util.List;

/**
 * 数据库动态表创建DTO
 * @author tuxuchen
 * @date 2022/8/16 10:20
 */
@Data
@ApiModel(value = "数据库动态表创建DTO", description = "数据库动态表创建DTO")
public class DynamicTableCreateDTO {

  /**
   * 编码
   */
  @ApiModelProperty(value = "编码")
  @Length(max = 32, message = "编码字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String code;

  /**
   * 表名
   */
  @ApiModelProperty(value = "表名")
  @TableField("table_name")
  @Length(max = 32, message = "表名字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "表名不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String tableName;

  /**
   * 名称
   */
  @ApiModelProperty(value = "名称")
  @Length(max = 32, message = "名称字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "名称不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String name;

  /**
   * 图标
   */
  @ApiModelProperty(value = "图标")
  @Length(max = 255, message = "图标字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String icon;

  /**
   * 是否生成Java编码文件
   */
  @ApiModelProperty(value = "是否生成Java编码文件")
  private Boolean isProjectFile = false;

  /**
   * java代码路径
   */
  @ApiModelProperty(value = "java代码路径")
  private String projectSrcPath;

  /**
   * 基础包路径
   */
  @ApiModelProperty(value = "基础包路径")
  private String rootPackage;

  /**
   * 数据库表前缀
   * <p>
   *   示例: 数据库表名 freshwater_student  则前缀为 freshwater_
   *   如果不传此参数 则生成的java代码文件名 会带前缀
   * </p>
   */
  @ApiModelProperty(value = "数据库表前缀")
  private String tablePrefixs;

  /**
   * 作者,开发者名字
   */
  @ApiModelProperty(value = "作者,开发者名字")
  private String author;

  /**
   * 是否生成菜单路由
   */
  @ApiModelProperty(value = "是否生成菜单路由")
  private Boolean isMenuRoute = false;

  /**
   * 前端菜单路由地址
   */
  @ApiModelProperty(value = "前端菜单路由地址")
  private String menuUrl;

  /**
   * 动态表字段
   */
  @ApiModelProperty(value = "动态表字段")
  @NotEmpty(message = "动态表字段不能为空", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private List<DynamicTableFieldEntity> tableFields;

}
