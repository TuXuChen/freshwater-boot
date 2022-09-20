package org.freshwater.boot.rbac.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.freshwater.boot.common.constants.enums.NormalStatusEnum;
import org.freshwater.boot.common.group.CreateValidate;
import org.freshwater.boot.common.group.UpdateValidate;
import org.freshwater.boot.common.utils.ValidationUtils;
import org.freshwater.boot.rbac.dto.UserQueryDTO;
import org.freshwater.boot.rbac.dto.UserRegisterDTO;
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.mapper.UserMapper;
import org.freshwater.boot.rbac.service.UserRoleMappingService;
import org.freshwater.boot.rbac.service.UserService;
import org.freshwater.boot.rbac.utils.EntityUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.freshwater.boot.common.constants.Constants.DEFAULT_PAGEABLE;
import static org.freshwater.boot.rbac.constants.Constants.USER_ACCOUNT;
import static org.freshwater.boot.rbac.constants.Constants.USER_DEFAULT_PASSWORD;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author tuxuchen
 * @since 2022-07-22 16:22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

  @Autowired
  private UserRoleMappingService userRoleMappingService;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public UserEntity create(UserEntity user) {
    this.createValidation(user);
    EntityUtils.initCreatorInfo(user);
    user.setPassword(passwordEncoder.encode(USER_DEFAULT_PASSWORD));
    baseMapper.insert(user);
    return user;
  }

  @Override
  public UserEntity register(UserRegisterDTO registerUser) {
    ValidationUtils.validate(registerUser, CreateValidate.class);
    long countUserAccount = baseMapper.selectCountByUserAccount(registerUser.getMobile());
    Validate.isTrue(countUserAccount == 0, "登录账号已存在:%s", registerUser.getMobile());
    UserEntity user = new UserEntity();
    user.setUserAccount(registerUser.getMobile());
    user.setMobile(registerUser.getMobile());
    user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
    user.setRealName(registerUser.getMobile());
    user.setState(NormalStatusEnum.ENABLE.getStatus());
    user.setCreator(registerUser.getMobile());
    user.setCreatorName(registerUser.getMobile());
    user.setCreateTime(LocalDateTime.now());
    user.setModifier(registerUser.getMobile());
    user.setModifierName(registerUser.getMobile());
    user.setModifyTime(LocalDateTime.now());
    baseMapper.insert(user);
    return user;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public UserEntity update(UserEntity user) {
    this.updateValidation(user);
    UserEntity dbUser = baseMapper.selectById(user.getId());
    Validate.isTrue(!Objects.equals(USER_ACCOUNT, dbUser.getUserAccount()), "不能修改超级管理员");
    EntityUtils.initModifierInfo(dbUser);
    dbUser.setUserAccount(user.getUserAccount());
    dbUser.setMobile(user.getMobile());
    dbUser.setRealName(user.getRealName());
    dbUser.setEmail(user.getEmail());
    dbUser.setState(user.getState());
    baseMapper.updateById(dbUser);
    return dbUser;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteById(String id) {
    Validate.notBlank(id, "主键ID不能为空");
    long countId = baseMapper.selectCountById(id);
    Validate.isTrue(countId != 0, "删除的账号不存在");
    userRoleMappingService.deleteByUserId(id);
    baseMapper.deleteById(id);
  }

  @Override
  public UserEntity findById(String id) {
    if(StringUtils.isBlank(id)) {
      return null;
    }
    return baseMapper.selectById(id);
  }

  @Override
  public UserEntity findByUserAccount(String username) {
    if(StringUtils.isBlank(username)) {
      return null;
    }
    return baseMapper.selectByUserAccount(username);
  }

  @Override
  public PageInfo findByConditions(UserQueryDTO query, Pageable pageable) {
    pageable = ObjectUtils.defaultIfNull(pageable, DEFAULT_PAGEABLE);
    PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
    List<UserEntity> users = baseMapper.selectByConditions(query);
    PageHelper.clearPage();
    return PageInfo.of(users);
  }

  private void createValidation(UserEntity user) {
    ValidationUtils.validate(user, CreateValidate.class);
    long count = baseMapper.selectCountByUserAccount(user.getUserAccount());
    Validate.isTrue(count == 0, "登录账号已存在:%s", user.getUserAccount());
  }

  private void updateValidation(UserEntity user) {
    ValidationUtils.validate(user, UpdateValidate.class);
    long countId = baseMapper.selectCountById(user.getId());
    Validate.isTrue(countId != 0, "更新账号不存在");
    long countAccountAndIdNot = baseMapper.selectByUserAccountAndIdNot(user.getUserAccount(), user.getId());
    Validate.isTrue(countAccountAndIdNot == 0, "登录账号已存在:%s", user.getUserAccount());
  }

}
