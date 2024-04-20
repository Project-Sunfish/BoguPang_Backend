package com.kill.gaebokchi.oauth2.userinfo;

import java.util.Date;
import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getRegistrationId() {
        return String.valueOf(attributes.get("id"));
    }

}
