package com.kuaijie.center.exception;

/**
 * 访问路径异常
 * Created by joker on 2017/12/8.
 * https://github.com/Jokerblazes/messageCenter.git
 */
public class UriException extends RuntimeException {

    public UriException() {
        super("uri不允许为空！");
    }

    public UriException(String msg) {
        super(msg);
    }
}
