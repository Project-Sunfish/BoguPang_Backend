package com.kill.gaebokchi.domain.account.infra;

import com.kill.gaebokchi.domain.account.dto.response.OAuthResponse;

public interface OAuthApiClient {
    OAuthResponse requestOAuthInfo(String accessToken);
}
