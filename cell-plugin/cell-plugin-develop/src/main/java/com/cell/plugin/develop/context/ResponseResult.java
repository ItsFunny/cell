package com.cell.plugin.develop.context;



import java.io.Serializable;
import java.util.Date;

public class ResponseResult<T> implements Serializable{
    private int code;
    private String message;
    private T data;
    private long tm;

    public ResponseResult() {
        this.code = ResultEnum.SUCCESS.getCode();
        this.message = ResultEnum.SUCCESS.getMessage();
        this.tm = new Date().getTime();
    }

    public ResponseResult(int code, String message) {
        this.code = code;
        this.message = message;
        this.tm = new Date().getTime();
    }

    public ResponseResult(ResultEnum resultEnum ) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.tm = new Date().getTime();
    }
    
    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.tm = new Date().getTime();
    }

    public ResponseResult(ResultEnum resultEnum , T data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
        this.tm = new Date().getTime();
    }

    public ResponseResult(T data) {
        this.code = ResultEnum.SUCCESS.getCode();
        this.message = ResultEnum.SUCCESS.getMessage();
        this.data = data;
        this.tm = new Date().getTime();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTm() {
        return tm;
    }

    public void setTm(long tm) {
        this.tm = tm;
    }
}
