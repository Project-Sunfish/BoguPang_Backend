package com.kill.gaebokchi.controller;

import com.kill.gaebokchi.entity.Member;
import com.kill.gaebokchi.entity.dto.UserSignUpDto;
import com.kill.gaebokchi.jwt.JwtUtils;
import com.kill.gaebokchi.oauth2.service.MemberService;
import com.kill.gaebokchi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final JwtUtils jwtUtils;
    private final MemberService memberService;

    @GetMapping("/jwt-test")
    public String jwtTest(){
        return "Success : jwtTest request";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpDto userSignUpDto, @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        String accessToken = authorizationHeader.substring("Bearer ".length());
        log.info(accessToken);
        Optional<String> email = jwtUtils.extractEmail(accessToken);

        try {
            memberService.signUp(email.get(), userSignUpDto);
            return new ResponseEntity<>("회원가입이 정상적으로 완료되었습니다", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
