package kr.co.dglee.board.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  /**
   * @see <a href="https://datatracker.ietf.org/doc/html/rfc6750">RFC6750</a>
   * <br/> OAuth 2.0 Authorization Framework: Bearer Token
   */
  public static final String TOKEN_PREFIX = "Bearer ";

  private static final String ROLE = "role";

  private static final String USER_NAME = "userName";

  private static final Long ACCESS_TOKEN_EXPIRED = 60 * 60 * 1L; // 1시간

  private final SecretKey key;

  public JwtUtil(@Value("${spring.auth.token}") String secret) {
    key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SIG.HS256.key().build().getAlgorithm());
  }

  public String getUserName(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get(USER_NAME, String.class);
  }

  public String getRole(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get(ROLE, String.class);
  }

  public boolean isExpired(String token) {

    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
  }

  public String createToken(String userName, String role) {
    return Jwts.builder()
        .claim(USER_NAME, userName)
        .claim(ROLE, role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED))
        .signWith(key)
        .compact();
  }
}
