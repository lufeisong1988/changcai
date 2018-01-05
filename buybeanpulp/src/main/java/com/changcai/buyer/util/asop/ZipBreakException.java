package com.changcai.buyer.util.asop;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ZipBreakException extends RuntimeException{

    public ZipBreakException(String msg) {
        super(msg);
    }

    public ZipBreakException(Exception e) {
        super(e);
    }

    public ZipBreakException() {
    }
}
