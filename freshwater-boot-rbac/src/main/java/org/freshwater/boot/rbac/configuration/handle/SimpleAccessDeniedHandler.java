package org.freshwater.boot.rbac.configuration.handle;

import org.freshwater.boot.common.model.ResponseCode;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.common.utils.ServletResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过期时的处理
 * @author tuxuchen
 * @date 2022/7/25 16:11
 */
public class SimpleAccessDeniedHandler implements AccessDeniedHandler {

  private static final String USER_DENIED = "用户无权限访问此功能";

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
    ResponseModel responseModel = ResponseModelUtils.failure(ResponseCode.E6002, USER_DENIED, USER_DENIED);
    ServletResponseUtils.writeJson(response, responseModel);
  }

}
