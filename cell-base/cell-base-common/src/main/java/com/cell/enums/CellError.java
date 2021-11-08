package com.cell.enums;


public enum CellError
{
    NO_ERROR(0, "", "ALL"),
    UNKNOWN_ERROR(1, "未知错误", "ALL"),
    PROMISE_TIMEOUT(1001, "Promise 设置结果超时", "COMMON"),


    EXECUTE_TIMEOUT(10001, "业务执行超时", "");
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
