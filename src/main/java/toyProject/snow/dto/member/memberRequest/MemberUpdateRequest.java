package toyProject.snow.dto.member.memberRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MemberUpdateRequest {

    @Schema(description = "회원 새닉네임", example = "도술천재길동")
    private String newNickname;
    @Schema(description = "회원 비밀번호", example = "1234")
    private String password;
    @Schema(description = "회원 새 비밀번호", example = "4321")
    private String newPassword;
}
