package com.kill.gaebokchi.domain.account.service;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.repository.MemberRepository;
import com.kill.gaebokchi.domain.account.repository.TypeFlagRepository;
import com.kill.gaebokchi.global.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kill.gaebokchi.global.error.ExceptionCode.NOT_FOUND_MEMBER_EMAIL;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TypeFlagRepository typeFlagRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(()-> new BadRequestException(NOT_FOUND_MEMBER_EMAIL));
    }

}
