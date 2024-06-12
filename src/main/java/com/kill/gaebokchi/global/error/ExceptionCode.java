package com.kill.gaebokchi.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    EXPIRED_TOKEN(0001, "JWT 기한이 만료되었습니다."),
    INVALID_TOKEN_SIGN(0002, "잘못된 JWT 서명입니다."),
    UNSUPPORT_TOKEN(0003, "지원되지 않는 JWT입니다."),
    INVALID_TOKEN_FORM(0004, "잘못된 JWT 형식입니다."),
    INVALID_TOKEN_TYPE(0005, "토큰 형식은 BEARER 입니다."),
    IS_NOT_ACCESSTOKEN(0006, "액세스 토큰이 아닙니다."),
    IS_NOT_REFRESHTOKEN(0007, "리프레시 토큰이 아닙니다."),

    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),

    NOT_FOUND_MEMBER_ID(1001, "요청한 ID에 해당하는 회원이 존재하지 않습니다."),
    NOT_FOUND_MEMBER_EMAIL(1002, "요청한 email에 해당하는 회원이 존재하지 않습니다."),
    INVALID_LOGIN_FORM(1003, "요청한 로그인 형식이 올바르지 않습니다."),
    INVALID_SOCIAL_TYPE(1004, "요청한 소셜 타입명이 잘못되었습니다."),
    INVALID_SIGNUP_FORM(1005, "요청한 회원가입 형식이 올바르지 않습니다."),
    ALREADY_SIGNUP_MEMBER(1006, "이미 ROLE_USER 권한을 가진 회원입니다."),


    NOT_FOUND_DEFAULT_BOGU_ID(2001, "요청한 ID에 해당하는 기본복어가 존재하지 않습니다."),
    EXCEED_DEFAULT_BOGU_CAPACITY(2002, "오늘 생성 가능한 기본복어 수를 초과했습니다."),

    NOT_FOUND_EVOLVED_BOGU_ID(3001, "요청한 ID에 해당하는 진화복어가 존재하지 않습니다."),
    NOT_FOUND_CATEGORY(3002, "요청한 카테고리 명이 잘못되었습니다."),
    EXCEED_CATEGORY_COUNT(3003, "요청한 카테고리 수가 3개를 초과하였습니다."),
    INVALID_EVOLUTION_FORM(3004, "요청한 진화복어 형식이 올바르지 않습니다."),
    INVALID_LEVEL(3005, "요청한 진화복어의 LEVEL값이 올바르지 않습니다."),
    NEGATIVE_EVOLVED_BOGU_COUNT(3006, "해당 진화복어의 수가 음수입니다."),
    DUPLICATE_LIBERATION(3007, "이미 해방된 복어에 대해 해방을 시도했습니다."),
    DUPLICATE_EVOLUTION(3008, "이미 진화된 기본복어에 대해 진화를 시도했습니다."),
    POP_TO_ALREADY_LIBERATION(3009, "이미 해방된 복어에 대해 터트리기를 시도했습니다."),

    NOT_FOUND_DOGAM_BOGU_ID(4001, "요청한 ID에 해당하는 도감복어가 존재하지 않습니다.");

    private final int code;
    private final String message;
}
