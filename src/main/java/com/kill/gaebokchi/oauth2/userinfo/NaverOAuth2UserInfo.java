package com.kill.gaebokchi.oauth2.userinfo;

import java.util.Date;
import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo{
    public NaverOAuth2UserInfo(Map<String, Object> attributes){
        super(attributes);
    }

    @Override
    public String getRegistrationId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if(response==null) return null;
        return (String)response.get("id");
    }

}
