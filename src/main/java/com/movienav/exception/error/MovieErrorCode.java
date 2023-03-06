package com.movienav.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MovieErrorCode implements ErrorCode {

    NO_MOVIE(HttpStatus.NOT_FOUND, "존재하지 않는 영화입니다."),
    ZERO_GOOD_COUNT(HttpStatus.BAD_REQUEST, "zero good count");

    private final HttpStatus httpStatus;
    private final String message;
}
