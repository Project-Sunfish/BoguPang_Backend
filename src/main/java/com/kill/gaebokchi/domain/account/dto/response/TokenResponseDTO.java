package com.kill.gaebokchi.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TokenResponseDTO {
    String accessToken;
    String refreshToken;

    public static TokenResponseDTO of(String aT, String rT){
        return TokenResponseDTO.builder()
                .accessToken(aT)
                .refreshToken(rT)
                .build();
    }
}
