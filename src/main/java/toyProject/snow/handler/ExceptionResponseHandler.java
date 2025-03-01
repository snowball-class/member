package toyProject.snow.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toyProject.snow.dto.ApiResponse;

import java.security.SignatureException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionResponseHandler {

    // badRequest() : 400
    @ExceptionHandler({IllegalAccessError.class, NoSuchElementException.class})
    public ResponseEntity<ApiResponse> handleCommonException(Exception e){
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    /*
     회원가입 중복관련
     중복된 이메일
     */
    @ExceptionHandler({EmailDuplicatedException.class})
    public ResponseEntity<ApiResponse> handleEmailDuplicatedException(EmailDuplicatedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
    } //"이미 사용중인 이메일입니다."

    @ExceptionHandler({NickNameDuplicatedException.class})
    public ResponseEntity<ApiResponse> handleNickNameDuplicatedException(NickNameDuplicatedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
    } //"이미 사용중인 닉네임입니다."

    @ExceptionHandler({passwordNotMatchException.class})
    public ResponseEntity<ApiResponse> handlepasswordNotMatchException(passwordNotMatchException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
    } //"비밀번호가 일치하지 않습니다."

    /*
     HttpStatus.UNAUTHORIZED : 401
     토큰 오류
     */
    @ExceptionHandler({SignatureException.class, JwtException.class})
    public ResponseEntity<ApiResponse> handleSignatureTokenException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 유효하지 않습니다."));
    }

    @ExceptionHandler({MalformedJwtException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiResponse> handleInvalidTokenException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("올바르지 않은 토큰입니다."));
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ApiResponse> handleExpiredTokenException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 만료되었습니다."));
    }


    // 커스텀 예외 정의
    public static class EmailDuplicatedException extends RuntimeException {
        public EmailDuplicatedException(String message) {
            super(message);
        }
    }

    public static class NickNameDuplicatedException extends RuntimeException {
        public NickNameDuplicatedException(String message) {
            super(message);
        }
    }

    public static class passwordNotMatchException extends RuntimeException {
        public passwordNotMatchException(String message) {
            super(message);
        }
    }

}


