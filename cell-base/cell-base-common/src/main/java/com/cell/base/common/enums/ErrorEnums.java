package com.cell.base.common.enums;

public enum ErrorEnums implements ErrorInterface
{
    ;

    ErrorEnums(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    @Override
    public int getCode()
    {
        return this.code;
    }

    @Override
    public String getMsg()
    {
        return this.msg;
    }
}
