package com.kill.gaebokchi.domain.account.controller;

import com.kill.gaebokchi.domain.account.dto.response.MemberResponseDTO;
import com.kill.gaebokchi.domain.account.dto.request.SignUpRequestDTO;
import com.kill.gaebokchi.domain.account.dto.response.TutorialResponseDTO;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.security.CustomUserDetails;
import com.kill.gaebokchi.domain.account.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> findUser(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);
        MemberResponseDTO response = MemberResponseDTO.from(member);
        return ResponseEntity.ok(response);
    }
    @PutMapping
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody SignUpRequestDTO signUpRequestDTO){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);
        try{
            memberService.updateMember(member, signUpRequestDTO);
            return ResponseEntity.ok("해당 user의 정보를 정상적으로 수정했습니다.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);
        try{
            memberService.deleteMember(member);
            return ResponseEntity.ok("해당 user를 정상적으로 삭제했습니다.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    @GetMapping("/tutorial")
    public ResponseEntity<?> getTutorialFlag(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);
        TutorialResponseDTO response = memberService.getTutorialFlag(member);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/tutorial")
    public ResponseEntity<?> makeTrueTutorialFlag(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String email = customUserDetails.getUsername();
        Member member = memberService.findMemberByEmail(email);
        try{
            memberService.toggleTutorialFlag(member);
            return ResponseEntity.ok("해당 user의 tutorial flag를 true로 바꿨습니다.");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
