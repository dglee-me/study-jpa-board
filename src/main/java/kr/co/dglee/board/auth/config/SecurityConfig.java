package kr.co.dglee.board.auth.config;

import java.util.Collections;
import java.util.List;
import kr.co.dglee.board.auth.filter.AuthenticationFilter;
import kr.co.dglee.board.auth.filter.JwtAuthenticationFilter;
import kr.co.dglee.board.auth.handler.AuthFailureHandler;
import kr.co.dglee.board.auth.handler.AuthSuccessHandler;
import kr.co.dglee.board.auth.provider.CustomAuthenticationProvider;
import kr.co.dglee.board.auth.service.AuthService;
import kr.co.dglee.board.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtUtil jwtUtil;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CustomAuthenticationProvider customAuthenticationProvider(AuthService authService) {
    return new CustomAuthenticationProvider(authService, passwordEncoder());
  }

  @Bean
  public AuthenticationManager authenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
    return new ProviderManager(customAuthenticationProvider);
  }

  @Bean
  public AuthenticationFilter customAuthenticationFilter(
      AuthenticationManager authenticationManager,
      AuthSuccessHandler authSuccessHandler,
      AuthFailureHandler authFailureHandler
  ) {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);

    // 엔드포인트로 들어오는 요청을 CustomAuthenticationFilter에서 처리하도록 지정한다.
    authenticationFilter.setFilterProcessesUrl("/auth/token");
    authenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler);
    authenticationFilter.setAuthenticationFailureHandler(authFailureHandler);

    authenticationFilter.afterPropertiesSet();

    return authenticationFilter;
  }

  /**
   * 4. Spring Security 기반의 사용자의 정보가 맞을 경우 수행이 되며 결과값을 리턴해주는 Handler customLoginSuccessHandler: 이 메서드는 인증 성공 핸들러를 생성한다. 인증 성공 핸들러는 인증 성공시 수행할 작업을
   * 정의한다.
   */
  @Bean
  public AuthSuccessHandler customLoginSuccessHandler() {
    return new AuthSuccessHandler(jwtUtil);
  }

  /**
   * 5. Spring Security 기반의 사용자의 정보가 맞지 않을 경우 수행이 되며 결과값을 리턴해주는 Handler customLoginFailureHandler: 이 메서드는 인증 실패 핸들러를 생성한다. 인증 실패 핸들러는 인증 실패시 수행할 작업을
   * 정의한다.
   */
  @Bean
  public AuthFailureHandler customLoginFailureHandler() {
    return new AuthFailureHandler();
  }


  /**
   * 스프링시큐리티의 보안 설정에 적용받지 않을 URL 을 등록한다.
   * <p>
   * 정적 리소스(이미지, CSS, JavaScript) h2-console
   *
   * @return WebSecurityCustomizer
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console())
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationFilter authenticationFilter)
      throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화

        // 인증 필터 추가
        .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), AuthenticationFilter.class)
        .addFilterAt(authenticationFilter, AuthenticationFilter.class)

        .headers(
            headers ->
                headers.frameOptions(
                    FrameOptionsConfig::sameOrigin
                )
        )

        // CORS 설정
        .cors(
            cors -> cors.configurationSource(request -> {
                  var config = new CorsConfiguration();
                  config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                  config.setAllowedMethods(List.of("*"));
                  config.setAllowedHeaders(List.of("*"));
                  config.setAllowCredentials(true);
                  config.setMaxAge(3600L);
                  return config;
                }
            )
        )

        // 인증되지 않은 사용자는 아래 URL만 접근 가능
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/auth/token").permitAll()
            .anyRequest().authenticated());

    return http.build();
  }
}
