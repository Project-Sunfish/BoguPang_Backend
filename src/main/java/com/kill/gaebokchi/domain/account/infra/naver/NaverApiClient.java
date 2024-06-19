package com.kill.gaebokchi.domain.account.infra.naver;

import com.kill.gaebokchi.domain.account.dto.response.OAuthResponse;
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
public class NaverApiClient implements OAuthApiClient {
    private final RestTemplate restTemplate;
    @Override
    public OAuthResponse requestOAuthInfo(String accessToken) {
        String url = SocialType.NAVER.getUrl();
        HttpMethod httpMethod = SocialType.NAVER.getHttpMethod();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer "+accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(httpHeaders);
        ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {};
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, httpMethod, request, RESPONSE_TYPE);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");

        return OAuthResponse.builder()
                .socialType(SocialType.NAVER)
                .email(responseBody.get("id") + "@NAVER")
                .build();
    }
}
