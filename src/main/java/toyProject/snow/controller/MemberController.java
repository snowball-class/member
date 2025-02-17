//package toyProject.snow.controller;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import toyProject.snow.dto.ApiResponse;
//import toyProject.snow.entity.MemberEntity;
//import toyProject.snow.service.MemberService;
//
//import java.util.UUID;
//
//@Controller
//@ResponseBody
//@RequestMapping("/member")
//public class MemberController {
//
//    private final MemberService memberService;
//
//    public MemberController(MemberService memberService){
//        this.memberService = memberService;
//    }
//
//    /*
//      회원정보조회
//     */
//    @GetMapping
//    public ApiResponse getMemberInfo(@AuthenticationPrincipal User user){
//        return ApiResponse.success(memberService.getMemberInfo(UUID.fromString(user.getUsername())));
//    }
//
//    /*
//      회원탈퇴
//    */
////    @DeleteMapping
////    public ApiResponse deleteMember(@AuthenticationPrincipal User user){
////        return ApiResponse.success(memberService.deleteMember(UUID.fromString(user.getUsername())));
////    }
//
//    /*
//      회원정보수정
//    */
////    @PutMapping
////    public ApiResponse updateMember(@AuthenticationPrincipal User user, ){
////        return ApiResponse.success(memberService.updateMember(UUID.fromString(user.getUsername())));
////    }
//}
