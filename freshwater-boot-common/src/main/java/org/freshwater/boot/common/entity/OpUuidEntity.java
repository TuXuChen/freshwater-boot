package org.freshwater.boot.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.freshwater.boot.common.group.CreatorValidate;
import org.freshwater.boot.common.group.ModifierValidate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 带操作信息的基本实体
 * @author tuxuchen
 * @date 2022/8/29 10:14
 */
@Data
public class OpUuidEntity extends UuidEntity {

  private static final long serialVersionUID = 2199713785244492779L;

  /**
   * 创建人账号
   */
  @ApiModelProperty("创建人账号")
  @NotBlank(message = "创建人账号不能为空", groups = CreatorValidate.class)
  @Length(max = 32, message = "创建人账号字符长度不能超过32!", groups = CreatorValidate.class)
  private String creator;

  /**
   * 创建人姓名
   */
  @ApiModelProperty("创建人姓名")
  @TableField("creator_name")
  @NotBlank(message = "创建人姓名不能为空", groups = CreatorValidate.class)
  @Length(max = 32, message = "创建人姓名字符长度不能超过32!", groups = CreatorValidate.class)
  private String creatorName;

  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  @TableField("create_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  /**
   * 最后修改人账号
   */
  @ApiModelProperty("最后修改人账号")
  @NotBlank(message = "最后修改人账号不能为空", groups = ModifierValidate.class)
  @Length(max = 32, message = "最后修改人账号字符长度不能超过32!", groups = ModifierValidate.class)
  private String modifier;

  /**
   * 最后修改人姓名
   */
  @ApiModelProperty("最后修改人姓名")
  @TableField("modifier_name")
  @NotBlank(message = "最后修改人账号不能为空", groups = ModifierValidate.class)
  @Length(max = 32, message = "最后修改人账号字符长度不能超过32!", groups = ModifierValidate.class)
  private String modifierName;

  /**
   * 最后修改时间
   */
  @ApiModelProperty("最后修改时间")
  @TableField("modify_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime modifyTime;
}
