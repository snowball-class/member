package toyProject.snow.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// security 0.12.3 ver
@Component
public class JWTUtil {

    private SecretKey secretKey; // 객체키 만듬

    private JWTUtil(@Value("${spring.jwt.secret}")String secret){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS512.key().build().getAlgorithm());
    }
    
    // 검증 진행 메소드 4개
    public String getMemberUUID(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberUUID", String.class);
    }

    public String getEmail(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getMemberType(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberType", String.class);
    }

    public String getTokenType(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("tokenType", String.class);
    }

    // 토큰 만료 확인
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
    
    // 토큰 생성 : 로그인 성공 시 토큰 생성해서 응답
    public String createJwt(String tokenType, String memberUUID, String email, String memberType, Long expiredMs){
        return Jwts.builder()
                .claim("tokenType", tokenType)
                .setSubject(memberUUID) // 여기서 subject를 설정
                .claim("email", email)
                .claim("memberType", memberType)
                .issuedAt(new Date(System.currentTimeMillis())) // 현재 발행 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    // 토큰 검증 메서드 추가
    public boolean validateToken(String token) {
        try {
            // 토큰 파싱 시도가 성공하면 유효한 토큰으로 간주
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 잘못되었거나 만료된 경우 false 반환
            return false;
        }
    }

}
