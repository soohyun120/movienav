package com.movienav.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_CREDENTIAL(HttpStatus.BAD_REQUEST, "Invalid credentials supplied");

    private final HttpStatus httpStatus;
    private final String message;
}
