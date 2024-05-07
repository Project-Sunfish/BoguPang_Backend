package com.kill.gaebokchi.domain.user.controller;

import com.kill.gaebokchi.domain.user.entity.dto.JoinDTO;
import com.kill.gaebokchi.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ResponseBody
public class MemberController {
    private final MemberService memberService;
    private final UserDetailsService userDetailsService;

    @PostMapping("/join")
    public String join(@RequestBody JoinDTO joinDTO){
        memberService.join(joinDTO);
        return "ok";
    }

    @PostMapping("/test")
    public String test(){
        return "success test";
    }
}
