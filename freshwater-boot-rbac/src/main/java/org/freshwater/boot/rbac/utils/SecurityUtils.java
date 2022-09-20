package org.freshwater.boot.rbac.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.freshwater.boot.common.constants.enums.NormalStatusEnum;
import org.freshwater.boot.common.utils.ApplicationContextUtils;
import org.freshwater.boot.rbac.constants.Constants;
import org.freshwater.boot.rbac.entity.RoleEntity;
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.service.RoleService;
import org.freshwater.boot.rbac.service.UserService;

import java.security.Principal;
import java.util.List;

import static org.freshwater.boot.rbac.constants.Constants.ANONYMOUS_USER;


/**
 * 权限相关的工具,主要方便查询当前登录用户的信息
 * @author tuxuchen
 * @date 2022/7/25 17:18
 */
public class SecurityUtils {

  protected SecurityUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 获取当前系统登录人的代理对象
   * @return
   */
  public static Principal getPrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Validate.notNull(authentication , "当前系统未登录用户！");
    return authentication;
  }

  /**
   * 设置当前登录人，允许更换当前登录人的账号信息，相当于切换了登录账号
   * @param account
   */
  public static void setPrincipal(String account) {
    Validate.notBlank(account, "登录账号不能为空");
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account, Constants.USER_DEFAULT_PASSWORD, Lists.newArrayList());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /**
   * 匿名用户设置, 当前系统未登录信息或是匿名用户登录时，允许自定义设置一个账号来替换匿名用户
   * @param account
   */
  public static void setAnonymousUser(String account) {
    Validate.notBlank(account, "登录账号不能为空");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(authentication == null || ANONYMOUS_USER.equals(authentication.getName())) {
      setPrincipal(account);
    }
  }

  /**
   * 获取当前系统的登录人账号，返回的是Principal的name属性
   * @return
   */
  public static String getUserAccount() {
    Principal principal = getPrincipal();
    String account = principal.getName();
    Validate.notBlank(account , "当前系统未登录用户！");
    Validate.isTrue(!ANONYMOUS_USER.equals(account), "当前系统未登录用户！");
    return account;
  }

  /**
   * 获取当前登录用户信息
   * @return
   */
  public static UserEntity getCurrentUser() {
    String account = getUserAccount();
    UserService userService = ApplicationContextUtils.getApplicationContext().getBean(UserService.class);
    UserEntity user = userService.findByUserAccount(account);
    Validate.notNull(user, "登录用户不存在:%s", account);
    return user;
  }

  /**
   * 是否登录
   * @return
   */
  public static boolean isLogin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(authentication == null || ANONYMOUS_USER.equals(authentication.getName())) {
      return false;
    }
    return true;
  }

  /**
   * 根据用户id获取用户权限信息
   * @param userId
   * @return
   */
  public static List<? extends GrantedAuthority> getAuthorityByUserId(String userId) {
    Validate.notBlank(userId, "用户ID为空!");
    RoleService roleService = ApplicationContextUtils.getApplicationContext().getBean(RoleService.class);
    List<RoleEntity> roles = roleService.findByUserIdAndState(userId, NormalStatusEnum.ENABLE.getStatus());
    Validate.notEmpty(roles, "当前用户未分配角色!");
    List<SimpleGrantedAuthority> authorities = Lists.newArrayList();
    for (RoleEntity role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getCode()));
    }
    return authorities;
  }

  /**
   * 根据用户账号获取用户权限信息
   * @param username
   * @return
   */
  public static List<? extends GrantedAuthority> getAuthorityByUsername(String username) {
    Validate.notBlank(username, "用户账号为空!");
    UserService userService = ApplicationContextUtils.getApplicationContext().getBean(UserService.class);
    UserEntity user = userService.findByUserAccount(username);
    return SecurityUtils.getAuthorityByUserId(user.getId());
  }

}
