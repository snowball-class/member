package toyProject.snow.dto.join.joinRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class JoinDTO {
    @Schema(description = "회원이름", example = "홍길동")
    private String name;
    @Schema(description = "회원닉네임", example = "동에번쩍서에번쩍")
    private String nickname;
    @Schema(description = "회원이메일", example = "gildong@naver.com")
    private String email;
    @Schema(description = "비밀번호", example = "1234")
    private String password;
}
