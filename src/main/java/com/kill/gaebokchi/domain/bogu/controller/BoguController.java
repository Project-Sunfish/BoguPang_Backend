package com.kill.gaebokchi.domain.bogu.controller;

import com.kill.gaebokchi.domain.bogu.entity.dto.BoguResponseDTO;
import com.kill.gaebokchi.domain.bogu.entity.dto.EvolutionRequestDTO;
import com.kill.gaebokchi.domain.bogu.entity.dto.PopBoguResponseDTO;
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

import java.util.List;


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
        try{
            Long id = defaultBoguService.saveDefaultBogu(member);
            return ResponseEntity.ok(id);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/evolution")
    public ResponseEntity<?> createEvolvedBogu(@RequestBody EvolutionRequestDTO evolutionRequestDTO){
        if(evolutionRequestDTO.hasNullFields()){
            return new ResponseEntity<>("올바른 입력을 해주세요", HttpStatus.BAD_REQUEST);
        }
        try{
            Long id = evolvedBoguService.saveEvolvedBogu(evolutionRequestDTO);
            return ResponseEntity.ok(id);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> getBoguList(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(accessToken);
        Member member = memberService.findMemberByUsername(username);
        try{
            BoguResponseDTO res = boguService.findAllBogus(member);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/pop")
    public ResponseEntity<?> popBogu(@RequestBody Long id){
        try{
            PopBoguResponseDTO res = evolvedBoguService.popEvolvedBogu(id);
            return ResponseEntity.ok(res);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/liberation")
    public ResponseEntity<?> liberateBogu(@RequestBody Long id){
        try{
            evolvedBoguService.LiberateEvolvedBogu(id);
            return new ResponseEntity<>("해당 복어가 정상적으로 해방되었습니다.", HttpStatus.CREATED);

        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
