package com.kill.gaebokchi.domain.account.infra;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    KAKAO("https://kapi.kakao.com/v2/user/me", HttpMethod.GET),
    NAVER("https://openapi.naver.com/v1/nid/me", HttpMethod.GET),
    GOOGLE("https://www.googleapis.com/oauth2/v3/userinfo", HttpMethod.GET),
    APPLE("https://appleid.apple.com/auth/keys",HttpMethod.GET);

    private final String url;
    private final HttpMethod httpMethod;
}
