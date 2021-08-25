package com.cell.exception;

import com.cell.exceptions.AbstractRuntimeException;

public class ExtensionImportException extends AbstractRuntimeException
{
    public ExtensionImportException(String message)
    {
        super(message);
    }

    public ExtensionImportException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
