package org.freshwater.boot.common.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.util.Base64Utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具类
 * @author tuxuchen
 * @date 2022/8/29 10:26
 */
public class Md5Utils {

  private Md5Utils() {
    throw new UnsupportedOperationException();
  }

  /**
   * 将文本内容进行md5加密
   * @param content
   * @param encodeType
   * @return
   */
  public static String encode(String content, EncodeType encodeType){
    String resultString = null;
    byte[] bytes = encode(content);
    encodeType = ObjectUtils.defaultIfNull(encodeType, EncodeType.HEX);
    switch (encodeType) {
      case HEX:
        resultString = Hex.encodeHexString(bytes);
        break;
      case BASE64:
        resultString = Base64Utils.encodeToString(bytes);
        break;
      default:
        break;
    }
    return resultString;
  }

  /**
   * 将文本内容进行md5加密
   * @param content
   * @return
   */
  public static byte[] encode(String content) {
    Validate.notNull(content, "加密文本不能为null");
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      return md.digest(content.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  /**
   * 将文本内容进行md5加密,并将结果集以指定进制输出字符串
   * 注,如果radix超过指定的数据返回,则默认为10
   * @param content 文本内容
   * @param radix 指定转换的进制, 2 <= radix <= 36
   * @return
   */
  public static String encode(String content, int radix) {
    byte[] bytes = encode(content);
    BigInteger bigInt = new BigInteger(1, bytes);
    return bigInt.toString(radix);
  }

  /**
   * 编码方式
   */
  public enum EncodeType {
    HEX, BASE64
  }
}
