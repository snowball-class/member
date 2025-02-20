package toyProject.snow.dto.member.memberResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MemberDeletetResponse {
    @Schema(description = "회원 삭제 성공 여부", example = "true")
    private Boolean result;

    public MemberDeletetResponse(){
    }

    public MemberDeletetResponse(Boolean result){
        this.result = result;
    }
}
