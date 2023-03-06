package com.movienav.exception.controller;

import com.movienav.exception.CustomException;
import com.movienav.exception.error.MemberErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/entryPoint")
    public void entryPointException() {
        throw new CustomException(MemberErrorCode.NO_LOGIN);
    }

    @GetMapping("/accessDenied")
    public void accessDeniedException() {
        throw new CustomException(MemberErrorCode.NO_ADMIN);
    }
}
