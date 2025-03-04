package toyProject.snow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import toyProject.snow.dto.ApiResponse;
import toyProject.snow.dto.CustomMemberDetails;
import toyProject.snow.dto.loginLogout_dummy.loginRequest.LoginRequest;
import toyProject.snow.dto.loginLogout_dummy.logoutResponse.LogoutResponse;
import toyProject.snow.dto.member.memberRequest.MemberUpdateRequest;
import toyProject.snow.service.MemberService;

@Tag(name = "Dummy Controller")
@RestController
public class Controller_dummy {

//    private final MemberService memberService;
//
//    public Controller_dummy(MemberService memberService){
//        this.memberService = memberService;
//    }


    @Operation(summary = "로그인", description = "실제 로그인 처리는 /login 경로로 스프링 시큐리티 필터(LoginFilter)에서 처리")
    @PostMapping(value = "/login_dummy", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse login(@ModelAttribute LoginRequest loginRequest){
        // 이 메서드는 Swagger에만 표시되며, 실제 로직은 필터에서 처리됩니다.
        return ApiResponse.success(loginRequest);
    }

    @Operation(summary = "로그아웃", description = "Swagger 문서 작성을 위한 더미. 실제 로그아웃 처리는 /logout 경로로 CustomLogoutFilter에서 처리")
    @PostMapping("/logout_dummy")
    public ApiResponse logout(){
        // 이 메서드는 Swagger에만 표시되며, 실제 로직은 필터에서 처리됩니다.
        return ApiResponse.success(new LogoutResponse(true));
    }

//    /*
//  회원정보조회
// */
//    @Operation(summary = "회원정보조회", description = "실제 로그인(/login) 후 사용가능")
//    @GetMapping("/dummmy/member")
//    public ApiResponse getMemberInfo(@AuthenticationPrincipal CustomMemberDetails member){
//        return ApiResponse.success(memberService.getMemberInfo(member.getMemberUUID()));
//    }
//
//    /*
//      회원탈퇴
//    */
//    @Operation(summary = "회원탈퇴", description = "실제 로그인(/login) 후 사용가능")
//    @DeleteMapping("/dummmy/member")
//    public ApiResponse deleteMember(@AuthenticationPrincipal CustomMemberDetails member){
//        return ApiResponse.success(memberService.deleteMember(member.getMemberUUID()));
//    }
//
//    /*
//      회원정보수정
//    */
//    @Operation(summary = "회원정보수정", description = "실제 로그인(/login) 후 사용가능")
//    @PutMapping("/dummmy/member")
//    public ApiResponse updateMember(@AuthenticationPrincipal CustomMemberDetails member, @RequestBody MemberUpdateRequest request){
//        return ApiResponse.success(memberService.updateMember(member.getMemberUUID(), request));
//    }

}
