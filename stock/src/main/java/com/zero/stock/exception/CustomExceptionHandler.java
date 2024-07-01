package com.zero.stock.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
// 컨트롤러 레이어와 가까움
public class CustomExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code(e.getStatus())
                .message(e.getError())
                .build(), HttpStatus.valueOf(e.getStatus()));
    }
}
