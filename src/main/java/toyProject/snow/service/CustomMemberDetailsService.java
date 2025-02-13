package toyProject.snow.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toyProject.snow.dto.CustomMemberDetails;
import toyProject.snow.entity.MemberEntity;
import toyProject.snow.repository.MemberRepository;

@Service
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomMemberDetailsService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        MemberEntity memberEntity = memberRepository.findByEmail(email);

        if(memberEntity != null){
            return new CustomMemberDetails(memberEntity);
        }

        return null;
    }
}
