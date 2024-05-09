package kr.co.dglee.board.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  public AuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    try {
      LoginRequestDTO loginRequestDTO = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.email, loginRequestDTO.password);
      return getAuthenticationManager().authenticate(authToken);
    } catch (IOException e) {
      log.error("로그인 정보를 읽어오는데 실패했습니다.", e);
      throw new BadCredentialsException("로그인 정보를 읽어오는데 실패했습니다.");
    }
  }

  /**
   * 로그인 요청 DTO
   *
   * @param email
   * @param password
   */
  private record LoginRequestDTO(String email, String password) {
  }
}
