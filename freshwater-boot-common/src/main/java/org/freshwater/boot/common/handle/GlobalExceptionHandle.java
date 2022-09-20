package org.freshwater.boot.common.handle;

import lombok.extern.slf4j.Slf4j;
import org.freshwater.boot.common.model.ResponseCode;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * 全局异常信息处理
 * @author tuxuchen
 * @date 2022/7/22 16:47
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandle {

  /**
   * 全局异常信息处理
   * @param e
   * @return
   */
  @ResponseBody
  @ExceptionHandler(value = Exception.class)
  public ResponseModel handleException(Exception e) {
    log.error(e.getMessage(), e);
    String message;
    String errorMessage;
    if(e instanceof DataAccessException || e instanceof SQLException) {
      message = "数据库错误,请联系管理员!";
    } else {
      message = e.getMessage();
    }
    errorMessage = e.toString();
    return ResponseModelUtils.failure(ResponseCode.E5001, message, errorMessage);
  }

}
