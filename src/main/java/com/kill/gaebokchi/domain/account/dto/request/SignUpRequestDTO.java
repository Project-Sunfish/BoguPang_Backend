package com.kill.gaebokchi.domain.account.dto.request;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Getter
@Setter
public class SignUpRequestDTO {
    String name;
    String birthType;
    LocalDate birth;
    String gender;
    public boolean hasNullFields() {
        // name, birth, gender가 null인지 또는 name이나 gender가 빈 문자열인지 확인
        if (Objects.isNull(name) || StringUtils.isEmpty(name)) {
            return true;
        }

        try {
            LocalDate.parse(birth.toString());
        } catch (DateTimeParseException e) {
            return true;  // birth가 유효한 날짜 형식이 아닐 경우 true 반환
        }
        return false;
    }
}
