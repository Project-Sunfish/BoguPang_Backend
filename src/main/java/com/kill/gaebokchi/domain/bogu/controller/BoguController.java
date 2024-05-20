package com.kill.gaebokchi.domain.bogu.controller;

import com.kill.gaebokchi.domain.bogu.entity.dto.*;
import com.kill.gaebokchi.domain.bogu.service.BoguService;
import com.kill.gaebokchi.domain.bogu.service.DefaultBoguService;
import com.kill.gaebokchi.domain.bogu.service.EvolvedBoguService;
import com.kill.gaebokchi.domain.user.entity.Member;
import com.kill.gaebokchi.domain.user.jwt.JWTUtil;
import com.kill.gaebokchi.domain.user.repository.MemberRepository;
import com.kill.gaebokchi.domain.user.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/bogu")
@RequiredArgsConstructor
public class BoguController {
    private final DefaultBoguService defaultBoguService;
    private final EvolvedBoguService evolvedBoguService;
    private final BoguService boguService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    @PostMapping
    public ResponseEntity<?> createDefaultBogu(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        Long id = defaultBoguService.saveDefaultBogu(member);

        Map<String, Long> res = new HashMap<>();
        res.put("defaultBoguId", id);
        return ResponseEntity.ok(res);

    }
    @PostMapping("/evolution")
    public ResponseEntity<?> createEvolvedBogu(@RequestBody EvolutionRequestDTO evolutionRequestDTO){
        Long id = evolvedBoguService.saveEvolvedBogu(evolutionRequestDTO);
        Map<String, Long> res = new HashMap<>();
        res.put("evolvedBoguid", id);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/{evolvedBoguId}")
    public ResponseEntity<?> getEvolvedBoguById(@PathVariable("evolvedBoguId") Long id){

        EvolvedBoguResponseDTO res = evolvedBoguService.findEvolvedBoguByID(id);
        return ResponseEntity.ok(res);

    }
    @GetMapping
    public ResponseEntity<?> getBoguList(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);

        BoguResponseDTO res = boguService.findAllBogus(member);
        return ResponseEntity.ok(res);

    }

    @PostMapping("/pop")
    public ResponseEntity<?> popBogu(@RequestBody IdRequestDTO req){

        PopBoguResponseDTO res = evolvedBoguService.popEvolvedBogu(req.getId());
        return ResponseEntity.ok(res);

    }

    @PostMapping("/liberation")
    public ResponseEntity<?> liberateBogu(@RequestBody IdRequestDTO req){

        evolvedBoguService.LiberateEvolvedBogu(req.getId());
        return new ResponseEntity<>("해당 복어가 정상적으로 해방되었습니다.", HttpStatus.CREATED);

    }

}
