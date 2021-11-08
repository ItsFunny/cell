package com.cell.sdk.log;


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
    NORMAL(1l, "普通的日志信息"),
    GC(2l, "GC日志"),
    ILLEDGAL_USER(3L, "违法用户"),
    THIRD_PARTY((long) 1001, "第三方模块"),
    ;

    private Long code;
    private String description;

    LogTypeEnums(Long code, String description)
    {
        this.code = code;
        this.description = description;
    }

    public Long getCode()
    {
        return code;
    }

    public long getValue()
    {
        return code;
    }


    public void setCode(Long code)
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
