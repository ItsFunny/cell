package com.cell.component.http.filter.exception;


import com.cell.base.common.enums.ErrorInterface;

/**
 * @Title: BusinessException
 * @Description: (自定义运行时异常)
 * @createUsers chenh
 * @createTime 2017/12/5 19:51
 * @return
 */
public class BusinessException extends RuntimeException
{
    private static final long serialVersionUID = -2964851871181700073L;
    private String code;
    private Object data;
    private Integer level;

    public BusinessException()
    {
        super();
    }

    public BusinessException(String code, String message)
    {
        super(message);
        this.code = code;
    }

    public BusinessException(Integer code, String message)
    {
        super(message);
        this.code = String.valueOf(code);
    }

    public BusinessException(Integer code, String message, Integer level)
    {
        super(message);
        this.code = String.valueOf(code);
        this.level = level;
    }


    public BusinessException(ErrorInterface error)
    {
        super(error.getMsg());
        this.code = String.valueOf(error.getCode());
    }

    public BusinessException(ErrorInterface error, Throwable t)
    {
        super(error.getMsg(), t);
        this.code = String.valueOf(error.getCode());
    }

    public BusinessException(String code, String message, Object data, Integer level)
    {
        super(message);
        this.code = code;
        this.level = level;
        this.data = data;
    }

    public BusinessException(String code, String message, Throwable t, Integer level)
    {
        super(message, t);
        this.code = code;
        this.level = level;
    }

    public BusinessException(String code, String message, Throwable t)
    {
        super(message, t);
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }
}
