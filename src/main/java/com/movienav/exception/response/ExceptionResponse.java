package com.movienav.exception.response;

import com.movienav.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
    private ErrorCode errorCode;
}
