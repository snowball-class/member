package toyProject.snow.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class JoinDTO {

    private String name;
    private String nickname;
    private String email;
    private String password;
}
