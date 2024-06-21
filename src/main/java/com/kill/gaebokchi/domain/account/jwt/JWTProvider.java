package com.kill.gaebokchi.domain.account.jwt;

import com.kill.gaebokchi.domain.account.dto.response.TokenResponseDTO;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.service.MemberService;
import com.kill.gaebokchi.global.error.BadRequestException;
//import com.kill.gaebokchi.global.redis.RedisService;
import com.kill.gaebokchi.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.kill.gaebokchi.global.error.ExceptionCode.IS_NOT_REFRESHTOKEN;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTProvider {
    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final RedisService redisService;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    public TokenResponseDTO createJWT(Member member){
        long now = (new Date()).getTime();

        String subject = member.getEmail();
        String accessToken = jwtUtil.createJwt("access", member.getRole().toString(), subject, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtil.createJwt("refresh", member.getRole().toString(), subject, REFRESH_TOKEN_EXPIRE_TIME);
        redisService.setValues(subject, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return TokenResponseDTO.of(accessToken, refreshToken, member.getTutorial(), member.getRole().toString());
    }
    public TokenResponseDTO reissue(String refreshToken){
        String category = jwtUtil.getCategory(refreshToken);
        if(!category.equals("refresh")){
            throw new BadRequestException(IS_NOT_REFRESHTOKEN);
        }
        //redis에 저장되어 있는 refresh token이 프론트측에서 받은 refresh token과 같은지 확인
        String subject = jwtUtil.getSubject(refreshToken);
        String valueToken = redisService.getValues(subject);
        if(valueToken == null || !valueToken.equals(refreshToken) || !jwtUtil.getCategory(valueToken).equals("refresh")){
            throw new BadRequestException(IS_NOT_REFRESHTOKEN);
        }
        Member member = memberService.findMemberByEmail(subject);
        return createJWT(member);
    }
    public TokenResponseDTO signup(Member member){

        String refreshToken = redisService.getValues(member.getEmail());

        String subject = member.getEmail();
        String role = member.getRole().toString();
        String newAT=jwtUtil.createJwt("access", role, subject, ACCESS_TOKEN_EXPIRE_TIME);
        String newRT=jwtUtil.createJwt("refresh", role, subject, REFRESH_TOKEN_EXPIRE_TIME);
        log.info("original refresh token : "+redisService.getValues(subject));
        redisService.setValues(subject, newRT, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        log.info("new refresh token : "+redisService.getValues(subject));
        return new TokenResponseDTO(newAT, newRT, member.getTutorial(), role);
    }

    public void deleteToken(String subject){
        redisService.deleteValues(subject);
    }
}
