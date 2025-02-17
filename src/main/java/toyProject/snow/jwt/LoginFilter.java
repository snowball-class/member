package toyProject.snow.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import toyProject.snow.dto.CustomMemberDetails;
import toyProject.snow.entity.RefreshTokenEntity;
import toyProject.snow.repository.RefreshTokenRepository;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    private RefreshTokenRepository refreshTokenRepository;


    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws ArithmeticException {

        //클라이언트 요청에서 username, password 추출
        String email = obtainUsername(request); // key 값이 username일 때만 받을 수 있다고? 나중에 뷰단 커스텀하거나 메소드 오버라이딩 해야함
        String password = obtainPassword(request);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        //로그인 검증 - db에 있는 id랑 비번 가져와서 맞춰봄
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){

        // 유저 정보
        String email = authentication.getName();

        CustomMemberDetails userDetails = (CustomMemberDetails) authentication.getPrincipal();
        String memberUUID = userDetails.getMemberUUID().toString();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String memberType = auth.getAuthority();

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access", memberUUID, email, memberType, 6000000L);
        String refreshToken = jwtUtil.createJwt("refresh", memberUUID, email, memberType, 86400000L);

        // refresh 토큰 DB 저장
        saveRefreshTokenEntity(memberUUID, refreshToken, 86400000L);

        // 응답 생성
        response.setHeader("access", accessToken);
        response.addCookie(createCookies("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    private void saveRefreshTokenEntity(String memberUUID, String refreshToken, Long expiredMs){

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setMemberUUID(UUID.fromString(memberUUID));
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpirationTime(date.toString());

        refreshTokenRepository.save(refreshTokenEntity);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }


    private Cookie createCookies(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
//        cookie.setSecure(true); // https 통신 쓸 때
//        cookie.setPath("/"); // 쿠키 접근 범위
        cookie.setHttpOnly(true);

        return cookie;
    }

}
