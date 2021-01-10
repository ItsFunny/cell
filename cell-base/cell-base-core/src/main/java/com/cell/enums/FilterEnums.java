package com.cell.enums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-17 21:56
 */
public enum FilterEnums
{
    SATISFIED(1),
    UN_SATISFIED(2),
    CONTINUE(3),
    BREAK(4),


    // 用于logic
    LOGIC_LOG(10),

    ;
    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    FilterEnums(int status)
    {
        this.status = status;
    }
}