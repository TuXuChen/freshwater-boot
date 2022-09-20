package org.freshwater.boot.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.freshwater.boot.common.entity.OpUuidEntity;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/**
 * <p>
 * 菜单实体
 * </p>
 *
 * @author: tuxuchen
 * @Date: 2022-07-22 17:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("freshwater_menu")
@ApiModel(value = "菜单对象", description = "菜单")
public class MenuEntity extends OpUuidEntity {

  private static final long serialVersionUID = -5987347823961910142L;

  /**
   * 状态,0:禁用,1:启用
   */
  @ApiModelProperty(value = "状态,0:禁用,1:启用")
  @NotNull(message = "状态,0:禁用,1:启用不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer state;

  /**
   * 类型:0平台,1APP
   */
  @ApiModelProperty(value = "类型:0平台,1APP")
  @NotNull(message = "类型:0平台,1APP不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer type;

  /**
   * 菜单编码
   */
  @ApiModelProperty(value = "菜单编码")
  @Length(max = 32, message = "菜单编码字符长度不能超过32!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "菜单编码不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String code;

  /**
   * 菜单名称
   */
  @ApiModelProperty(value = "菜单名称")
  @Length(max = 64, message = "菜单名称字符长度不能超过64!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  @NotBlank(message = "菜单名称不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String name;

  /**
   * 菜单地址
   */
  @ApiModelProperty(value = "菜单地址")
  @Length(max = 255, message = "菜单地址字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String url;

  /**
   * 图标
   */
  @ApiModelProperty(value = "图标")
  @Length(max = 255, message = "图标字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String icon;

  /**
   * 上级ID
   */
  @ApiModelProperty(value = "上级ID")
  @TableField("parent_id")
  @Length(max = 255, message = "上级ID字符长度不能超过255!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private String parentId;

  /**
   * 排序,数字越小,权重越大
   */
  @ApiModelProperty(value = "排序,数字越小,权重越大")
  @TableField("sort_index")
  @NotNull(message = "排序,数字越小,权重越大不能为空!", groups = {Default.class, CreateValidate.class, UpdateValidate.class})
  private Integer sortIndex;

  /**
   * 是否是选中状态
   */
  @TableField(exist = false)
  @ApiModelProperty(value = "是否是选中状态")
  private Boolean checked;

  /**
   * 下级菜单
   */
  @TableField(exist = false)
  @ApiModelProperty(value = "下级菜单")
  private List<MenuEntity> children;

  /**
   * 按钮
   */
  @TableField(exist = false)
  @ApiModelProperty(value = "按钮")
  private List<ButtonEntity> buttons;

  /**
   * 添加按钮
   * @param button
   */
  public void addButton(ButtonEntity button) {
    if(button == null) {
      return;
    }
    if(this.buttons == null) {
      this.buttons = Lists.newArrayList();
    }
    this.buttons.add(button);
  }

  /**
   * 添加下级
   * @param child
   */
  public void addChild(MenuEntity child) {
    if(child == null) {
      return;
    }
    if(CollectionUtils.isEmpty(children)) {
      children = Lists.newArrayList();
    }
    if(!children.contains(child)) {
      children.add(child);
    }
  }

}
