package com.cell.exception;

import com.cell.base.common.enums.CellError;
import com.cell.base.common.exceptions.AbstractZZException;

public class ConfigurationException extends AbstractZZException
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ConfigurationException(Throwable e)
    {
        super(e);
    }

    public ConfigurationException(CellError error, String msg)
    {
        super(error, msg);
    }

    public ConfigurationException(String msg)
    {
        super(msg);
    }
}
