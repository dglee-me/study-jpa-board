package kr.co.dglee.board.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
    // access token은 Response Body에 담아서 전달하고, refresh token은 Cookie에 담아서 전달한다.
    // 이 곳에서 JWT 토큰을 발급할 수 있나? 아니면 다른 곳에서 발급해야하나?
    // 로그인 성공 후 처리할 내용을 작성한다.

    // Access Token은 Authorization Header에 담아서 전달한다. / 프론트엔드에서 받아서 Local Storage에 저장한다.
    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer accessToken");

    // Refresh Token을 Cookie에 담아서 전달한다.
    response.setHeader(HttpHeaders.SET_COOKIE, "refresh_token=refreshToken; Path=/; HttpOnly; Secure; SameSite=strict; Domain=localhost Max-Age=3600;");

    log.info("로그인 성공: {}", authentication.getName());
  }
}
