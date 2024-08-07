package com.kill.gaebokchi.domain.account.service;

import com.kill.gaebokchi.domain.account.dto.request.LoginRequestDTO;
import com.kill.gaebokchi.domain.account.dto.request.ReissueRequestDTO;
import com.kill.gaebokchi.domain.account.dto.request.SignUpRequestDTO;
import com.kill.gaebokchi.domain.account.dto.response.TokenResponseDTO;
import com.kill.gaebokchi.domain.account.dto.response.OAuthResponse;
import com.kill.gaebokchi.domain.account.infra.SocialType;
import com.kill.gaebokchi.domain.account.infra.apple.AppleApiClient;
import com.kill.gaebokchi.domain.account.infra.google.GoogleApiClient;
import com.kill.gaebokchi.domain.account.infra.kakao.KakaoApiClient;
import com.kill.gaebokchi.domain.account.infra.naver.NaverApiClient;
import com.kill.gaebokchi.domain.account.jwt.JWTProvider;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.entity.Role;
import com.kill.gaebokchi.domain.account.entity.TypeFlag;
import com.kill.gaebokchi.domain.account.repository.MemberRepository;
import com.kill.gaebokchi.domain.archive.LoginTime;
import com.kill.gaebokchi.domain.archive.LoginTimeRepository;
import com.kill.gaebokchi.global.error.AuthException;
import com.kill.gaebokchi.global.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.kill.gaebokchi.global.error.ExceptionCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class AuthService {
    private final TypeFlagService typeFlagService;
    private final MemberRepository memberRepository;
    private final LoginTimeRepository loginTimeRepository;
    private final JWTProvider jwtProvider;
    private final KakaoApiClient kakaoApiClient;
    private final NaverApiClient naverApiClient;
    private final GoogleApiClient googleApiClient;
    private final AppleApiClient appleApiClient;
    @Transactional
    public TokenResponseDTO login(LoginRequestDTO request){
        if(request.hasNullFields()){
            throw new BadRequestException(INVALID_LOGIN_FORM);
        }
        String socialType = request.getSocialType();
        OAuthResponse response;
        if(socialType.equalsIgnoreCase("Kakao")){
            response = kakaoApiClient.requestOAuthInfo(request.getAccessToken());
        }else if(socialType.equalsIgnoreCase("Naver")){
            response = naverApiClient.requestOAuthInfo(request.getAccessToken());
        }else if(socialType.equalsIgnoreCase("Google")){
            response = googleApiClient.requestOAuthInfo(request.getAccessToken());
        }else if(socialType.equalsIgnoreCase("Apple")){
            response = appleApiClient.requestOAuthInfo(request.getAccessToken());
        }else{
            throw new BadRequestException(INVALID_SOCIAL_TYPE);
        }
        Member findOne = findOrCreateMember(response);
        //archive
        LoginTime loginTime = LoginTime.builder()
                .memberId(findOne.getId())
                .loginAt(LocalDateTime.now())
                .build();
        loginTimeRepository.save(loginTime);
        //archive
        return jwtProvider.createJWT(findOne);
    }
    private Member findOrCreateMember(OAuthResponse response){
        return memberRepository.findByEmail(response.getEmail()).orElseGet(()->createMember(response));
    }
    private Member createMember(OAuthResponse response){
        log.info("social type : {}", response.getSocialType());
        Member member = Member.builder()
                .email(response.getEmail())
                .role(Role.ROLE_GUEST)
                .socialType(response.getSocialType())
                .tutorial(false)
                .build();
        TypeFlag typeFlag = typeFlagService.createFlag();
        member.setFlag(typeFlag);
        if(response.getSocialType()== SocialType.APPLE){//server에서 revoke해야 함, 이때 refresh token 필요
            member.setRefreshToken(response.getRefreshToken());
        }
        memberRepository.save(member);
        return member;
    }
    @Transactional
    public TokenResponseDTO signUp(Member member, SignUpRequestDTO request){
        if(request.hasNullFields()){
            throw new BadRequestException(INVALID_SIGNUP_FORM);
        }
        if(member.getRole()==Role.ROLE_USER){
            throw new AuthException(ALREADY_SIGNUP_MEMBER);
        }
        member.setBirthType(request.getBirthType());
        member.setBirth(request.getBirth());
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setRole(Role.ROLE_USER);
        return jwtProvider.signup(member);
    }
    @Transactional
    public TokenResponseDTO reissue(ReissueRequestDTO request){
        return jwtProvider.reissue(request.getRefreshToken());
    }

    @Transactional
    public void logout(String email){
        jwtProvider.deleteToken(email);
    }

}
