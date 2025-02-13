package toyProject.snow.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import toyProject.snow.entity.MemberEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class CustomMemberDetails implements UserDetails {

    private final MemberEntity memberEntity;

    public CustomMemberDetails(MemberEntity memberEntity){
        this.memberEntity = memberEntity;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return memberEntity.getMemberType().name();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getEmail();
    }

    public UUID getMemberUUID(){
        return memberEntity.getMemberUUID();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
