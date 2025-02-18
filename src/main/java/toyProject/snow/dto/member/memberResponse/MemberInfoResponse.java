package toyProject.snow.dto.member.memberResponse;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
public class MemberInfoResponse {

    @Schema(description = "회원이름", example = "홍길동")
    private String name;
    @Schema(description = "회원닉네임", example = "동에번쩍서에번쩍")
    private String nickname;
    @Schema(description = "회원이메일", example = "gildong@naver.com")
    private String email;
    @Schema(description = "가입일", example = "2025-02-18")
    private Timestamp joinDate;

    public MemberInfoResponse(){
    }

    public MemberInfoResponse(String name, String nickname, String email, Timestamp joinDate){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.joinDate = joinDate;
    }
}
