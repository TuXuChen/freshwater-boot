package org.freshwater.boot.rbac.configuration.handle;

import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.common.utils.ServletResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出成功的处理类
 * @author tuxuchen
 * @date 2022/7/25 16:20
 */
public class SimpleLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String username = "";
    if(authentication != null) {
      username = authentication.getName();
    }
    Cookie[] allCookies = request.getCookies();
    if(allCookies != null) {
      for(int index = 0 ; index < allCookies.length ; index++){
        Cookie currentCookie = allCookies[index];
        currentCookie.setMaxAge(0);
        response.addCookie(currentCookie);
      }
    }
    ResponseModel responseModel = ResponseModelUtils.success(username);
    ServletResponseUtils.writeJson(response, responseModel);
  }

}
