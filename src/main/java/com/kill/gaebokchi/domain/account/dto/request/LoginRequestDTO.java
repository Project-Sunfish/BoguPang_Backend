package com.kill.gaebokchi.domain.account.dto.request;

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

        return Objects.isNull(socialType) || StringUtils.isEmpty(socialType)|| Objects.isNull(accessToken) || StringUtils.isEmpty(accessToken);
    }
}
