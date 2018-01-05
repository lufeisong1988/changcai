package com.changcai.buyer.okhttp;

import com.changcai.buyer.interface_api.ErrorCode;

/**
 * Created by liuxingwei on 2017/6/13.
 */

public class NetErrorException extends  RuntimeException{

    public NetErrorException() {
        super();
        this.error = ErrorCode.SYSTEM_ERROR;
        this.msg = error.getDesc();
    }

    /**
     * @param cause
     */
    public NetErrorException(Throwable cause) {
        super(cause.getCause() != null ? cause.getCause().getMessage() : cause.getMessage(),
                cause.getCause() != null ? cause.getCause() : cause);
        if (cause instanceof NetErrorException) {
            this.error = ((NetErrorException) cause).getError();
            this.msg = error.getDesc();
        } else {
            this.error = ErrorCode.SYSTEM_ERROR;
            this.msg = error.getDesc();
        }
    }

    /**
     * @param error
     */
    public NetErrorException(ErrorCode error) {
        super(error.getDesc());
        this.error = error;
        this.msg = error.getDesc();
    }

    /**
     * @param error
     * @param cause
     */
    public NetErrorException(ErrorCode error, Throwable cause) {
        super(error.getDesc(), cause.getCause() != null ? cause.getCause() : cause);
        this.error = error;
        this.msg = error.getDesc();
    }

    public NetErrorException(String message) {
        super(message);
        this.error = ErrorCode.SYSTEM_ERROR;
        this.msg = message;
    }

    public NetErrorException(String message, Throwable cause) {
        super(message, cause.getCause() != null ? cause.getCause() : cause);
        if (cause instanceof NetErrorException) {
            this.error = ((NetErrorException) cause).getError();
            this.msg = error.getDesc();
        } else {
            this.error = ErrorCode.SYSTEM_ERROR;
            this.msg = error.getDesc();
        }
    }

    public NetErrorException(ErrorCode error, String message) {
        super(message);
        this.error = error;
        this.msg = error.getDesc();
    }

    public NetErrorException(ErrorCode error, String message, Throwable cause) {
        super(message, cause.getCause() != null ? cause.getCause() : cause);
        this.error = error;
        this.msg = error.getDesc();
    }

    /**
     * @param error
     * @param returnObject
     */
    public NetErrorException(ErrorCode error, Object returnObject) {
        super(error.getDesc());
        this.error = error;
        this.msg = error.getDesc();
    }

    /**
     * @param error
     * @param message
     * @param returnObject
     */
    public NetErrorException(ErrorCode error, String message, Object returnObject) {
        super(message);
        this.error = error;
        this.msg = error.getDesc();
    }

    /**
     * Getter method for property <tt>error</tt>.
     *
     * @return property value of error
     */
    public ErrorCode getError() {
        return error;
    }


    private ErrorCode error;

    private String state;
    private String msg;


    public NetErrorException(String state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public String getLocalizedMessage() {
        return msg;
    }

    public String getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

}
