package com.cell.enums;

/**
 * @author Charlie
 * @When
 * @Description 用于链式结构
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-17 21:56
 */
public enum ChainEnums
{
    // 用于logic
    LOGIC_LOG(1000),


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

    ChainEnums(int status)
    {
        this.status = status;
    }
}