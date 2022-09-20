package org.freshwater.boot.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.freshwater.boot.common.model.ResponseCode;
import org.freshwater.boot.common.model.ResponseModel;

import java.util.List;

/**
 * 统一的结果集返回 工具类
 * @author tuxuchen
 * @date 2022/8/29 10:18
 */
public class ResponseModelUtils {

  private ResponseModelUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 返回一个成功的数据
   * @return
   */
  public static ResponseModel success() {
    return success(null, "处理成功");
  }


  /**
   * 返回一个成功的数据
   * @param data
   * @return
   */
  public static ResponseModel success(Object data) {
    return success(data, "处理成功");
  }

  /**
   * 返回一个成功的数据
   * @param data
   * @return
   */
  public static ResponseModel success(Object data, String message) {
    ResponseModel responseModel = new ResponseModel();
    responseModel.setCode(ResponseCode.E0);
    responseModel.setSuccess(true);
    responseModel.setData(data);
    responseModel.setMessage(message);
    return responseModel;
  }

  /**
   * 返回一个失败信息数据
   * @param code
   * @param message
   * @return
   */
  public static ResponseModel failure(ResponseCode code, String message) {
    return failure(code, message, message);
  }

  /**
   * 返回一个失败信息数据
   * @param code
   * @param message
   * @param errorMessage
   * @return
   */
  public static ResponseModel failure(ResponseCode code, String message, String errorMessage) {
    ResponseModel responseModel = new ResponseModel();
    responseModel.setCode(code);
    responseModel.setSuccess(false);
    responseModel.setMessage(message);
    responseModel.setErrorMessage(errorMessage);
    return responseModel;
  }

  /**
   * 验证成功
   * @param responseModel
   */
  public static void validSuccess(ResponseModel responseModel) {
    validSuccess(responseModel, "操作失败");
  }

  /**
   * 验证成功
   * @param responseModel
   * @param defaultMsg
   */
  public static void validSuccess(ResponseModel responseModel, String defaultMsg) {
    if(StringUtils.isBlank(defaultMsg)) {
      defaultMsg = "操作失败";
    }
    if(responseModel == null || responseModel.getSuccess() == null || !responseModel.getSuccess()) {
      String msg = StringUtils.isBlank(responseModel.getErrorMessage()) ? defaultMsg : responseModel.getErrorMessage();
      throw new RuntimeException(msg);
    }
  }

  /**
   * 验证请求是否成功
   * @param responseModel
   * @return
   */
  public static boolean isSuccess(ResponseModel responseModel){
    if(responseModel == null || responseModel.getSuccess() == null || !responseModel.getSuccess()) {
      return false;
    }
    return true;
  }

  /**
   * 获取成功的数据
   * @param responseModel
   * @return
   */
  public static <T> T getSuccessData(ResponseModel responseModel, Class<T> clazz) {
    if(responseModel == null || responseModel.getSuccess() == null || !responseModel.getSuccess()) {
      return null;
    }
    Object data = responseModel.getData();
    if(data == null) {
      return null;
    }
    return JsonUtils.convert(data, clazz);
  }

  /**
   * 解析并获取ResponseModel报文中的对象列表
   * @param responseModel
   * @param typeReference
   * @param <T>
   * @return
   */
  public static <T> List<T> getList(ResponseModel responseModel, TypeReference<List<T>> typeReference) {
    if(responseModel == null || responseModel.getSuccess() == null || !responseModel.getSuccess()) {
      return Lists.newArrayList();
    }
    Object data = responseModel.getData();
    return JsonUtils.convert(data, typeReference);
  }
}
