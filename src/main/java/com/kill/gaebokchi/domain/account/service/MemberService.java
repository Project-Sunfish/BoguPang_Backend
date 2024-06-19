package com.kill.gaebokchi.domain.account.service;

import com.kill.gaebokchi.domain.account.dto.request.SignUpRequestDTO;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.repository.MemberRepository;
import com.kill.gaebokchi.global.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kill.gaebokchi.global.error.ExceptionCode.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(()-> new BadRequestException(NOT_FOUND_MEMBER_EMAIL));
    }

    @Transactional
    public void updateMember(Member member, SignUpRequestDTO request){
        if(request.hasNullFields()){
            throw new BadRequestException(INVALID_SIGNUP_FORM);
        }
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setBirthType(request.getBirthType());
        member.setBirth(request.getBirth());
    }
    @Transactional
    public void deleteMember(Member member){
        memberRepository.delete(member);
    }

    @Transactional
    public void toggleTutorialFlag(Member member){
        if(member.getTutorial()){
            throw new BadRequestException(ALREADY_COMPLETE_TUTORIAL);
        }
        member.setTutorial(true);
    }
}
