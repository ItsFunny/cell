package com.cell.node.core.exception;

import com.cell.base.common.enums.ErrorInterface;

public enum ErrorConstant implements ErrorInterface
{
    BAD_REQUEST(400, "Bad Request!"),
    NOT_AUTHORIZATION(401, "NotAuthorization"),
    NOT_FOUND_REQUEST(404, "Not Found Request Path"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    TOKEN_ILLEGAL(600, "TOKEN ILLEGAL"),
    TOKEN_EXPIRED(601, "TOKEN EXPIRED"),
    AUTH_FAILED(700, "Insufficient permissions"),
    USER_FORBIDDEN(701, "User is banned"),
    USER_NOT_LOGIN(702, "User is not login"),
    ARGUMENT_ILLEGAL(703, "Argument is illegal"),
    ACCOUNT_ALREADY_EXISTS(998, "[SERVER]Account already exists"),
    LOGIN_FIRST(999, "[Server] Please login firstly"),

    RUNTIME_EXCEPTION(1000, "[Server]Runtime Error"),
    NULL_POINTER_EXCEPTION(1001, "[Server]NullPointerException"),
    CLASS_CAST_EXCEPTION(1002, "[Server] Cast Exception"),
    IO_EXCEPTION(1003, "[Server] IO Exception,Please Wait for a several minuts"),
    NO_SUCH_METHOD_EXCEPTION(1004, "[Server]No Such Method"),
    INDEX_OUT_OF_BOUNDS_EXCEPTION(1005, "[Server]Array Index Out Of Bound"),
    CONNECT_EXCEPTION(1006, "[Server]Network is busy"),
    ERROR_MEDIA_TYPE(1007, "[Server]Content-type Illegal"),
    EMPTY_REQUEST_BOYD(1008, "[Server]Request is empty"),
    ERROR_REQUEST_BOYD(1009, "[Server]Request is illegal"),
    ERROR_VERSION(2000, "[Server]Version mismatch"),
    ERROR_FORMAT_PARAMETER(2001, "[Server]Argument illegal"),


    RECORD_NOT_EXISTS(3000, "[DB] Record Not Exists"),
    RECORD_ALREADY_EXISTS(30001, "[DB]Record Already Exists"),

    SERVER_INIT_CONFIG(40000, "Server forget to initialize config"),
    ITEM_DISABLE_UPDATE_FOR_SELL(100000, "This item is on sell, You cant modify"),
    USER_NOT_ALLOWED_TO_MODIFY_OTHER_USER_PASSWORD(1000001, "USER_NOT_ALLOWED_TO_MODIFY_OTHER_USER_PASSWORD"),
    EXPORT_FAILED(1000002, "export failed"),

    ;

    private final int code;
    private final String msg;

    @Override
    public int getCode()
    {
        return code;
    }

    @Override
    public String getMsg()
    {
        return msg;
    }

    ErrorConstant(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public static String getNameByValue(Integer val)
    {
        if (val != null)
        {
            int value = val;
            for (ErrorConstant constant : ErrorConstant.values())
            {
                if (constant.code == value)
                {
                    return constant.msg;
                }
            }
        }
        return "";
    }

    public ErrorConstant getTypeByValue(int value)
    {
        for (ErrorConstant constant : ErrorConstant.values())
        {
            if (constant.code == value)
            {
                return constant;
            }
        }
        return null;
    }

}
