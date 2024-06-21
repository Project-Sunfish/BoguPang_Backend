package com.kill.gaebokchi.domain.account.controller;

import com.kill.gaebokchi.domain.account.dto.request.LoginRequestDTO;
import com.kill.gaebokchi.domain.account.dto.request.ReissueRequestDTO;
import com.kill.gaebokchi.domain.account.dto.request.SignUpRequestDTO;
import com.kill.gaebokchi.domain.account.dto.response.TokenResponseDTO;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.security.CustomUserDetails;
import com.kill.gaebokchi.domain.account.service.AuthService;
import com.kill.gaebokchi.domain.account.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){
        TokenResponseDTO response = authService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody SignUpRequestDTO signUpRequestDTO){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);
        TokenResponseDTO response = authService.signUp(member, signUpRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody ReissueRequestDTO reissueRequestDTO){
        TokenResponseDTO response = authService.reissue(reissueRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        try{
            authService.logout(email);
            return ResponseEntity.ok("해당 유저가 성공적으로 로그아웃되었습니다.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
