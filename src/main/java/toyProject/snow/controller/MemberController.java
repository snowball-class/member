package toyProject.snow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import toyProject.snow.dto.ApiResponse;
import toyProject.snow.dto.CustomMemberDetails;
import toyProject.snow.dto.member.memberRequest.MemberUpdateRequest;
import toyProject.snow.service.MemberService;

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
    public ApiResponse updateMember(@AuthenticationPrincipal CustomMemberDetails member, @RequestBody MemberUpdateRequest request){
        return ApiResponse.success(memberService.updateMember(member.getMemberUUID(), request));
    }

    /*
      임시비밀번호발급
    */
    @Operation(summary = "임시비밀번호발급")
    @GetMapping("/temporary-password")
    public ApiResponse generateTemporaryPassword(@AuthenticationPrincipal CustomMemberDetails member){
        return ApiResponse.success("임시 비밀번호 전송 성공", memberService.generateTemporaryPassword(member.getMemberUUID()));
    }
}
