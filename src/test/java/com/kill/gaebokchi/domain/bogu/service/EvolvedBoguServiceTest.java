package com.kill.gaebokchi.domain.bogu.service;

import com.kill.gaebokchi.domain.bogu.entity.DefaultBogu;
import com.kill.gaebokchi.domain.bogu.entity.EvolvedBogu;
import com.kill.gaebokchi.domain.bogu.entity.dto.EvolutionRequestDTO;
import com.kill.gaebokchi.domain.bogu.repository.DefaultBoguRepository;
import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.domain.user.entity.dto.JoinDTO;
import com.kill.gaebokchi.domain.user.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EvolvedBoguServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired DefaultBoguService defaultBoguService;
    @Autowired EvolvedBoguService evolvedBoguService;
    @Autowired
    DefaultBoguRepository defaultBoguRepository;
    @Test
    public void 기본복어생성() throws Exception{
        //given
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setUsername("username");
        joinDTO.setPassword("password");
        memberService.join(joinDTO);

        //when
        Long defaultBoguId = defaultBoguService.saveDefaultBogu(memberService.findMemberByUsername("username"));
        Long defaultBoguId2 = defaultBoguService.saveDefaultBogu(memberService.findMemberByUsername("username"));

        //then
        //default bogu의 host에 회원 알맞게 설정되었는지 확인
        assertEquals(memberService.findMemberByUsername("username"), defaultBoguRepository.findById(defaultBoguId).orElse(null).getHost());
        assertEquals(memberService.findMemberByUsername("username"), defaultBoguRepository.findById(defaultBoguId2).orElse(null).getHost());
        //회원에 default bogu 2마리 추가되었는지확인
        assertEquals(2, memberService.findMemberByUsername("username").getBogus().size());

    }
    @Test
    public void 진화복어생성_진화횟수10번이하() throws Exception{
        //given
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setUsername("username");
        joinDTO.setPassword("password");
        memberService.join(joinDTO);

        //when
        Long defaultBoguId = defaultBoguService.saveDefaultBogu(memberService.findMemberByUsername("username"));
        EvolutionRequestDTO evolutionRequestDTO = new EvolutionRequestDTO();
        evolutionRequestDTO.setCategories(new ArrayList<>(Arrays.asList("기타")));
        evolutionRequestDTO.setProblem("친구가없어요ㅜ");
        evolutionRequestDTO.setDefaultBoguId(defaultBoguId);
        Long evolvedBoguId = evolvedBoguService.saveEvolvedBogu(evolutionRequestDTO);

        Long defaultBoguId2 = defaultBoguService.saveDefaultBogu(memberService.findMemberByUsername("username"));
        EvolutionRequestDTO evolutionRequestDTO2 = new EvolutionRequestDTO();
        evolutionRequestDTO2.setCategories(new ArrayList<>(Arrays.asList("기타")));
        evolutionRequestDTO2.setProblem("친구가없어요ㅜ");
        evolutionRequestDTO2.setDefaultBoguId(defaultBoguId2);
        Long evolvedBoguId2 = evolvedBoguService.saveEvolvedBogu(evolutionRequestDTO2);

        Long defaultBoguId3 = defaultBoguService.saveDefaultBogu(memberService.findMemberByUsername("username"));
        EvolutionRequestDTO evolutionRequestDTO3 = new EvolutionRequestDTO();
        evolutionRequestDTO3.setCategories(new ArrayList<>(Arrays.asList("기타")));
        evolutionRequestDTO3.setProblem("친구가없어요ㅜ");
        evolutionRequestDTO3.setDefaultBoguId(defaultBoguId2);
        Long evolvedBoguId3 = evolvedBoguService.saveEvolvedBogu(evolutionRequestDTO2);

        //then
        EvolvedBogu findOne = evolvedBoguService.findEvolvedBoguByID(evolvedBoguId);
        EvolvedBogu findOne2 = evolvedBoguService.findEvolvedBoguByID(evolvedBoguId2);
        EvolvedBogu findOne3 = evolvedBoguService.findEvolvedBoguByID(evolvedBoguId3);

        assertEquals(findOne.getDefaultForm().getHost(), findOne2.getDefaultForm().getHost());
        assertEquals(3, memberService.findMemberByUsername("username").getBogus().size());
    }
    @Test
    public void 진화복어생성_진화횟수10번초과() throws Exception{
        //given
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setUsername("username");
        joinDTO.setPassword("password");
        memberService.join(joinDTO);

        //when
        for(int i=0;i<10; i++){
            Long defaultBoguId = defaultBoguService.saveDefaultBogu(memberService.findMemberByUsername("username"));
            EvolutionRequestDTO evolutionRequestDTO = new EvolutionRequestDTO();
            evolutionRequestDTO.setCategories(new ArrayList<>(Arrays.asList("가족")));
            evolutionRequestDTO.setProblem("친구가없어요ㅜ");
            evolutionRequestDTO.setDefaultBoguId(defaultBoguId);
            Long evolvedBoguId = evolvedBoguService.saveEvolvedBogu(evolutionRequestDTO);
        }
        for(int i=0; i<5;i++){
            Long defaultBoguId = defaultBoguService.saveDefaultBogu(memberService.findMemberByUsername("username"));
            EvolutionRequestDTO evolutionRequestDTO = new EvolutionRequestDTO();
            evolutionRequestDTO.setCategories(new ArrayList<>(Arrays.asList("연인")));
            evolutionRequestDTO.setProblem("친구가없어요ㅜ");
            evolutionRequestDTO.setDefaultBoguId(defaultBoguId);
            Long evolvedBoguId = evolvedBoguService.saveEvolvedBogu(evolutionRequestDTO);
        }
        assertEquals(15, memberService.findMemberByUsername("username").getBogus().size());

    }

}