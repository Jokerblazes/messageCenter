package com.kuaijie.center.exception;

/**
 *
 * Created by joker on 2017/12/8.
 */
public class UriException extends RuntimeException {

    public UriException() {
        super("uri不允许为空！");
    }

    public UriException(String msg) {
        super(msg);
    }
}
