package com.kill.gaebokchi.domain.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    String socialType;
    String accessToken;
}
