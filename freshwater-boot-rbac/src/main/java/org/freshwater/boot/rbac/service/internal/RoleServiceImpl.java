package org.freshwater.boot.rbac.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.freshwater.boot.common.constants.enums.NormalStatusEnum;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.service.RedisService;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.rbac.dto.RoleQueryDTO;
import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.mapper.RoleMapper;
import org.freshwater.boot.rbac.service.RoleService;
import org.freshwater.boot.rbac.service.UserService;
import org.freshwater.boot.rbac.utils.EntityUtils;

import java.util.List;
import java.util.Objects;

import static org.freshwater.boot.common.constants.Constants.DEFAULT_PAGEABLE;
import static org.freshwater.boot.rbac.constants.RedisKeys.ROLE_CODE_INDEX;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 17:51
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

  @Autowired
  private UserService userService;
  @Autowired
  private RedisService redisService;

  @Override
  @Transactional
  public RoleEntity create(RoleEntity role) {
    ValidationUtils.validate(role, CreateValidate.class);
    long count = baseMapper.selectCountByName(role.getName());
    Validate.isTrue(count == 0, "重复的角色名称:%s", role.getName());
    String code = redisService.getAndIncrement(String.format(ROLE_CODE_INDEX), 1, 5);
    role.setCode(code);
    role.setIsSystem(false);
    EntityUtils.initCreatorInfo(role);
    baseMapper.insert(role);
    return role;
  }

  @Override
  @Transactional
  public RoleEntity update(RoleEntity role) {
    ValidationUtils.validate(role, UpdateValidate.class);
    long count = baseMapper.selectCountByNameAndIdNot(role.getName(), role.getId());
    Validate.isTrue(count == 0, "重复的角色名称:%s", role.getName());
    RoleEntity dbRole = baseMapper.selectById(role.getId());
    Validate.notNull(dbRole, "更新的角色不存在");
    if(dbRole.getIsSystem()) {
      Validate.isTrue(Objects.equals(role.getState(), NormalStatusEnum.ENABLE.getStatus()), "系统角色不能禁用");
    }
    EntityUtils.initModifierInfo(dbRole);
    dbRole.setName(role.getName());
    dbRole.setState(role.getState());
    baseMapper.updateById(dbRole);
    return dbRole;
  }

  @Override
  public void enableById(String id) {
    Validate.notBlank(id, "ID不能为空!");
    RoleEntity role = baseMapper.selectById(id);
    Validate.notNull(role, "未找到角色!");
    role.setState(NormalStatusEnum.ENABLE.getStatus());
    EntityUtils.initModifierInfo(role);
    baseMapper.updateById(role);
  }

  @Override
  @Transactional
  public void deleteById(String id) {
    Validate.notBlank(id, "主键ID不能为空");
    baseMapper.deleteById(id);
  }

  @Override
  public RoleEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  @Override
  public List<RoleEntity> findByUserIdAndState(String userId, Integer state) {
    if (StringUtils.isBlank(userId) || Objects.isNull(state)) {
      return Lists.newArrayList();
    }
    return baseMapper.selectByUserIdAndState(userId, state);
  }

  @Override
  public List<RoleEntity> findByUserNameAndState(String username, Integer state) {
    if (StringUtils.isBlank(username)) {
      return Lists.newArrayList();
    }
    return baseMapper.selectByUserNameAndState(username, state);
  }

  @Override
  public List<RoleEntity> findByStateAndUserId(Integer state, String userId) {
    if(StringUtils.isBlank(userId)) {
      return Lists.newArrayList();
    }
    UserEntity user = userService.findById(userId);
    if(user == null) {
      return Lists.newArrayList();
    }
    return baseMapper.selectByStateAndUserId(state, userId);
  }

  @Override
  public PageInfo<RoleEntity> findByConditions(RoleQueryDTO query, Pageable pageable) {
    pageable = ObjectUtils.defaultIfNull(pageable, DEFAULT_PAGEABLE);
    PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
    List<RoleEntity> roles = baseMapper.selectByConditions(query);
    PageHelper.clearPage();
    return PageInfo.of(roles);
  }

}
