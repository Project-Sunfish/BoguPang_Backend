package com.kill.gaebokchi.domain.account.dto;

import com.kill.gaebokchi.domain.account.entity.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponseDTO {
    String accessToken;
    String refreshToken;
    String role;

    public static LoginResponseDTO of(String aT, String rT, String role){
        return LoginResponseDTO.builder()
                .accessToken(aT)
                .refreshToken(rT)
                .role(role)
                .build();
    }
}
