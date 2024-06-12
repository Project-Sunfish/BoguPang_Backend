package com.kill.gaebokchi.domain.account.infra.google;

import com.kill.gaebokchi.domain.account.dto.OAuthResponse;
import com.kill.gaebokchi.domain.account.infra.OAuthApiClient;
import com.kill.gaebokchi.domain.account.infra.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleApiClient implements OAuthApiClient {
    private final RestTemplate restTemplate;
    @Override
    public OAuthResponse requestOAuthInfo(String accessToken) {
        String url = SocialType.GOOGLE.getUrl();
        HttpMethod httpMethod = SocialType.GOOGLE.getHttpMethod();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer "+accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpHeaders);
        ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {};
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, httpMethod, request, RESPONSE_TYPE);

        return OAuthResponse.builder()
                .socialType(SocialType.GOOGLE)
                .email(response.getBody().get("id") + "@GOOGLE")
                .build();
    }
}
