package com.kill.gaebokchi.domain.bogu.controller;

import com.kill.gaebokchi.domain.account.security.CustomUserDetails;
import com.kill.gaebokchi.domain.bogu.dto.request.EvolutionRequestDTO;
import com.kill.gaebokchi.domain.bogu.dto.request.IdRequestDTO;
import com.kill.gaebokchi.domain.bogu.dto.response.*;
import com.kill.gaebokchi.domain.bogu.dto.response.dogam.DetailDogamBoguResponseDTO;
import com.kill.gaebokchi.domain.bogu.dto.response.dogam.DogamBoguResponseDTO;
import com.kill.gaebokchi.domain.bogu.service.BoguService;
import com.kill.gaebokchi.domain.bogu.service.DefaultBoguService;
import com.kill.gaebokchi.domain.bogu.service.EvolvedBoguService;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.jwt.JWTUtil;
import com.kill.gaebokchi.domain.account.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoguController {
    private final DefaultBoguService defaultBoguService;
    private final EvolvedBoguService evolvedBoguService;
    private final BoguService boguService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    @PostMapping("/bogu/creation")
    public ResponseEntity<?> createDefaultBogu(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        DefaultBoguResponseDTO res = defaultBoguService.saveDefaultBogu(member);

        return ResponseEntity.ok(res);

    }
    @PostMapping("/bogu/evolution")
    public ResponseEntity<?> createEvolvedBogu(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody EvolutionRequestDTO evolutionRequestDTO){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        EvolvedBoguResponseDTO res = evolvedBoguService.saveEvolvedBogu(member, evolutionRequestDTO);
        return ResponseEntity.ok(res);
    }
    @PostMapping("/bogu/id")
    public ResponseEntity<?> getEvolvedBoguById(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody IdRequestDTO idRequestDTO){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        EvolvedBoguResponseDTO res = evolvedBoguService.findEvolvedBoguByID(member, idRequestDTO.getId());
        return ResponseEntity.ok(res);

    }
    @GetMapping("/bogu")
    public ResponseEntity<?> getBoguList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        BoguResponseDTO res = boguService.findAllBogus(member);
        return ResponseEntity.ok(res);
    }
    @PostMapping("/bogu")
    public ResponseEntity<?> updateBoguList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        evolvedBoguService.updateEvolvedBogu(member);
        return ResponseEntity.ok("진화 복어의 업데이트가 완료되었습니다.");
    }


    @PostMapping("/bogu/pop")
    public ResponseEntity<?> popBogu(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody IdRequestDTO req){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        PopBoguResponseDTO res = evolvedBoguService.popEvolvedBogu(member, req.getId());
        return ResponseEntity.ok(res);

    }

    @PostMapping("/bogu/liberation")
    public ResponseEntity<?> liberateBogu(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody IdRequestDTO req){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        evolvedBoguService.LiberateEvolvedBogu(member, req.getId());
        return new ResponseEntity<>("해당 복어가 정상적으로 해방되었습니다.", HttpStatus.CREATED);

    }

    @PostMapping("/collection")
    public ResponseEntity<?> getDogamBoguList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        DogamBoguResponseDTO res = evolvedBoguService.getDogamBogus(member);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/collection/id")
    public ResponseEntity<?> getDogamBoguById(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody IdRequestDTO idRequestDTO){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        List<DetailDogamBoguResponseDTO> res = evolvedBoguService.findDogamBoguById(member,idRequestDTO.getId().intValue());
        return ResponseEntity.ok(res);

    }

    @PostMapping("/deleteNew")
    public ResponseEntity<?> deleteCreatedFlag(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody IdRequestDTO req){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);

        evolvedBoguService.deleteNewFlag(member, (int)req.getId().longValue());
        return ResponseEntity.ok("해당 타입의 new가 정상적으로 삭제되었습니다.");
    }



}
