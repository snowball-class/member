package toyProject.snow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import toyProject.snow.dto.SignDummy.memberRequestDummy.LoginRequest;
import toyProject.snow.dto.SignDummy.memberResponseDummy.LoginResponse;

@RestController
public class SignControllerDummy {

    @Operation(summary = "로그인", description = "실제 로그아웃 처리는 /login 경로로 LoginFilter에서 수행됩니다.")
    @PostMapping("/loginDummy")
    public ResponseEntity<LoginResponse> dummylogin(@RequestBody LoginRequest loginRequest){
        // 이 메서드는 Swagger에만 표시되며, 실제 로직은 필터에서 처리됩니다.
        LoginResponse loginResponse = new LoginResponse("dummy-access-token", "dummy-refresh-token");
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @Operation(summary = "로그아웃", description = "실제 로그아웃 처리는 /logout 경로로 CustomLogoutFilter에서 수행됩니다.")
    @PostMapping("/logoutDummy")
    public String dummylogout(@RequestBody LoginRequest loginRequest){
        // 이 메서드는 Swagger에만 표시되며, 실제 로직은 필터에서 처리됩니다.

        return "dummy logout";
    }

}
