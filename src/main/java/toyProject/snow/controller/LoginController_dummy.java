package toyProject.snow.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toyProject.snow.dto.ApiResponse;
import toyProject.snow.dto.loginLogout_dummy.loginRequest.LoginRequest;
import toyProject.snow.dto.loginLogout_dummy.logoutResponse.LogoutResponse;

@RestController
public class LoginController_dummy {


    @Operation(summary = "로그인", description = "실제 로그인 처리는 /login 경로로 스프링 시큐리티 필터(LoginFilter)에서 처리")
    @PostMapping(value = "/login_dummy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse login(@RequestBody LoginRequest loginRequest){
        // 이 메서드는 Swagger에만 표시되며, 실제 로직은 필터에서 처리됩니다.
        return ApiResponse.success(loginRequest);
    }

    @Operation(summary = "로그아웃", description = "Swagger 문서 작성을 위한 더미. 실제 로그아웃 처리는 /logout 경로로 CustomLogoutFilter에서 처리")
    @PostMapping("/logout_dummy")
    public ApiResponse logout(@RequestBody LoginRequest loginRequest){
        // 이 메서드는 Swagger에만 표시되며, 실제 로직은 필터에서 처리됩니다.
        return ApiResponse.success(new LogoutResponse(true));
    }

}
