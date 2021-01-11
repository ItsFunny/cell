package com.cell.enums;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-12 05:40
 */
public enum ExceptionEnums
{
    OUTPUT_ERROR(1l, ""),


    ILLEGAL_ARGUMENT_ERROR(10l, "参数错误"),
    ;

    private Long code;
    private String logicMsg;

    ExceptionEnums(Long code, String logicMsg)
    {
        this.code = code;
        this.logicMsg = logicMsg;
    }

    public Long getCode()
    {
        return code;
    }

    public void setCode(Long code)
    {
        this.code = code;
    }

    public String getLogicMsg()
    {
        return logicMsg;
    }

    public void setLogicMsg(String logicMsg)
    {
        this.logicMsg = logicMsg;
    }}

