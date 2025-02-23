package toyProject.snow.dto.loginLogout_dummy.logoutResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LogoutResponse {

    @Schema(description = "로그아웃 성공 여부", example = "true")
    private Boolean result;

    public LogoutResponse(){
    }

    public LogoutResponse(Boolean result){
        this.result = result;
    }
}
