package com.zero.stock.exception.impl;

import com.zero.stock.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends AbstractException {
    @Override
    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getError() {
        return "존재하지 않는 회원입니다.";
    }
}
