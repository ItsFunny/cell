package com.cell.base.common.exceptions;

import com.cell.base.common.enums.CellError;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 22:17
 */
public class CommandException extends AbstractZZException
{

    public CommandException(String msg)
    {
        super(msg);
    }

    public CommandException(String message, String message1)
    {
        super(message, message1);
    }

    public CommandException(String message, Throwable cause, String message1)
    {
        super(message, cause, message1);
    }

    public CommandException(Throwable cause, String message)
    {
        super(cause, message);
    }

    public CommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1)
    {
        super(message, cause, enableSuppression, writableStackTrace, message1);
    }

    public CommandException(CellError errorCode)
    {
        super(errorCode);
    }

    public CommandException(CellError errorCode, String message)
    {
        super(errorCode, message);
    }

    public CommandException(CellError errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

    public CommandException(CellError errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }

    public CommandException(Throwable cause)
    {
        super(cause);
    }

    public CommandException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
