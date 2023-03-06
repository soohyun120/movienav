package com.movienav.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    NO_REVIEW(HttpStatus.NOT_FOUND, "not found review"),
    WRONG_USERNAME(HttpStatus.BAD_REQUEST, "아이디가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
