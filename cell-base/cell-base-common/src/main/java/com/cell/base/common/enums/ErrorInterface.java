package com.cell.base.common.enums;

public interface ErrorInterface
{
    default boolean ok()
    {
        return this.getCode() == 0;
    }

    int getCode();

    String getMsg();
}
