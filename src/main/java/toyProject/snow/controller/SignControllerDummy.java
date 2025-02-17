package toyProject.snow.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignControllerDummy {

    @Operation(summary = "로그인", description = "실제 로그아웃 처리는 LoginFilter에서 수행됩니다.")
    @PostMapping("/login")
    public String dummylogin(){
        // 이 메서드는 Swagger에만 표시되며, 실제 로직은 필터에서 처리됩니다.
        return "dummy login";
    }

    @Operation(summary = "로그아웃", description = "실제 로그아웃 처리는 CustomLogoutFilter에서 수행됩니다.")
    @PostMapping("/logout")
    public String dummylogout(){
        // 이 메서드는 Swagger에만 표시되며, 실제 로직은 필터에서 처리됩니다.
        return "dummy logout";
    }

}
