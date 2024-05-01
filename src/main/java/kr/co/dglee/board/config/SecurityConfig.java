package kr.co.dglee.board.config;

import java.util.Collections;
import kr.co.dglee.board.auth.service.AuthService;
import kr.co.dglee.board.auth.filter.CustomAuthenticationFilter;
import kr.co.dglee.board.auth.handler.CustomAuthFailureHandler;
import kr.co.dglee.board.auth.handler.CustomAuthSuccessHandler;
import kr.co.dglee.board.auth.provider.CustomAuthenticationProvider;
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
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

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
  public CustomAuthenticationFilter customAuthenticationFilter(
      AuthenticationManager authenticationManager,
      CustomAuthSuccessHandler customAuthSuccessHandler,
      CustomAuthFailureHandler customAuthFailureHandler
  ) {
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);

    // 엔드포인트로 들어오는 요청을 CustomAuthenticationFilter에서 처리하도록 지정한다.
    customAuthenticationFilter.setFilterProcessesUrl("/auth/token");
    customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthSuccessHandler);
    customAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailureHandler);
    customAuthenticationFilter.afterPropertiesSet();

    return customAuthenticationFilter;
  }

  /**
   * 4. Spring Security 기반의 사용자의 정보가 맞을 경우 수행이 되며 결과값을 리턴해주는 Handler customLoginSuccessHandler: 이 메서드는 인증 성공 핸들러를 생성한다. 인증 성공 핸들러는 인증 성공시 수행할 작업을
   * 정의한다.
   */
  @Bean
  public CustomAuthSuccessHandler customLoginSuccessHandler() {
    return new CustomAuthSuccessHandler();
  }

  /**
   * 5. Spring Security 기반의 사용자의 정보가 맞지 않을 경우 수행이 되며 결과값을 리턴해주는 Handler customLoginFailureHandler: 이 메서드는 인증 실패 핸들러를 생성한다. 인증 실패 핸들러는 인증 실패시 수행할 작업을
   * 정의한다.
   */
  @Bean
  public CustomAuthFailureHandler customLoginFailureHandler() {
    return new CustomAuthFailureHandler();
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
  public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationFilter authenticationFilter)
      throws Exception {
    http
        // CSRF 비활성화
        .csrf(AbstractHttpConfigurer::disable)

        // 기본 인증 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)

        // 인증 필터 추가
        .addFilterBefore(authenticationFilter, CustomAuthenticationFilter.class)

        // 세션 비활성화
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .headers(
            headers ->
                headers.frameOptions(
                    HeadersConfigurer.FrameOptionsConfig::sameOrigin
                )
        )

        // CORS 설정
        .cors(
            cors -> cors.configurationSource(request -> {
                  var config = new org.springframework.web.cors.CorsConfiguration();
                  config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                  config.setAllowedMethods(java.util.List.of("*"));
                  config.setAllowedHeaders(java.util.List.of("*"));
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
