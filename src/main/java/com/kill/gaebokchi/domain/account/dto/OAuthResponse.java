package com.kill.gaebokchi.domain.account.dto;

import com.kill.gaebokchi.domain.account.infra.SocialType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class OAuthResponse {
    String email;
    SocialType socialType;
}
