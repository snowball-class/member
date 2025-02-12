package toyProject.snow.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import toyProject.snow.entity.RefreshTokenEntity;
import toyProject.snow.jwt.JWTUtil;
import toyProject.snow.repository.RefreshTokenRepository;

import java.util.Date;


// 리팩토링 필요! 1. 로직 서비스단으로 빼기, 2. 전역 exception 만들기 3. refresh rotate 부분 빼기 4. Cookie 만드는 부분...
@Controller
@ResponseBody
public class TokenReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenReissueController(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository){
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){

        // get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }

        //response status code
        if(refreshToken == null){

            // 1. 토큰이 null
            return new ResponseEntity<>("refresh token is null", HttpStatus.BAD_REQUEST);
        }

        // 2. 토큰이 만료
        try{
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e){

            return new ResponseEntity<>("expired refresh token", HttpStatus.BAD_REQUEST);
        }

        // 3. refresh 토큰인지 확인
        String tokenType = jwtUtil.getTokenType(refreshToken);

        if(!tokenType.equals("refresh")){
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // refresh 토큰 DB에 저장되어 있는지 확인
        Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
        if(!isExist){
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 페이로드에 넣어서 토큰 반환
        String email = jwtUtil.getEmail(refreshToken);
        String memberType = jwtUtil.getMemberType(refreshToken);
        
        String newAccessToken = jwtUtil.createJwt("access", email, memberType, 6000000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", email, memberType, 864000000L);
        
        response.setHeader("access", newAccessToken);
        response.addCookie(createCookies("refresh", newRefreshToken));

        // DB에 기존 refresh 토큰 삭제 후, 새 refresh 토큰으로 저장
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        saveRefreshTokenEntity(email, newRefreshToken, 86400000L);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void saveRefreshTokenEntity(String email, String refreshToken, Long expiredMs){

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setEmail(email);
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpirationTime(date.toString());

        refreshTokenRepository.save(refreshTokenEntity);
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