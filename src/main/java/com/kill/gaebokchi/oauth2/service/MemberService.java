package com.kill.gaebokchi.oauth2.service;

import com.kill.gaebokchi.entity.Member;
import com.kill.gaebokchi.entity.Role;
import com.kill.gaebokchi.entity.dto.UserSignUpDto;
import com.kill.gaebokchi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(String email, UserSignUpDto userSignUpDto) throws Exception{
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        optionalMember.ifPresent(member->{
            member.setGender(userSignUpDto.getGender());
            member.setNickname(userSignUpDto.getNickname());
            member.setBirth(userSignUpDto.getBirth());
            member.setRole(Role.USER);
        });
    }
}
