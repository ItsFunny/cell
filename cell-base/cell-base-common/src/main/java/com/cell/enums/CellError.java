package com.cell.enums;


public enum CellError
{
    UNKNOWN_ERROR(1, "未知错误", "ALL"),
    ;
    private int code;
    private String desc;
    private String module;

    CellError(int code, String desc, String module)
    {
        this.code = code;
        this.desc = desc;
        this.module = module;
    }
}
