package com.cell.exceptions;

import org.slf4j.helpers.MessageFormatter;

public class CommonBusinessException extends RuntimeException
{

    /**
     *
     */
    private static final long serialVersionUID = -6402774007412540009L;

    private String errorMsg;

    public CommonBusinessException()
    {
        super();
    }

}
