package com.cell.node.core.exception;


import com.cell.base.common.exceptions.AbstractRuntimeException;

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
