package com.cell.log_origin;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-14 09:53
 */
public enum LogTypeEnums
{
    NORMAL(1, "普通的日志信息");

    private int code;
    private String description;

    LogTypeEnums(int code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }}
