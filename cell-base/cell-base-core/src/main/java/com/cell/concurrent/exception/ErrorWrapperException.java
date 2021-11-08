package com.cell.concurrent.exception;


import com.cell.enums.CellError;
import com.cell.exceptions.AbstractZZException;

public class ErrorWrapperException extends AbstractZZException
{
    public ErrorWrapperException(CellError errorCode)
    {
        super(errorCode, "", null);
    }
}
