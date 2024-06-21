package com.kill.gaebokchi.domain.account.service;

import com.kill.gaebokchi.domain.account.dto.request.SignUpRequestDTO;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.entity.Role;
import com.kill.gaebokchi.domain.account.repository.MemberRepository;
import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import com.kill.gaebokchi.domain.bogu.repository.DefaultBoguRepository;
import com.kill.gaebokchi.domain.bogu.repository.EvolvedBoguRepository;
import com.kill.gaebokchi.domain.bogu.service.DefaultBoguService;
import com.kill.gaebokchi.global.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kill.gaebokchi.global.error.ExceptionCode.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final EvolvedBoguRepository evolvedBoguRepository;
    private final DefaultBoguRepository defaultBoguRepository;

    public Member findMemberByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(()-> new BadRequestException(NOT_FOUND_MEMBER_EMAIL));
    }

    @Transactional
    public void updateMember(Member member, SignUpRequestDTO request){
        if(request.hasNullFields()){
            throw new BadRequestException(INVALID_SIGNUP_FORM);
        }
        if(member.getRole()== Role.ROLE_GUEST){
            throw new BadRequestException(USER_ROLE_GUEST);
        }
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setBirthType(request.getBirthType());
        member.setBirth(request.getBirth());
    }
    @Transactional
    @CacheEvict(value={"member", "evolvedBogu"})
    public void deleteMember(Member member){
        List<EvolvedBogu> evolvedBoguList = evolvedBoguRepository.findByHost(member);
        for(EvolvedBogu evolvedBogu : evolvedBoguList){
            evolvedBoguRepository.delete(evolvedBogu);
        }
        List<DefaultBogu> defaultBoguList = defaultBoguRepository.findByHost(member);
        for (DefaultBogu defaultBogu : defaultBoguList) {
            defaultBoguRepository.delete(defaultBogu);
        }
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
