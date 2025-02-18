package toyProject.snow.dto.member.memberResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDeletetResponse {
    @Schema(description = "회원 삭제 성공 여부", example = "true")
    boolean result;

    public MemberDeletetResponse(){
    }

    public MemberDeletetResponse(Boolean result){
        this.result = result;
    }
}
