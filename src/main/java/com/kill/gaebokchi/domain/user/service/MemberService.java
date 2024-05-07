package com.kill.gaebokchi.domain.user.service;

import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.domain.user.entity.dto.JoinDTO;
import com.kill.gaebokchi.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinDTO joinDTO){
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = memberRepository.existsByUsername(username);

        if(isExist) return;
        log.info(username);
        log.info(password);
        Member entity = new Member();
        entity.setUsername(username);
        entity.setPassword(bCryptPasswordEncoder.encode(password));
        entity.setRole("ROLE_ADMIN");
        memberRepository.save(entity);

    }
}
