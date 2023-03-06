package com.movienav.exception.handler;

import com.movienav.exception.CustomException;
import com.movienav.exception.error.CommonErrorCode;
import com.movienav.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

/**
 * controller 에서 발생한 예외 처리
 */
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<Object> handleCustomException(CustomException e) {
//        log.warn("handleCustomException", e);
//
//        ErrorCode errorCode = e.getErrorCode();
//        return handleExceptionInternal(errorCode);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
//        log.warn("handleIllegalArgument", e);
//
//        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
//        return handleExceptionInternal(errorCode, e.getMessage());
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
//                                                                  HttpHeaders headers, HttpStatusCode status,
//                                                                  WebRequest request) {
//        log.warn("handleMethodArgumentNotValid", e);
//
//        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
//        return handleExceptionInternal(e, errorCode);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAllException(Exception e) {
//        log.warn("handleAllException", e);
//
//        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
//        return handleExceptionInternal(errorCode);
//    }
//
//    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
//        return ResponseEntity.status(errorCode.getHttpStatus())
//                .body(makeErrorResponse(errorCode));
//    }
//
//    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
//        return ErrorResponse.builder()
//                .code(errorCode.name())
//                .message(errorCode.getMessage())
//                .build();
//    }
//
//    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
//        return ResponseEntity.status(errorCode.getHttpStatus())
//                .body(makeErrorResponse(errorCode, message));
//    }
//
//    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
//        return ErrorResponse.builder()
//                .code(errorCode.name())
//                .message(message)
//                .build();
//    }
//
//    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
//        return ResponseEntity.status(errorCode.getHttpStatus())
//                .body(makeErrorResponse(e, errorCode));
//    }
//
//    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
//        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(ErrorResponse.ValidatationError::of)
//                .collect(Collectors.toList());
//
//        return ErrorResponse.builder()
//                .code(errorCode.name())
//                .message(errorCode.getMessage())
//                .errors(validationErrorList)
//                .build();
//    }
//
//}
