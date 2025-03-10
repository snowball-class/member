package toyProject.snow.dto.member.memberResponse;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class MemberInfoResponse {

    @Schema(description = "회원정보조회 성공 여부", example = "true")
    private Boolean result;

    @Schema(description = "회원ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID memberUUID;
    @Schema(description = "회원이름", example = "홍길동")
    private String name;
    @Schema(description = "회원닉네임", example = "동에번쩍서에번쩍")
    private String nickname;
    @Schema(description = "회원이메일", example = "gildong@naver.com")
    private String email;
    @Schema(description = "가입일", example = "2025-02-18")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime joinDate;

    public MemberInfoResponse(){
    }

    public MemberInfoResponse(Boolean result){
        this.result = result;
    }

    public MemberInfoResponse(Boolean result, UUID memberUUID, String name, String nickname, String email, LocalDateTime joinDate){
        this.result = result;
        this.memberUUID = memberUUID;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.joinDate = joinDate;
    }
}
