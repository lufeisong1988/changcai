package com.changcai.buyer.interface_api;

/**
 * Created by liuxingwei on 2017/2/24.
 */

public enum ErrorCode {


    /**
     * 网络错误
     */
    NET_ERROR("NET_ERROR", "网络异常，请稍后重试"),

    SYSTEM_ERROR("SYSTEM_ERROR", "系统错误"),

    SERVER_ERROR("SERVER_ERROR","服务器异常，请稍后重试"),
    /**
     * json解析错误==服务器返回错误
     */
    PARSER_EXCEPTION("JSON_PARSER", "未知错误"),

    /**
     *链接服务器异常
     */
    LINK_EXCEPTION("LINK_EXCEPTION","连接服务器失败，请稍后重试"),

    /**
     * 代买逻辑错误
     */
    IllegalArgumentException("ARGUMENT_ILLEGAL","参数非法"),
    ContextNullException("ContextNullException","ContextNull"),


    /**
     *
     */

    LOGIC_EXCEPTION("server","message");
    /**
     * 错误编码
     */
    private String code;

    /**
     * 错误描述
     */
    private String desc;

    /**
     * @param code
     * @param desc
     */
    private ErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    /**
     * @param code
     * @return
     */
    public static ErrorCode getByCode(String code) {
        for (ErrorCode item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>desc</tt>.
     *
     * @return property value of desc
     */
    public String getDesc() {
        return desc;
    }

}
