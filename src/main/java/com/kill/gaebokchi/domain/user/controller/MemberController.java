package com.kill.gaebokchi.domain.user.controller;

import com.kill.gaebokchi.domain.user.dto.LogInDto;
import com.kill.gaebokchi.domain.user.dto.MemberDto;
import com.kill.gaebokchi.domain.user.dto.SignUpDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kill.gaebokchi.domain.user.SecurityUtil;
import com.kill.gaebokchi.domain.user.service.MemberService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LogInDto logInDto, HttpServletResponse response){
        String username= logInDto.getUsername();
        String password= logInDto.getPassword();
        String accessToken = memberService.login(username, password);
        response.setHeader("Authorization", "Bearer "+accessToken);
        log.info("Bearer "+accessToken);
        return new ResponseEntity<>("로그인이 정상적으로 작성되었습니다.", HttpStatus.CREATED);
    }
    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto){
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }
    @PostMapping("/test")
    public String test(){
        return SecurityUtil.getCurrentUsername();
    }
}
