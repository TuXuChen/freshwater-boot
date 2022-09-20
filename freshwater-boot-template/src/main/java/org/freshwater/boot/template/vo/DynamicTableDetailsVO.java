package org.freshwater.boot.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.freshwater.boot.common.entity.OpUuidEntity;
import org.freshwater.boot.template.entity.DynamicTableFieldEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 动态表详情VO类
 * @author tuxuchen
 * @date 2022/8/19 11:38
 */
@Data
@ApiModel(value = "动态表详情VO类", description = "动态表详情VO类")
public class DynamicTableDetailsVO extends OpUuidEntity implements Serializable {

  private static final long serialVersionUID = 7235915241587789715L;

  /**
   * 编码
   */
  @ApiModelProperty(value = "编码")
  private String code;

  /**
   * 表名
   */
  @ApiModelProperty(value = "表名")
  private String tableName;

  /**
   * 名称
   */
  @ApiModelProperty(value = "名称")
  private String name;

  /**
   * 图标
   */
  @ApiModelProperty(value = "图标")
  private String icon;

  /**
   * 状态,0:禁用,1:启用
   */
  @ApiModelProperty(value = "状态,0:禁用,1:启用")
  private Integer state;

  /**
   * 动态表字段
   */
  @ApiModelProperty(value = "动态表字段")
  private List<DynamicTableFieldEntity> tableFields;
}
