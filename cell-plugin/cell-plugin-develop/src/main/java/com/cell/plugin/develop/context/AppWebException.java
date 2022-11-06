package com.cell.plugin.develop.context;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: amber
 * Date: 2018-05-17
 * Time: 10:45
 */
public class AppWebException extends Exception{

    private static final long serialVersionUID = -8198281171334131008L;

    private int errCode;

    public AppWebException() {}

    public AppWebException(int errCode, String message,
                           Exception exception) {
        super(message, exception);

        this.errCode = errCode;
    }

    public AppWebException(int errCode, String message) {
        this(errCode, message,null);
    }

    public int getErrCode() {
        return this.errCode;
    }
}
