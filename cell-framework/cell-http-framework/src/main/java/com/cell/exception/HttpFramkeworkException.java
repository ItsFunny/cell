package com.cell.exception;

import com.cell.rpc.client.base.framework.command.IHttpCommand;
import com.cell.enums.CellError;
import com.cell.exceptions.AbstractZZException;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:59
 */
@Data
public class HttpFramkeworkException extends AbstractZZException
{
    private IHttpCommand command;
    public HttpFramkeworkException(IHttpCommand command, CellError error, String msg)
    {
        super(error,msg);
        this.command=command;
    }

    public HttpFramkeworkException(String message, String message1)
    {
        super(message, message1);
    }

    public HttpFramkeworkException(String message, Throwable cause, String message1)
    {
        super(message, cause, message1);
    }

    public HttpFramkeworkException(Throwable cause, String message)
    {
        super(cause, message);
    }

    public HttpFramkeworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1)
    {
        super(message, cause, enableSuppression, writableStackTrace, message1);
    }

    public HttpFramkeworkException(CellError errorCode)
    {
        super(errorCode);
    }

    public HttpFramkeworkException(CellError errorCode, String message)
    {
        super(errorCode, message);
    }

    public HttpFramkeworkException(CellError errorCode, String message, Throwable cause)
    {
        super(errorCode, message, cause);
    }

    public HttpFramkeworkException(CellError errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }

    public HttpFramkeworkException(Throwable cause)
    {
        super(cause);
    }

    public HttpFramkeworkException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
