package org.freshwater.boot.file.entity;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-08-03 14:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_file")
@ApiModel(value = "文件对象", description = "文件")
public class FileEntity extends UuidEntity {

  private static final long serialVersionUID = -8412780054773649138L;

  /**
   * 文件相对路径
   */
  @ApiModelProperty(value = "文件相对路径")
  @Length(max = 255, message = "文件相对路径字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "文件相对路径不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String path;

  /**
   * 文件路径
   */
  @ApiModelProperty(value = "文件路径")
  @Length(max = 255, message = "文件路径字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "文件路径不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String filename;

  /**
   * 源文件名
   */
  @ApiModelProperty(value = "源文件名")
  @TableField("original_filename")
  @Length(max = 255, message = "源文件名字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "源文件名不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String originalFilename;

  /**
   * 有效期
   */
  @ApiModelProperty(value = "有效期")
  @TableField("effective_date")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate effectiveDate;

  /**
   * 文件扩展名
   */
  @ApiModelProperty(value = "文件扩展名")
  @Length(max = 32, message = "文件扩展名字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String suffix;

  /**
   * 文件上传者
   */
  @ApiModelProperty(value = "文件上传者")
  @Length(max = 32, message = "文件上传者字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String creator;

  /**
   * 创建时间
   */
  @ApiModelProperty(value = "创建时间")
  @TableField("create_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @NotNull(message = "创建时间不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private LocalDateTime createTime;


}
