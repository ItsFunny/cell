package com.cell.plugin.develop.context;

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
    ARGUMENT_ILLEGAL(703, "Argument is illegal"),
    ILLEGAL_OPERATION(704, "illegal operation"),
    ILLEGAL_OPERATION_OF_UPDATING_CHEST_STATUS(705, "illegal opration of updating chest status"),
    ILLEGAL_OPERATION_OF_UPDATING_CHEST_STATUS_NOT_TIME_YET(706, "did not meet unlock time "),
    ILLEGAL_OPERATION_OF_NFT_IS_TIRED(707, "ILLEGAL_OPERATION_OF_NFT_IS_TIRED"),
    ILLEGAL_OPERATION_OF_COMPLETE_CHAPTER(708, "the chapter is not finished yet"),

    ARGUMENT_ILLEGAL_WRONG_ROLE(709,"argument is illegal,role is not the concrete terrain type"),
    Insufficient_balance(709, "Insufficient balance"),
    ARGUMENT_ILLEGAL_ROLE_IS_NOT_AVAILABLE(710,"argument is illegal,role is not available ,maybe it is in fighting"),
    ARGUMENT_ILLEGAL_THE_MAPLEVEL_IS_RUNNING(711,"argument is illegal,this maplevel is already fighting"),
    ARGUMENT_ILLEGAL_THE_COMPETION_IS_FINISHED(712,"argument is illegal,this maplevel is finished"),
    ARGUMENT_ILLEGAL_THE_CHEST_IS_RECEIVED(713,"argument is illegal,this chest is received"),
    ARGUMENT_ILLEGAL_THE_MAPLEVEL_MATCH_ERROR(713,"argument is illegal,this maplevel  is not matched with the chapter"),

    ILLEGAL_OPERATION_THE_CHEST_IS_NOT_RECEIVED(810, "illegal opration ,the chest is not received"),
    ILLEGAL_OPERATION_THE_CHEST_IS_OPENED(811, "illegal opration ,the chest is opend"),
    ILLEGAL_OPERATION_THE_CHEST_IS_NOT_LOCKED(812, "illegal opration ,the chest is not locked"),
    ILLEGAL_OPERATION_THE_NFT_LEVEL_IS_NOT_MATCHED(812, "illegal opration ,the nft's level is not matched"),


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
