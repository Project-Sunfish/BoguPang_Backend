package com.kill.gaebokchi.domain.account.jwt;

import com.kill.gaebokchi.domain.account.dto.LoginResponseDTO;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.global.error.BadRequestException;
import com.kill.gaebokchi.global.error.ExceptionCode;
//import com.kill.gaebokchi.global.redis.RedisService;
import com.kill.gaebokchi.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.kill.gaebokchi.global.error.ExceptionCode.IS_NOT_ACCESSTOKEN;
import static com.kill.gaebokchi.global.error.ExceptionCode.IS_NOT_REFRESHTOKEN;

@Component
@Slf4j
@RequiredArgsConstructor
public class JWTProvider {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    public LoginResponseDTO createJWT(Member member){
        long now = (new Date()).getTime();

        String subject = member.getEmail();
        String accessToken = jwtUtil.createJwt("access", member.getRole().toString(), subject, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = jwtUtil.createJwt("refresh", member.getRole().toString(), subject, REFRESH_TOKEN_EXPIRE_TIME);
        redisService.setValues(subject, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return LoginResponseDTO.of(accessToken, refreshToken, member.getRole().toString());
    }
    public LoginResponseDTO reissue(String refreshToken){
        log.info("refresh Token : "+refreshToken);
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
        String role = jwtUtil.getRole(refreshToken);
        String newAT = jwtUtil.createJwt("access", role, subject, ACCESS_TOKEN_EXPIRE_TIME);
        String newRT = jwtUtil.createJwt("refresh", role, subject, REFRESH_TOKEN_EXPIRE_TIME);

        redisService.setValues(subject, newRT, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return new LoginResponseDTO(newAT, newRT, role);
    }
    public LoginResponseDTO signup(Member member){

        String refreshToken = redisService.getValues(member.getEmail());

        String subject = member.getEmail();
        String role = member.getRole().toString();
        String newAT=jwtUtil.createJwt("access", role, subject, ACCESS_TOKEN_EXPIRE_TIME);
        String newRT=jwtUtil.createJwt("refresh", role, subject, REFRESH_TOKEN_EXPIRE_TIME);
        log.info("original refresh token : "+redisService.getValues(subject));
        redisService.setValues(subject, newRT, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        log.info("new refresh token : "+redisService.getValues(subject));
        return new LoginResponseDTO(newAT, newRT, role);
    }
}
