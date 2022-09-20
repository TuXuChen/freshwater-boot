package org.freshwater.boot.rbac.configuration.handle;

import org.freshwater.boot.common.model.ResponseCode;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.common.utils.ServletResponseUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录无效处理
 * @author tuxuchen
 * @date 2022/8/10 11:27
 */
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

  private static final String USER_NOT_LOGIN = "用户已失效或未登录";

  @Override
  public void afterPropertiesSet() throws Exception {

  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
    response.setStatus(HttpStatus.OK.value());
    ResponseModel responseModel = ResponseModelUtils.failure(ResponseCode.E6001, USER_NOT_LOGIN, USER_NOT_LOGIN);
    ServletResponseUtils.writeJson(response, responseModel);
  }

}
