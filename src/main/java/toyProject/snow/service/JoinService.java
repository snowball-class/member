package toyProject.snow.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import toyProject.snow.dto.join.joinRequest.JoinRquest;
import toyProject.snow.entity.MemberEntity;
import toyProject.snow.entity.MemberType;
import toyProject.snow.repository.MemberRepository;

@Service
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // boolean으로 회원가입 성공하면 true, 실패하면 false go
    public boolean join(JoinRquest joinRquest){

        String name = joinRquest.getName();
        String nickname = joinRquest.getNickname();
        String email = joinRquest.getEmail();
        String password = joinRquest.getPassword();

        Boolean isExist = memberRepository.existsByEmail(email);

        if(isExist){
            return false;
        }

        MemberEntity newMember = new MemberEntity();
        newMember.setName(name);
        newMember.setNickname(nickname);
        newMember.setEmail(email);
        newMember.setMemberType(MemberType.ROLE_MEMBER);
        newMember.setPassword(bCryptPasswordEncoder.encode(password));

        memberRepository.save(newMember);

        return true;
    }
}
