package com.youtube.search.exception;

public class YoutubeSearchException extends HttpException {

    public YoutubeSearchException(String msg, Throwable t) {
        super(msg, t);
    }

    public YoutubeSearchException(String msg) {
        super(msg);
    }
}
