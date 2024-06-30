package com.kill.gaebokchi.global.error;

import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisAnnos;

public class AuthException extends RuntimeException {
    private final int code;
    private final String message;

    public AuthException(final ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
