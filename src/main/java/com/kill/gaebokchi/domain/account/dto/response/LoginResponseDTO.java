package com.kill.gaebokchi.domain.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponseDTO {
    String accessToken;
    String refreshToken;
    Boolean tutorialFlag;
    String role;

    public static LoginResponseDTO of(String aT, String rT, Boolean tutorial, String role){
        return LoginResponseDTO.builder()
                .accessToken(aT)
                .refreshToken(rT)
                .tutorialFlag(tutorial)
                .role(role)
                .build();
    }
}