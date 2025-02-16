package toyProject.snow.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import toyProject.snow.dto.ApiResponse;
import toyProject.snow.entity.MemberEntity;
import toyProject.snow.service.MemberService;

import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping
    public ApiResponse getMemberInfo(@AuthenticationPrincipal User user){
        return ApiResponse.success(memberService.getMemberInfo(UUID.fromString(user.getUsername())));
    }

    @DeleteMapping
    public void deleteMember(@AuthenticationPrincipal User user){
        memberService.deleteMember(UUID.fromString(user.getUsername()));
    }

    @PutMapping
    public MemberEntity updateMember(@AuthenticationPrincipal User user){
        return memberService.updateMember(UUID.fromString(user.getUsername()));
    }
}
