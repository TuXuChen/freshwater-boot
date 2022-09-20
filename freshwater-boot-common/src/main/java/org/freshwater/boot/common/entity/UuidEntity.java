package org.freshwater.boot.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * uuid主键的实体定义
 * @author tuxuchen
 * @date 2022/8/29 10:13
 */
@Data
public class UuidEntity implements Serializable {

  private static final long serialVersionUID = 1111151735552585034L;

  /**
   * 主键ID，使用UUID
   */
  @ApiModelProperty("主键ID，使用UUID")
  @TableId(value = "id", type = IdType.ASSIGN_UUID)
  @Length(max = 255, message = "主键字符长度不能大于255", groups = UpdateValidate.class)
  @NotBlank(message = "更新时主键不能为空!", groups = UpdateValidate.class)
  @Null(message = "创建数据时,主键必须为null", groups = CreateValidate.class)
  private String id;
}
