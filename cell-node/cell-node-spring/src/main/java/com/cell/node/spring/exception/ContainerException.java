package com.cell.node.spring.exception;

import com.cell.enums.CellError;
import com.cell.exceptions.AbstractZZException;

public class ContainerException extends AbstractZZException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ContainerException(CellError errorCode, Throwable cause)
    {
        super(errorCode, cause);
    }

    public ContainerException(Throwable cause)
    {
        super(cause);
    }

    public ContainerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ContainerException(String message)
    {
        super(message);
    }
}
