package com.cell.plugin.develop.context;

import com.cell.base.common.enums.ErrorInterface;

public class ErrorEntity implements ErrorInterface {
    
    /** 错误代码 */
    private int code;
    /** 错误消息 */
    private String message;
    /** 错误级别*/
    private int errorLevel;
    
    private ErrorEntity(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return this.message;
    }

    public String getMessage() {
        return message;
    }
    
    public static final ErrorEntity define(int code, String message) {
        return new ErrorEntity(code, message);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorEntity{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", errorLevel=").append(errorLevel);
        sb.append('}');
        return sb.toString();
    }
}
