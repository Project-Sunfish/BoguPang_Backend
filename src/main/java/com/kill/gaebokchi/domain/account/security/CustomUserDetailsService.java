package com.kill.gaebokchi.domain.account.security;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.repository.MemberRepository;
import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private  final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member principal = memberRepository.findByEmail(email).get();
        if(principal==null){
            throw new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_EMAIL);
        }
        return new CustomUserDetails(principal);
    }
}
