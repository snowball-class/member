package toyProject.snow.dto.join.joinResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import toyProject.snow.entity.MemberType;

import java.sql.Timestamp;

@Getter
@Setter
public class JoinResponse {

    @Schema(description = "회원이름", example = "홍길동")
    private String name;
    @Schema(description = "회원닉네임", example = "동동이")
    private String nickname;
    @Schema(description = "회원역할", example = "ADMIN, ROLE_MEMBER")
    private MemberType memberType;

    public JoinResponse(){
    }

    public JoinResponse(String name, String nickname, MemberType memberType){
        this.name = name;
        this.nickname = nickname;
        this.memberType = memberType;
    }

}
