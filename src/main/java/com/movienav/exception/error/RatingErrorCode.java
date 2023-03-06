package com.movienav.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RatingErrorCode implements ErrorCode {

    NO_RATING(HttpStatus.NOT_FOUND, "not found rating");

    private final HttpStatus httpStatus;
    private final String message;
}
