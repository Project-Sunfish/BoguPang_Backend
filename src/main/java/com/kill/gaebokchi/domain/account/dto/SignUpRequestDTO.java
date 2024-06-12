package com.kill.gaebokchi.domain.account.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SignUpRequestDTO {
    String name;
    LocalDate birth;
    String gender;
}
