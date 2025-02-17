package toyProject.snow.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import toyProject.snow.dto.ApiResponse;

import java.security.SignatureException;
import java.util.NoSuchElementException;

@RestController
public class ExceptionResponseHandler {

    // badRequest() : 400
    @ExceptionHandler({IllegalAccessError.class, NoSuchElementException.class})
    public ResponseEntity<ApiResponse> handleCommonException(Exception e){
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    /*
     HttpStatus.UNAUTHORIZED : 401
     토큰 오류
     */
    @ExceptionHandler({SignatureException.class})
    public ResponseEntity<ApiResponse> handleSignatureTokenException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 유효하지 않습니다."));
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<ApiResponse> handleInvalidTokenException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("올바르지 않은 토큰입니다."));
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ApiResponse> handleExpiredTokenException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 만료되었습니다."));
    }

}
