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
import toyProject.snow.jwt.JWTUtil;


// 리팩토링 필요! 1. 로직 서비스단으로 빼기, 2. 전역 exception 만들기
@Controller
@ResponseBody
public class TokenReissueController {

    private final JWTUtil jwtUtil;

    public TokenReissueController(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
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

        // 페이로드에 넣어서 토큰 반환
        String email = jwtUtil.getEmail(refreshToken);
        String memberType = jwtUtil.getMemberType(refreshToken);

        String newAccessToken = jwtUtil.createJwt("access", email, memberType, 6000000L);

        response.setHeader("access", newAccessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
