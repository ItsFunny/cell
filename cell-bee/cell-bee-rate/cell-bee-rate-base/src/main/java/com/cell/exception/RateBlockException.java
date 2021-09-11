package com.cell.exception;

import com.cell.enums.CellError;
import com.cell.exceptions.AbstractZZException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 07:56
 */
public class RateBlockException extends AbstractZZException
{

    public RateBlockException(String msg)
    {
        super(msg);
    }

    public RateBlockException(String message, String message1)
    {
        super(message, message1);
    }

    public RateBlockException(String message, Throwable cause, String message1)
    {
        super(message, cause, message1);
    }

    public RateBlockException(Throwable cause, String message)
    {
        super(cause, message);
    }

    public RateBlockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1)
    {
        super(message, cause, enableSuppression, writableStackTrace, message1);
    }

    public RateBlockException(CellError errorCode)
    {
        super(errorCode);
    }

    public RateBlockException(CellError errorCode, String message)
    {
        super(errorCode, message);
    }

    public RateBlockException(CellError errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

    public RateBlockException(CellError errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }

    public RateBlockException(Throwable cause)
    {
        super(cause);
    }

    public RateBlockException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
