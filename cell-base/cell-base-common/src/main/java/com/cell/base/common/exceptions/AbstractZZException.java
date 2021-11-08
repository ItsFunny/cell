package com.cell.base.common.exceptions;


import com.cell.base.common.enums.CellError;

@SuppressWarnings("serial")
public abstract class AbstractZZException extends Exception
{

    private CellError errorCode = CellError.UNKNOWN_ERROR;
    private volatile String message;

    public AbstractZZException(String msg)
    {
        super(msg);
        this.message = msg;
    }

    public AbstractZZException(String message, String message1)
    {
        super(message);
        this.message = message1;
    }

    public AbstractZZException(String message, Throwable cause, String message1)
    {
        super(message, cause);
        this.message = message1;
    }

    public AbstractZZException(Throwable cause, String message)
    {
        super(cause);
        this.message = message;
    }

    public AbstractZZException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message1;
    }

    public CellError getErrorCode()
    {
        return this.errorCode;
    }


    public AbstractZZException(CellError errorCode)
    {
        this(errorCode, "", null);
    }

    public AbstractZZException(CellError errorCode, String message)
    {
        this(errorCode, message, null);
    }

    public AbstractZZException(CellError errorCode, String message, Throwable cause)
    {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public AbstractZZException(CellError errorCode, Throwable cause)
    {
        this(errorCode, "", cause);
    }

    public AbstractZZException(Throwable cause)
    {
        this("", cause);
    }

    public AbstractZZException(String message, Throwable cause)
    {
        super(message, cause);
//		parseAnnotation();
    }


    @Override
    public String getMessage()
    {
        if (message == null)
        {
            if (errorCode != null)
            {
                message = super.getMessage() + " Exception Error:\n" + errorCode.toString();
            } else
            {
                message = super.getMessage();
            }
        }
        return message;
    }

//	private void parseAnnotation() {
//		Error error = this.getClass().getAnnotation(Error.class);
//		if (error != null) {
//			this.errorCode = error.error();
//		}
//	}
}
