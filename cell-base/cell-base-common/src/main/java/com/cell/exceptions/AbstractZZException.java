package com.cell.exceptions;


import com.cell.enums.CellError;

@SuppressWarnings("serial")
public abstract class AbstractZZException extends Exception
{

	private CellError errorCode = CellError.UNKNOWN_ERROR;
	private volatile String message;

	public CellError getErrorCode() {
		return this.errorCode;
	}

	public AbstractZZException() {
		this("", null);
	}

	public AbstractZZException(CellError errorCode) {
		this(errorCode, "", null);
	}

	public AbstractZZException(CellError errorCode, String message) {
		this(errorCode, message, null);
	}

	public AbstractZZException(CellError errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public AbstractZZException(CellError errorCode, Throwable cause) {
		this(errorCode, "", cause);
	}

	public AbstractZZException(Throwable cause) {
		this("", cause);
	}

	public AbstractZZException(String message, Throwable cause) {
		super(message, cause);
//		parseAnnotation();
	}

	public AbstractZZException(String message) {
		this(message, null);
	}

	@Override
	public String getMessage() {
		if (message == null) {
			if (errorCode != null) {
				message = super.getMessage() + " Exception Error:\n" + errorCode.toString();
			} else {
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
