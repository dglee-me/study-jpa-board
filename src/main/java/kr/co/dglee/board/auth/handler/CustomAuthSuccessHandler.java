package kr.co.dglee.board.auth.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 성공적으로 인증된 경우 호출되는 핸들러
 */
@Slf4j
@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    // TODO:: Cookie? Local Storage? 결정해야한다.
    // 이 곳에서 JWT 토큰을 발급할 수 있나? 아니면 다른 곳에서 발급해야하나?
    // 로그인 성공 후 처리할 내용을 작성한다.
    response.addCookie(new Cookie("access_token", "access_token"));
    response.addCookie(new Cookie("refresh_token", "refresh_token"));

    log.info("로그인 성공: {}", authentication.getName());
  }
}
