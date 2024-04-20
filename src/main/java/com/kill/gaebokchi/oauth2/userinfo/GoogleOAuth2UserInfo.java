package com.kill.gaebokchi.oauth2.userinfo;

import java.util.Date;
import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo{
    public GoogleOAuth2UserInfo(Map<String, Object> attributes){
        super(attributes);
    }

    @Override
    public String getRegistrationId() {
        return (String) attributes.get("sub");
    }

}
