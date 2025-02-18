package toyProject.snow.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import toyProject.snow.dto.CustomMemberDetails;
import toyProject.snow.jwt.CustomLogoutFilter;
import toyProject.snow.jwt.JWTUtil;
import toyProject.snow.jwt.JwtAuthenticationFilter;
import toyProject.snow.jwt.LoginFilter;
import toyProject.snow.repository.RefreshTokenRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

//스프링부트에 configuration인 것 등록 @Configuration
//security 라는 config라는 것 @EnableWebSecurity
//클래스 내부에 특정한 메소드들을 빈으로 등록해 security 설정 진행 가능

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository, JwtAuthenticationFilter jwtAuthenticationFilter){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // authentication을 가져오는 manager을 빈으로 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //csrf disable : session에서는 session이 stateful 상태라 csrf 방어해 줘야함. jwt 방식은 stateless라 방어 필요 없음
        http
                .csrf((auth) -> auth.disable());

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfig = new CorsConfiguration();

                        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:8081"));
                        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
                        corsConfig.setAllowCredentials(true);

                        return corsConfig;
                    }
                }));

        //From 로그인 방식 disable : jwt 로그인 방식이라서
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable : jwt 로그인 방식이라서
        http
                .httpBasic((auth) -> auth.disable());

        //인가 작업 : 특정한 경로에 대해 인가 작업(.authorizeHttpRequests 이용)
        //.authenticated() : 로그인한 사람만 접속 가능
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()
                        .requestMatchers( "/swagger-ui/**", "/v3/**").permitAll()
                        .requestMatchers("/loginDummy", "logoutDummy").permitAll()
//                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().authenticated());

        // 커스터마이징한 loginFilter 추가
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenRepository), UsernamePasswordAuthenticationFilter.class);

        // 커스터마이징한 logoutFilter 추가
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);

        http
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

        //세션 설정 : jwt 방식에서는 세션이 항상 stateless로 설정, 가장 중요
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

// jwt 발급 전에 회원정보를 검증해서 jwt를 발급해야
// 회원정보를 검증하기 위해서는 id, pw 매치(유저입력-DB) 확인

