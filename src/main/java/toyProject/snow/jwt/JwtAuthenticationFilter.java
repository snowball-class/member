package toyProject.snow.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import toyProject.snow.service.CustomMemberDetailsService;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final CustomMemberDetailsService customMemberDetailsService;

    public JwtAuthenticationFilter(JWTUtil jwtUtil, CustomMemberDetailsService customMemberDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customMemberDetailsService = customMemberDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        String path = request.getServletPath();
        // Public 엔드포인트인 경우 필터 로직을 건너뜁니다.
        if (path.equals("/join") || path.equals("/login") ||
                path.startsWith("/swagger-ui") || path.startsWith("/v3") ||
                path.equals("/loginDummy") || path.equals("/logoutDummy")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim(); // "Bearer " 제거
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 이메일(또는 식별자) 추출
                String email = jwtUtil.getEmail(token);
                // UserDetailsService를 사용하여 CustomMemberDetails 로드
                UserDetails userDetails = customMemberDetailsService.loadUserByUsername(email);
                // Authentication 객체 생성 (비밀번호는 null, 토큰이 이미 인증되었으므로)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}