package com.kill.gaebokchi.domain.account.dto.response;

import com.kill.gaebokchi.domain.account.infra.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuthResponse {
    String email;
    SocialType socialType;
    String refreshToken;
}
