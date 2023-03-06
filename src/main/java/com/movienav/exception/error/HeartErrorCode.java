package com.movienav.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HeartErrorCode implements ErrorCode {

    NO_HEART(HttpStatus.NOT_FOUND, "not found heart");

    private final HttpStatus httpStatus;
    private final String message;
}
