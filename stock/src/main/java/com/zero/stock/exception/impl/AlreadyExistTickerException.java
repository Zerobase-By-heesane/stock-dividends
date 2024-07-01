package com.zero.stock.exception.impl;

import com.zero.stock.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistTickerException extends AbstractException {

    @Override
    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getError() {
        return "이미 존재하는 종목입니다.";
    }
}
