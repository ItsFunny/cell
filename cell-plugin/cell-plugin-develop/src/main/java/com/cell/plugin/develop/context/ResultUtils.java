package com.cell.plugin.develop.context;



public class ResultUtils
{
    public static <T> ResponseResult<T> success(T data)
    {
        ResponseResult<T> ret = new ResponseResult<>();
        ret.setCode(200);
        ret.setData(data);
        return ret;
    }

    public static <T> ResponseResult<T> fail(ErrorConstant errorConstant, T data)
    {
        ResponseResult<T> ret = new ResponseResult<>();
        ret.setCode(errorConstant.getCode());
        ret.setMessage(errorConstant.getMsg());
        ret.setData(data);
        return ret;
    }

    public static <T> ResponseResult<T> fail(int code, String msg, T data)
    {
        ResponseResult<T> ret = new ResponseResult<>();
        ret.setCode(code);
        ret.setMessage(msg);
        ret.setData(data);
        return ret;
    }
}
