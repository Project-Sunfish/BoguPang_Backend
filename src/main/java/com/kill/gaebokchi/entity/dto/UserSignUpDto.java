package com.kill.gaebokchi.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
public class UserSignUpDto {
    private String nickname;
    private String gender;
    private Date birth;
}
