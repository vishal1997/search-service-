package com.youtube.search.exception;

public class HttpException extends Exception {

    public HttpException(String msg, Throwable t) {
        super(msg, t);
    }

    public HttpException(String msg) {
        super(msg);
    }
}
