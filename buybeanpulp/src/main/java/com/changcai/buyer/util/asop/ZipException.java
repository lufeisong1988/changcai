package com.changcai.buyer.util.asop;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZipException extends RuntimeException{

    public ZipException(String msg) {
        super(msg);
    }

    public ZipException(Exception e) {
        super(e);
    }

    public ZipException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
