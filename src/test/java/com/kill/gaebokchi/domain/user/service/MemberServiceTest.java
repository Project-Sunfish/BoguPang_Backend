package com.kill.gaebokchi.domain.user.service;

import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.repository.MemberRepository;
import com.kill.gaebokchi.domain.account.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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