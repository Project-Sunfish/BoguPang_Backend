package com.kill.gaebokchi.domain.user.service;

import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.domain.user.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void 유저찾기(){
        Member member = new Member();
        member.setUsername("u");
        member.setPassword("p");
        memberRepository.save(member);

        memberService.findMemberByUsername("user");
    }

}