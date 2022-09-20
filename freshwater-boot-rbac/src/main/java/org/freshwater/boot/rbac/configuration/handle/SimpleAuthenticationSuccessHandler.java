package org.freshwater.boot.rbac.configuration.handle;

import org.apache.commons.lang3.StringUtils;
import org.freshwater.boot.common.model.ResponseCode;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.common.utils.ServletResponseUtils;
import org.freshwater.boot.rbac.configuration.JwtProperties;
import org.freshwater.boot.rbac.constants.enums.ButtonType;
import org.freshwater.boot.rbac.entity.UserEntity;
import org.freshwater.boot.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 普通登录成功处理器
 * @author tuxuchen
 * @date 2022/7/25 16:22
 */
public class SimpleAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired
  private UserService userService;
  @Autowired
  private JwtProperties jwtProperties;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String username = authentication.getName();
    if(StringUtils.isBlank(username)) {
      ResponseModel responseModel = ResponseModelUtils.failure(ResponseCode.E6001, "用户未登录", "用户未登录");
      ServletResponseUtils.writeJson(response, responseModel);
    }
    UserEntity user = userService.findByUserAccount(username);
    if(user == null) {
      ResponseModel responseModel = ResponseModelUtils.failure(ResponseCode.E6001, "用户不存在", "用户不存在");
      ServletResponseUtils.writeJson(response, responseModel);
    }
    String jwt = jwtProperties.generateToken(authentication.getName());
    response.setHeader(jwtProperties.getHeader(), jwt);
    // todo 需要角色 或者菜单 再返回 目前只返回user
    HttpSession session = request.getSession();
    session.setAttribute("BUTTON_TYPE", ButtonType.BUTTON_WEB.getType());
    ResponseModel responseModel = ResponseModelUtils.success(user);
    ServletResponseUtils.writeJson(response, responseModel);
  }

}
