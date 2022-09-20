package org.freshwater.boot.rbac.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liao hua
 * @date 2021/7/26 17:17
 */
@Api(tags = "登录")
@RestController
@RequestMapping("/v1/rbac")
public class LoginController {

  /**
   * 登录
   *
   * @param username
   * @param password
   */
  @PostMapping("login")
  @ApiOperation("登录")
  public void create(@RequestParam("username") String username, @RequestParam("password") String password) {
    // TODO　不需要测试时 需删除
  }

}
