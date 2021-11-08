package com.cell.base.common.enums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-14 05:42
 */
public enum TypeEnums
{
    // 用于logic
    LOG(1000),


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

    TypeEnums(int status)
    {
        this.status = status;
    }
}
