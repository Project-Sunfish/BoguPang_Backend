package com.kill.gaebokchi.domain.user.service;

import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.domain.user.entity.TypeFlag;
import com.kill.gaebokchi.domain.user.entity.dto.JoinDTO;
import com.kill.gaebokchi.domain.user.repository.MemberRepository;
import com.kill.gaebokchi.domain.user.repository.TypeFlagRepository;
import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.kill.gaebokchi.global.error.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TypeFlagRepository typeFlagRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long join(JoinDTO joinDTO){
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = memberRepository.existsByUsername(username);

        if(isExist) return null;

        Member entity = new Member();
        entity.setUsername(username);
        entity.setPassword(bCryptPasswordEncoder.encode(password));
        entity.setRole("ROLE_ADMIN");
        TypeFlag typeFlag = new TypeFlag();
        List<Boolean> flag = new ArrayList<>(Collections.nCopies(23, false));
        typeFlag.setNewFlag(flag);
        typeFlag.setLiberatedFlag(flag);

        typeFlagRepository.save(typeFlag);
        entity.setFlag(typeFlag);
        memberRepository.save(entity);


        return entity.getId();
    }

    public Member findMemberByUsername(String username){
        return memberRepository.findByUsername(username).orElseThrow(()-> new BadRequestException(NOT_FOUND_MEMBER_ID));
    }
}
