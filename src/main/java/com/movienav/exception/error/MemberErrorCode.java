package com.movienav.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    ALREADY_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NO_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    NO_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    NO_ADMIN(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
