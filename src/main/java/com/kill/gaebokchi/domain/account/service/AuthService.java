package com.kill.gaebokchi.domain.account.service;

import com.kill.gaebokchi.domain.account.dto.LoginResponseDTO;
import com.kill.gaebokchi.domain.account.dto.OAuthResponse;
import com.kill.gaebokchi.domain.account.dto.SignUpRequestDTO;
import com.kill.gaebokchi.domain.account.infra.google.GoogleApiClient;
import com.kill.gaebokchi.domain.account.infra.kakao.KakaoApiClient;
import com.kill.gaebokchi.domain.account.infra.naver.NaverApiClient;
import com.kill.gaebokchi.domain.account.jwt.JWTProvider;
import com.kill.gaebokchi.domain.account.dto.LoginRequestDTO;
import com.kill.gaebokchi.domain.account.entity.Member;
import com.kill.gaebokchi.domain.account.entity.Role;
import com.kill.gaebokchi.domain.account.entity.TypeFlag;
import com.kill.gaebokchi.domain.account.repository.MemberRepository;
import com.kill.gaebokchi.global.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kill.gaebokchi.global.error.ExceptionCode.INVALID_SOCIAL_TYPE;
import static com.kill.gaebokchi.global.error.ExceptionCode.NOT_FOUND_MEMBER_EMAIL;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class AuthService {
    private final TypeFlagService typeFlagService;
    private final MemberRepository memberRepository;
    private final JWTProvider jwtProvider;
    private final KakaoApiClient kakaoApiClient;
    private final NaverApiClient naverApiClient;
    private final GoogleApiClient googleApiClient;
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request){
        String socialType = request.getSocialType();
        OAuthResponse response;
        if(socialType.equalsIgnoreCase("Kakao")){
            response = kakaoApiClient.requestOAuthInfo(request.getAccessToken());
        }else if(socialType.equalsIgnoreCase("Naver")){
            response = naverApiClient.requestOAuthInfo(request.getAccessToken());
        }else if(socialType.equalsIgnoreCase("Google")){
            response = googleApiClient.requestOAuthInfo(request.getAccessToken());
        }else{
            throw new BadRequestException(INVALID_SOCIAL_TYPE);
        }
        Member findOne = findOrCreateMember(response);
        return jwtProvider.createJWT(findOne);
    }
    private Member findOrCreateMember(OAuthResponse response){
        return memberRepository.findByEmail(response.getEmail()).orElseGet(()->createMember(response));
    }
    private Member createMember(OAuthResponse response){
        Member member = Member.builder()
                .email(response.getEmail())
                .role(Role.ROLE_GUEST)
                .socialType(response.getSocialType())
                .build();
        TypeFlag typeFlag = typeFlagService.createFlag();
        member.setFlag(typeFlag);
        memberRepository.save(member);
        return member;
    }
    @Transactional
    public LoginResponseDTO signUp(Member member, SignUpRequestDTO request){
        member.setBirth(request.getBirth());
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setRole(Role.ROLE_USER);
        return jwtProvider.signup(member);
    }

}
