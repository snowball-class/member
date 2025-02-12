package toyProject.snow.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;
import toyProject.snow.repository.RefreshTokenRepository;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {

        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // path and method verify
        String requestURI = request.getRequestURI();
        if(!requestURI.matches("^\\/logout$")){
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if(!requestMethod.equals("POST")){
            filterChain.doFilter(request, response);
            return;
        }

        // get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }

        // refresh token null check
        if(refreshToken == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // expired check
        try{
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인(발급 시 페이로드에 적힌)
        String tokenType = jwtUtil.getTokenType(refreshToken);
        if(!tokenType.equals("refresh")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DB 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if(!isExist){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        // 로그아웃 진행
        // refresh token을 db에서 제거
        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        // refresh token의 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
