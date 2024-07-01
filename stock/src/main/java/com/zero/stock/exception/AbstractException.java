package com.zero.stock.exception;

public abstract class AbstractException extends RuntimeException{
    abstract public int getStatus();
    abstract public String getError();
}
