package com.cell.exceptions;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 12:19
 */
public class InternalWrapperException extends RuntimeException
{
    public InternalWrapperException(Throwable cause)
    {
        super(cause);
    }
}
