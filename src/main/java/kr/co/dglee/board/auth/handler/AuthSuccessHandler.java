package kr.co.dglee.board.auth.handler;

import static kr.co.dglee.board.auth.util.JwtUtil.TOKEN_PREFIX;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.dglee.board.auth.util.JwtUtil;
import kr.co.dglee.board.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 성공적으로 인증된 경우 호출되는 핸들러
 */
@Component
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    User user = (User) authentication.getPrincipal();

    final String accessToken = jwtUtil.createToken(user.getEmail(), user.getRole().name());
    response.setHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken);
  }
}
