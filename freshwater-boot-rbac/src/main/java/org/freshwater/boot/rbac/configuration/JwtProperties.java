package org.freshwater.boot.rbac.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * jwt
 * @author tuxuchen
 * @date 2022/8/3 17:12
 */
@Data
@Component
@EnableConfigurationProperties({JwtProperties.class})
@ConfigurationProperties(prefix = "freshwater.jwt")
public class JwtProperties {

  /**
   * JWT名称
   */
  private String header;

  /**
   * 过期时间
   */
  private Long expire;

  /**
   * 密钥
   */
  private String secret;

  /**
   * 生成Jwt
   *
   * @param username
   * @return
   */
  public String generateToken(String username) {
    Date nowDate = new Date();
    Date expireDate = new Date(nowDate.getTime() + 1000 * expire);
    return Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setSubject(username)
        .setIssuedAt(nowDate)
        // 7天過期
        .setExpiration(expireDate)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  /**
   * 解析Jwt
   *
   * @param jwt
   * @return
   */
  public Claims getClaimsByToken(String jwt) {
    try {
      return Jwts.parser()
          // 密钥
          .setSigningKey(secret)
          .parseClaimsJws(jwt)
          .getBody();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 判断Jwt是否过期
   *
   * @param claims
   * @return
   */
  public boolean isTokenExpired(Claims claims) {
    return claims.getExpiration().before(new Date());
  }

}
