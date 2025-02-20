package toyProject.snow.dto.member.memberResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MemberUpdateResponse {

    @Schema(description = "회원정보수정 성공 여부", example = "true")
    private Boolean result;
    @Schema(description = "", example = "true")
    private String newNickname;

    public MemberUpdateResponse(){}

    public MemberUpdateResponse(Boolean result){
        this.result = result;
    }

    public MemberUpdateResponse(String newNickname){
        this.newNickname = newNickname;
    }

    public MemberUpdateResponse(Boolean result, String newNickname){
        this.result = result;
        this.newNickname = newNickname;
    }
}
