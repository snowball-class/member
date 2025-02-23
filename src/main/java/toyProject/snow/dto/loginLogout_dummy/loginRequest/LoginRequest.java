package toyProject.snow.dto.loginLogout_dummy.loginRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @Schema(description = "이메일", example = "gildong@naver.com")
    private String email;
    @Schema(description = "비밀번호", example = "1234")
    private String password;

    public LoginRequest(){
    }
    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
}