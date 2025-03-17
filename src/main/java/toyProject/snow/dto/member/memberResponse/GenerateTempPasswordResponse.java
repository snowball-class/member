package toyProject.snow.dto.member.memberResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateTempPasswordResponse {

    @Schema(description = "임시비밀번호 발급 성공 여부", example = "true")
    private Boolean result;

    public GenerateTempPasswordResponse(){
    }

    public GenerateTempPasswordResponse(Boolean result){
        this.result = result;
    }
}
