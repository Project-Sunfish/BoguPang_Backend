package com.kill.gaebokchi.domain.user.service;

import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.domain.user.jwt.CustomUserDetails;
import com.kill.gaebokchi.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private  final MemberService memberService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member entity = memberService.findMemberByUsername(username);
        if(entity!=null){
            //userDetails에 담기 -> AuthenticationManger가 이를 검증함
            return new CustomUserDetails(entity);
        }
        return null;
    }
}
