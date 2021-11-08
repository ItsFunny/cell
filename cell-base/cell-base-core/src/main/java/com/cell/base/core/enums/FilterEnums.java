package com.cell.base.core.enums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-24 08:17
 */
public enum FilterEnums
{
    SATISFIED(100),
    UN_SATISFIED(101),
    CONTINUE(102),
    BREAK(103),
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
