package com.cell.exception;


import com.cell.base.common.enums.CellError;
import com.cell.base.common.exceptions.AbstractZZException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 05:29
 */
public class RPCFrameworkException extends AbstractZZException
{

    public RPCFrameworkException(String msg)
    {
        super(msg);
    }

    public RPCFrameworkException(String message, String message1)
    {
        super(message, message1);
    }

    public RPCFrameworkException(String message, Throwable cause, String message1)
    {
        super(message, cause, message1);
    }

    public RPCFrameworkException(Throwable cause, String message)
    {
        super(cause, message);
    }

    public RPCFrameworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1)
    {
        super(message, cause, enableSuppression, writableStackTrace, message1);
    }

    public RPCFrameworkException(CellError errorCode)
    {
        super(errorCode);
    }

    public RPCFrameworkException(CellError errorCode, String message)
    {
        super(errorCode, message);
    }

    public RPCFrameworkException(CellError errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

    public RPCFrameworkException(CellError errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }

    public RPCFrameworkException(Throwable cause)
    {
        super(cause);
    }

    public RPCFrameworkException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
