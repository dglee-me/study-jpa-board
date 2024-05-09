package kr.co.dglee.board.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * 인증이 실패했을 때 호출되는 핸들러
 */
@Slf4j
public class AuthFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {

    log.error("로그인 실패: {}", exception.getMessage());

    // 인증 실패 시 401 Unauthorized 응답을 반환한다.
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}