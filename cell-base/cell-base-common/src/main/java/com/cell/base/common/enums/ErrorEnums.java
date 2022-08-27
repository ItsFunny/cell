package com.cell.base.common.enums;

public enum ErrorEnums implements ErrorInterface
{
    NO_ERROR(0, "SUCCESS"),
    CONTINUE(1, "continue"),
    BREAK(2, "break"),
    ERROR(3, "error"),
    ;

    ErrorEnums(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    @Override
    public boolean ok()
    {
        return this == ErrorEnums.NO_ERROR;
    }

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
