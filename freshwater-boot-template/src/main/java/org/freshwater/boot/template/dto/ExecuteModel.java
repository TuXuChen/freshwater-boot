package org.freshwater.boot.template.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * sql执行模型
 * @author tuxuchen
 * @date 2022/8/19 14:45
 */
@Data
public class ExecuteModel {

  /**
   * 动态表编码
   */
  @NotBlank(message = "动态表编码不能为空!")
  private String code;

  /**
   * 输入的参数
   */
  private Map<String, Object> inputParams;

  /**
   * 系统参数
   */
  private Map<String, Object> systemParams;

  /**
   * 分页信息
   */
  private PageRequestModel pageRequest;

  /**
   * 获取分页信心
   * @return
   */
  public Pageable getPageable() {
    if(this.pageRequest != null) {
      return PageRequest.of(pageRequest.getPage(), pageRequest.getSize());
    }
    return null;
  }

}
