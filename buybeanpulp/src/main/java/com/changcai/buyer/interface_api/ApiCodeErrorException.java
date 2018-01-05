package com.changcai.buyer.interface_api;

/**
 * Created by liuxingwei on 2016/11/28.
 */

public class ApiCodeErrorException extends RuntimeException {


    public ApiCodeErrorException() {
        super();
        this.error = ErrorCode.SYSTEM_ERROR;
        this.msg = error.getDesc();
    }

    /**
     * @param cause
     */
    public ApiCodeErrorException(Throwable cause) {
        super(cause.getCause() != null ? cause.getCause().getMessage() : cause.getMessage(),
                cause.getCause() != null ? cause.getCause() : cause);
        if (cause instanceof ApiCodeErrorException) {
            this.error = ((ApiCodeErrorException) cause).getError();
            this.msg = error.getDesc();
        } else {
            this.error = ErrorCode.SYSTEM_ERROR;
            this.msg = error.getDesc();
        }
    }

    /**
     * @param error
     */
    public ApiCodeErrorException(ErrorCode error) {
        super(error.getDesc());
        this.error = error;
        this.msg = error.getDesc();
    }

    /**
     * @param error
     * @param cause
     */
    public ApiCodeErrorException(ErrorCode error, Throwable cause) {
        super(error.getDesc(), cause.getCause() != null ? cause.getCause() : cause);
        this.error = error;
        this.msg = error.getDesc();
    }

    public ApiCodeErrorException(String message) {
        super(message);
        this.error = ErrorCode.SYSTEM_ERROR;
        this.msg = message;
    }

    public ApiCodeErrorException(String message, Throwable cause) {
        super(message, cause.getCause() != null ? cause.getCause() : cause);
        if (cause instanceof ApiCodeErrorException) {
            this.error = ((ApiCodeErrorException) cause).getError();
            this.msg = error.getDesc();
        } else {
            this.error = ErrorCode.SYSTEM_ERROR;
            this.msg = error.getDesc();
        }
    }

    public ApiCodeErrorException(ErrorCode error, String message) {
        super(message);
        this.error = error;
        this.msg = error.getDesc();
    }

    public ApiCodeErrorException(ErrorCode error, String message, Throwable cause) {
        super(message, cause.getCause() != null ? cause.getCause() : cause);
        this.error = error;
        this.msg = error.getDesc();
    }

    /**
     * @param error
     * @param returnObject
     */
    public ApiCodeErrorException(ErrorCode error, Object returnObject) {
        super(error.getDesc());
        this.error = error;
        this.msg = error.getDesc();
    }

    /**
     * @param error
     * @param message
     * @param returnObject
     */
    public ApiCodeErrorException(ErrorCode error, String message, Object returnObject) {
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


    public ApiCodeErrorException(String state, String msg) {
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
