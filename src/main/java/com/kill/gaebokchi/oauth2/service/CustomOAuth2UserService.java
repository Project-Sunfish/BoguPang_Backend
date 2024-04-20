package com.kill.gaebokchi.oauth2.service;

import com.kill.gaebokchi.entity.Member;
import com.kill.gaebokchi.entity.SocialType;
import com.kill.gaebokchi.oauth2.CustomOAuth2User;
import com.kill.gaebokchi.oauth2.OAuthAttributes;
import com.kill.gaebokchi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    //check if member exists
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser()실행");
        //take information of OAuth2
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); //get information of OAuth2

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //kakao, google, naver
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractedAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Member createMember = getMember(extractedAttributes, socialType);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createMember.getRole().getKey())),
                attributes,
                extractedAttributes.getNameAttributeKey(),
                createMember.getEmail(),
                createMember.getRole()
        );
    }
    private SocialType getSocialType(String registrationId){
        if("naver".equals(registrationId)){
            log.info("social Type is naver");
            return SocialType.NAVER;
        }else if("kakao".equals(registrationId)){
            log.info("social Type is kakao");
            return SocialType.KAKAO;
        }
        log.info("social Type is google");
        return SocialType.GOOGLE;
    }
    private Member getMember(OAuthAttributes attributes, SocialType socialType){
        Member findMember = memberRepository.findBySocialTypeAndSocialId(socialType, attributes.getOAuth2UserInfo().getRegistrationId()).orElse(null);
        if(findMember==null){
            return saveMember(attributes, socialType);
        }
        return findMember;
    }
    private Member saveMember(OAuthAttributes attributes, SocialType socialType){
        Member createMember = attributes.toEntity(socialType, attributes.getOAuth2UserInfo());
        return memberRepository.save(createMember);
    }



}

