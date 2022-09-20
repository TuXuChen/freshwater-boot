package org.freshwater.boot.rbac.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.rbac.entity.ButtonEntity;
import org.freshwater.boot.rbac.entity.RoleButtonMappingEntity;
import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.mapper.RoleButtonMappingMapper;
import org.freshwater.boot.rbac.service.ButtonService;
import org.freshwater.boot.rbac.service.RoleButtonMappingService;
import org.freshwater.boot.rbac.service.RoleService;

import java.util.List;

/**
 * <p>
 * 角色按钮关联信息 服务实现类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
@Service
public class RoleButtonMappingServiceImpl extends ServiceImpl<RoleButtonMappingMapper, RoleButtonMappingEntity> implements RoleButtonMappingService {

  @Autowired
  private RoleService roleService;
  @Autowired
  private ButtonService buttonService;

  @Override
  @Transactional
  public RoleButtonMappingEntity create(RoleButtonMappingEntity roleButtonMapping) {
    ValidationUtils.validate(roleButtonMapping, CreateValidate.class);
    // TODO: 2022-07-22 开发人员进行业务逻辑处理
    baseMapper.insert(roleButtonMapping);
    return roleButtonMapping;
  }

  @Override
  @Transactional
  public RoleButtonMappingEntity update(RoleButtonMappingEntity roleButtonMapping) {
    ValidationUtils.validate(roleButtonMapping, UpdateValidate.class);
    RoleButtonMappingEntity dbRoleButtonMapping = baseMapper.selectById(roleButtonMapping.getId());
    Validate.notNull(dbRoleButtonMapping, "更新的角色按钮关联信息不存在");
    dbRoleButtonMapping.setRoleId(roleButtonMapping.getRoleId());
    dbRoleButtonMapping.setButtonId(roleButtonMapping.getButtonId());
    // TODO: 2022-07-22 开发人员进行业务逻辑处理
    baseMapper.updateById(dbRoleButtonMapping);
    return dbRoleButtonMapping;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void rebindRoleButtons(String roleId, List<ButtonEntity> buttons) {
    this.bindValidation(roleId, buttons);
    try {
      baseMapper.deleteByRoleId(roleId);
      if(CollectionUtils.isEmpty(buttons)) {
        return;
      }
      for (ButtonEntity button : buttons) {
        RoleButtonMappingEntity mapping = new RoleButtonMappingEntity();
        mapping.setRoleId(roleId);
        mapping.setButtonId(button.getId());
        baseMapper.insert(mapping);
      }
    } finally {
      buttonService.deleteUrlCache();
    }
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    Validate.notBlank(id, "主键ID不能为空");
    baseMapper.deleteById(id);
  }

  @Override
  public RoleButtonMappingEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  /**
   * 绑定前数据校验
   * @param roleId
   * @param buttons
   */
  private void bindValidation(String roleId, List<ButtonEntity> buttons) {
    Validate.notBlank(roleId, "角色ID不能为空");
    RoleEntity role = roleService.findById(roleId);
    Validate.notNull(role, "角色不存在:%s", roleId);
    if(!CollectionUtils.isEmpty(buttons)) {
      for (ButtonEntity button : buttons) {
        Validate.notBlank(button.getId(), "按钮ID不能为空");
        ButtonEntity dbButton = buttonService.findById(button.getId());
        Validate.notNull(dbButton, "按钮不存在:%s", button.getId());
      }
    }
  }

}
