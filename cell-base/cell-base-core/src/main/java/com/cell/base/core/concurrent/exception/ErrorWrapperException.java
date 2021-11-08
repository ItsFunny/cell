package com.cell.base.core.concurrent.exception;


import com.cell.base.common.enums.CellError;
import com.cell.base.common.exceptions.AbstractZZException;

public class ErrorWrapperException extends AbstractZZException
{
    public ErrorWrapperException(CellError errorCode)
    {
        super(errorCode, "", null);
    }
}
