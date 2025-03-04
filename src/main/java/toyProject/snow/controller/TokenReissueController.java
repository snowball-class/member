package toyProject.snow.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import toyProject.snow.entity.RefreshToken;
import toyProject.snow.jwt.JWTUtil;
import toyProject.snow.repository.RefreshTokenRepository;

import java.util.Date;
import java.util.UUID;


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

    @Operation(summary = "리프레시토큰재발급")
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
        String memberUUID = jwtUtil.getMemberUUID(refreshToken);
        String email = jwtUtil.getEmail(refreshToken);
        String memberType = jwtUtil.getMemberType(refreshToken);
        
        String newAccessToken = jwtUtil.createJwt("access", memberUUID, email, memberType, 6000000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", memberUUID, email, memberType, 864000000L);
        
        response.setHeader("access", newAccessToken);
        response.addCookie(createCookies("refresh", newRefreshToken));

        // DB에 기존 refresh 토큰 삭제 후, 새 refresh 토큰으로 저장
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
        saveRefreshTokenEntity(memberUUID, newRefreshToken, 86400000L);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void saveRefreshTokenEntity(String memberUUID, String refreshToken, Long expiredMs){

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshtoken = new RefreshToken();
        refreshtoken.setMemberUUID(UUID.fromString(memberUUID));
        refreshtoken.setRefreshToken(refreshToken);
        refreshtoken.setExpirationTime(date.toString());

        refreshTokenRepository.save(refreshtoken);
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

/*
    access 토큰이 만료되었다는 연락이 오면
    accesss 토큰이 만료되었다는 예외가 발생(ex 400 응답)
    예외 핸들러 코드를 통해서
    리프레시 토큰을 다시 서버측으로 전송
    그럼 서버측에서 리프레시 토큰을 받아서
    새로운 access 토큰을 만드는 로직에서
    리프레시 토큰을 검증하고
    새로운 access 토큰을 만들어서 주면서, refresh 토큰도 새로 만들어줌
    (생명주기가 긴 refresh은 발급 시 db에 저장해서 해커에게 탈취되더라도
    db에 저장된 토큰만 사용가능하도록)
    
    로그아웃하면 이 토큰들을 지워줌 = 토큰 탈취 되더라도 공격 못함

    Redis를 사용하면 생명주기 끝난 토큰 자동 삭제가능하나
    RDB로 진행하면 스케줄 만들어줘야

    로그인 지속시간 길어짐
    보안 강화

    발급한 refresh 토큰을 해킹해서 refresh rotate 하려고 할 때
    rotate 이전 refresh 토큰을 가지고 /reissue로 오면  사용하지 못하도록
 */