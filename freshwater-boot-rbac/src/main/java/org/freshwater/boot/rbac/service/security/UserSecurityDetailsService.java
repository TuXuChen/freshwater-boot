package org.freshwater.boot.rbac.service.security;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.service.UserService;

/**
 * 用户信息的查询服务
 * @author tuxuchen
 * @date 2022/7/25 11:19
 */
public class UserSecurityDetailsService implements UserDetailsService {

  @Autowired
  private UserService userService;;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userService.findByUserAccount(username);
    Validate.notNull(user, "用户不存在");
    return new User(username, user.getPassword(), Lists.newArrayList());
  }

}
