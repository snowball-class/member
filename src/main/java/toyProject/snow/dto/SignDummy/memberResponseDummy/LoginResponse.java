package toyProject.snow.dto.SignDummy.memberResponseDummy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    @Schema(description = "발급된 액세스 토큰", example = "dummy-access-token")
    private String accessToken;
    @Schema(description = "발급된 리프레시 토큰", example = "dummy-refresh-token")
    private String refreshToken;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
