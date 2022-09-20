package org.freshwater.boot.rbac.configuration.handle;

import org.freshwater.boot.common.model.ResponseCode;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.common.utils.ServletResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败后的处理器
 * @author tuxuchen
 * @date 2022/7/25 16:20
 */
public class SimpleAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    ResponseModel responseModel = ResponseModelUtils.failure(ResponseCode.E5001, exception.getMessage(), exception.getMessage());
    ServletResponseUtils.writeJson(response, responseModel);
  }

}
