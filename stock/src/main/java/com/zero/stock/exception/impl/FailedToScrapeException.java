package com.zero.stock.exception.impl;

import com.zero.stock.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class FailedToScrapeException extends AbstractException {
    @Override
    public int getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getError() {
        return "스크래핑에 실패했습니다.";
    }
}
