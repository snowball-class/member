package toyProject.snow.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import toyProject.snow.dto.ApiResponse;
import toyProject.snow.dto.CustomMemberDetails;
import toyProject.snow.entity.MemberEntity;
import toyProject.snow.service.MemberService;


import java.util.UUID;

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    /*
      회원정보조회
     */
    @Operation(summary = "회원정보조회")
    @GetMapping
    public ApiResponse getMemberInfo(@AuthenticationPrincipal CustomMemberDetails member){
        return ApiResponse.success(memberService.getMemberInfo(member.getMemberUUID()));
    }

    /*
      회원탈퇴
    */
    @Operation(summary = "회원탈퇴")
    @DeleteMapping
    public ApiResponse deleteMember(@AuthenticationPrincipal CustomMemberDetails member){
        return ApiResponse.success(memberService.deleteMember(member.getMemberUUID()));
    }

    /*
      회원정보수정
    */
    @Operation(summary = "회원정보수정")
    @PutMapping
    public ApiResponse updateMember(@AuthenticationPrincipal User user){
        return null; // ApiResponse.success(memberService.updateMember(UUID.fromString(user.getUsername())));
    }
}
