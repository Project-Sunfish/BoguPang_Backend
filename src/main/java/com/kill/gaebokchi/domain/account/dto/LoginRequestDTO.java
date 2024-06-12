package com.kill.gaebokchi.domain.account.dto;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class LoginRequestDTO {
    String socialType;
    String accessToken;
    public boolean hasNullFields() {
        return Objects.isNull(socialType) || Objects.isNull(accessToken) || StringUtils.isEmpty(socialType) || StringUtils.isEmpty(accessToken);
    }
}
