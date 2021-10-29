package com.cell.concurrent.exception;


import com.cell.enums.CellError;
import com.cell.exceptions.AbstractZZException;

//@Error(error = CellError.PROMISE_TIMEOUT)
public class PromiseTimeoutException extends AbstractZZException
{
    public PromiseTimeoutException() {
    	super(CellError.PROMISE_TIMEOUT.name());
    }
}
