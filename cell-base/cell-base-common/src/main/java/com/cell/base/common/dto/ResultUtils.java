package com.cell.base.common.dto;

import com.cell.base.common.enums.ErrorInterface;

public class ResultUtils
{

    public static <T> ResultDTO<T> success(T data)
    {
        ResultDTO<T> ret = new ResultDTO<>();
        ret.setSuccess(true);
        ret.setData(data);
        return ret;
    }

    public static <T> ResultDTO<T> fail(ErrorInterface errorConstant, T data)
    {
        ResultDTO<T> ret = new ResultDTO<>();
        ret.setSuccess(false);
        ret.setErrCode(errorConstant.getCode());
        ret.setErrMsg(errorConstant.getMsg());
        ret.setData(data);
        return ret;
    }

    public static <T> ResultDTO<T> fail(int code, String msg, T data)
    {
        ResultDTO<T> ret = new ResultDTO<>();
        ret.setSuccess(false);
        ret.setErrCode(code);
        ret.setErrMsg(msg);
        ret.setData(data);
        return ret;
    }
}
