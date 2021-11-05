package com.youtube.exception;

import java.io.IOException;

public class YoutubeResponseParseException extends IOException {
    public YoutubeResponseParseException(String msg, Throwable e) {
        super(msg, e);
    }
}
