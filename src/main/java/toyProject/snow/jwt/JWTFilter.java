package toyProject.snow.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // authorization 헤더 검증
        if(authorization == null || !authorization.startsWith("Bearer ")){

            System.out.println("token null");
            filterChain.doFilter(request, response); // 메소드 종료 전에 doFilter로 filterChain으로 다음 필터로 넘겨준다
            // 검증 X 시 메소드 종료(필수)
            return;
        }

        System.out.println("authorization now");
        // bearer 없애고 순수 토큰만 get

        
    }
}
