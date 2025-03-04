package toyProject.snow.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import toyProject.snow.dto.CustomMemberDetails;
import toyProject.snow.entity.Member;
import toyProject.snow.entity.MemberType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/*
JWTFilter
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public JwtAuthenticationFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access 키가 담긴 토큰 꺼내기
        String accessToken = request.getHeader("Authorization");


        // 토큰이 없으면 다음 필터로
        if(accessToken == null){
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 제거
        if(accessToken.startsWith("Bearer ")){
            accessToken = accessToken.substring(7);
        }

        // 토큰 만료 여부 확인, 만료 시 다음 필터 넘기면 X
        try{
            jwtUtil.isExpired(accessToken);
        }catch(ExpiredJwtException e){

            // responseBody -> 프론트와 상의 필요
            PrintWriter writer = response.getWriter();
            writer.println("access token expired");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // -> 프론트와 상의필요
            return;
        }

        // 토큰이 access 인지 확인(발급 시 페이로드에 명시) -> 프상필
        String tokenType = jwtUtil.getTokenType(accessToken);

        if(!tokenType.equals("access")){

            PrintWriter writer = response.getWriter();
            writer.println("invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, memberType 획득해, 일시적인 세션 만들기
        String memberUUID = jwtUtil.getMemberUUID(accessToken);
        String memberType = jwtUtil.getMemberType(accessToken);
        String email = jwtUtil.getEmail(accessToken);

        Member member = new Member();
        member.setMemberUUID(UUID.fromString(memberUUID));
        member.setMemberType(MemberType.valueOf(memberType)); // -> 나중에 enum 수정해야할 듯
        member.setEmail(email);

        CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
