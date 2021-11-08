package com.cell.base.common.exceptions;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 14:35
 */
public class MessageNotDoneException extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public MessageNotDoneException()
    {
        super();
    }

    public MessageNotDoneException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public MessageNotDoneException(String message)
    {
        super(message);
    }

    public MessageNotDoneException(Throwable cause)
    {
        super(cause);
    }
}

