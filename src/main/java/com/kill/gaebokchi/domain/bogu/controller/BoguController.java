package com.kill.gaebokchi.domain.bogu.controller;

import com.kill.gaebokchi.domain.bogu.entity.dto.*;
import com.kill.gaebokchi.domain.bogu.entity.dto.dogamBogu.CollectedBoguResponseDTO;
import com.kill.gaebokchi.domain.bogu.entity.dto.dogamBogu.DogamBoguResponseDTO;
import com.kill.gaebokchi.domain.bogu.service.BoguService;
import com.kill.gaebokchi.domain.bogu.service.DefaultBoguService;
import com.kill.gaebokchi.domain.bogu.service.EvolvedBoguService;
import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.domain.user.jwt.JWTUtil;
import com.kill.gaebokchi.domain.user.repository.MemberRepository;
import com.kill.gaebokchi.domain.user.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @PostMapping("/bogu")
    public ResponseEntity<?> createDefaultBogu(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        DefaultBoguResponseDTO res = defaultBoguService.saveDefaultBogu(member);

        return ResponseEntity.ok(res);

    }
    @PostMapping("/bogu/evolution")
    public ResponseEntity<?> createEvolvedBogu(HttpServletRequest request, @RequestBody EvolutionRequestDTO evolutionRequestDTO){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        EvolvedBoguResponseDTO res = evolvedBoguService.saveEvolvedBogu(member, evolutionRequestDTO);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/bogu/{evolvedBoguId}")
    public ResponseEntity<?> getEvolvedBoguById(HttpServletRequest request, @PathVariable("evolvedBoguId") Long id){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        EvolvedBoguResponseDTO res = evolvedBoguService.findEvolvedBoguByID(member, id);
        return ResponseEntity.ok(res);

    }
    @GetMapping("/bogu")
    public ResponseEntity<?> getBoguList(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        BoguResponseDTO res = boguService.findAllBogus(member);
        return ResponseEntity.ok(res);

    }


    @PostMapping("/bogu/pop")
    public ResponseEntity<?> popBogu(HttpServletRequest request, @RequestBody IdRequestDTO req){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        PopBoguResponseDTO res = evolvedBoguService.popEvolvedBogu(member, req.getId());
        return ResponseEntity.ok(res);

    }

    @PostMapping("/bogu/liberation")
    public ResponseEntity<?> liberateBogu(HttpServletRequest request, @RequestBody IdRequestDTO req){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        evolvedBoguService.LiberateEvolvedBogu(member, req.getId());
        return new ResponseEntity<>("해당 복어가 정상적으로 해방되었습니다.", HttpStatus.CREATED);

    }

    @GetMapping("/collection")
    public ResponseEntity<?> getDogamBoguList(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        DogamBoguResponseDTO res = evolvedBoguService.getDogamBogus(member);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/collection/{dogamBoguId}")
    public ResponseEntity<?> getDogamBoguById(HttpServletRequest request, @PathVariable("dogamBoguId") Integer id){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        CollectedBoguResponseDTO res = evolvedBoguService.findDogamBoguById(member, id);
        return ResponseEntity.ok(res);

    }

}
