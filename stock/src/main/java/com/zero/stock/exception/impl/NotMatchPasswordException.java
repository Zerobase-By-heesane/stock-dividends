package com.zero.stock.exception.impl;

import com.zero.stock.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotMatchPasswordException extends AbstractException {
    @Override
    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getError() {
        return "비밀번호가 일치하지 않습니다.";
    }
}
