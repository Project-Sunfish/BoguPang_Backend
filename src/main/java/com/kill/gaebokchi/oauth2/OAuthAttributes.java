package com.kill.gaebokchi.oauth2;

import com.kill.gaebokchi.entity.Member;
import com.kill.gaebokchi.entity.Role;
import com.kill.gaebokchi.entity.SocialType;
import com.kill.gaebokchi.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.kill.gaebokchi.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.kill.gaebokchi.oauth2.userinfo.NaverOAuth2UserInfo;
import com.kill.gaebokchi.oauth2.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {
    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo){
        this.nameAttributeKey=nameAttributeKey;
        this.oAuth2UserInfo=oAuth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes){
        if(socialType==SocialType.KAKAO){
            return ofKakao(userNameAttributeName, attributes);
        }else if(socialType==SocialType.NAVER){
            return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }
    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }
    public Member toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return Member.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getRegistrationId())
                .email(UUID.randomUUID()+"@socialUser.com")
                .role(Role.GUEST)
                .build();
    }
}
