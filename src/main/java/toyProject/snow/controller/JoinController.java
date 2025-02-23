package toyProject.snow.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import toyProject.snow.dto.ApiResponse;
import toyProject.snow.dto.join.joinRequest.JoinRequest;
import toyProject.snow.dto.join.joinResponse.JoinResponse;
import toyProject.snow.service.JoinService;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService){
        this.joinService = joinService;
    }

    @Operation(summary = "회원가입", description = "name, nickname, email, password를 받아 회원가입")
    @PostMapping("/join")
    public ApiResponse join(@ModelAttribute JoinRequest joinRequest){
        return ApiResponse.created(joinService.join(joinRequest));
    }

}
