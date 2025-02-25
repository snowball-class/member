package toyProject.snow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> ALLOWED_ORIGIN = List.of("http://localhost:3000");

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(ALLOWED_ORIGIN); // 허용할 Origin 설정
        // configuration.setAllowedOriginPatterns(ALLOWED_ORIGIN_PATTERN); // 허용할 Origin 패턴 설정
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드 지정
        corsConfig.setAllowedHeaders(Collections.singletonList("*")); // 헤더
        corsConfig.setAllowCredentials(true); // 자격 증명(쿠키 등) 허용 여부 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // 모든 경로에 대해 CORS 설정 적용

        return source;
    }
}
