package kr.co.dglee.board.auth.provider;

import kr.co.dglee.board.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * AuthenticationProvider 인터페이스를 구현한 클래스
 * <p>
 * 사용자가 입력한 로그인 정보를 검증하는 역할을 한다.
 */
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final AuthService authService;

  private final PasswordEncoder passwordEncoder;

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

    final String loginId = token.getName();
    final String userPassword = (String) token.getCredentials();

    UserDetails user = authService.loadUserByUsername(loginId);

    if (!passwordEncoder.matches(userPassword, user.getPassword())) {
      throw new BadCredentialsException(user.getUsername() + " Invalid password");
    }

    return new UsernamePasswordAuthenticationToken(user, userPassword, user.getAuthorities());
  }
}
