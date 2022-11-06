package com.cell.plugin.develop.context;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.ConnectException;

/**
 * @Description: 全局异常
 * Author lv bin
 * @date 2018/5/16 9:35
 * version V1.0.0
 */


@ControllerAdvice
public class GlobalExceptionHandler
{

    protected static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ICellExceptionHandler exceptionHandler;

    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseResult<String> runtimeExceptionHandler(RuntimeException runtimeException)
    {
        return result(ErrorConstant.RUNTIME_EXCEPTION.getCode(), ErrorConstant.RUNTIME_EXCEPTION.getMsg(), runtimeException);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseResult<String> argumentExceptionHandler(IllegalArgumentException runtimeException)
    {
        return result(ErrorConstant.ARGUMENT_ILLEGAL.getCode(), ErrorConstant.ARGUMENT_ILLEGAL.getMsg(), runtimeException);
    }

    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ResponseResult<String> nullPointerExceptionHandler(NullPointerException ex)
    {
        return result(ErrorConstant.NULL_POINTER_EXCEPTION.getCode(), ErrorConstant.NULL_POINTER_EXCEPTION.getMsg(), ex);
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    @ResponseBody
    public ResponseResult<String> classCastExceptionHandler(ClassCastException ex)
    {
        return result(ErrorConstant.CLASS_CAST_EXCEPTION.getCode(), ErrorConstant.CLASS_CAST_EXCEPTION.getMsg(), ex);
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResponseResult<String> iOExceptionHandler(IOException ex)
    {
        return result(ErrorConstant.IO_EXCEPTION.getCode(), ErrorConstant.IO_EXCEPTION.getMsg(), ex);
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    @ResponseBody
    public ResponseResult<String> noSuchMethodExceptionHandler(NoSuchMethodException ex)
    {
        return result(ErrorConstant.NO_SUCH_METHOD_EXCEPTION.getCode(), ErrorConstant.NO_SUCH_METHOD_EXCEPTION.getMsg(), ex);
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseBody
    public ResponseResult<String> indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex)
    {
        return result(ErrorConstant.INDEX_OUT_OF_BOUNDS_EXCEPTION.getCode(), ErrorConstant.INDEX_OUT_OF_BOUNDS_EXCEPTION.getMsg(), ex);
    }

    //网络异常
    @ExceptionHandler(ConnectException.class)
    @ResponseBody
    public ResponseResult<String> connectException(ConnectException ex)
    {
        return result(ErrorConstant.CONNECT_EXCEPTION.getCode(), ErrorConstant.CONNECT_EXCEPTION.getMsg(), ex);
    }

    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseResult<String> requestNotReadable(HttpMessageNotReadableException ex)
    {
        return result(ErrorConstant.BAD_REQUEST.getCode(), ErrorConstant.BAD_REQUEST.getMsg(), ex);
    }

    //400错误
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public ResponseResult<String> requestTypeMismatch(TypeMismatchException ex)
    {
        return result(ErrorConstant.BAD_REQUEST.getCode(), ErrorConstant.BAD_REQUEST.getMsg(), ex);
    }

    //400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public ResponseResult<String> requestMissingServletRequest(MissingServletRequestParameterException ex)
    {
        return result(ErrorConstant.BAD_REQUEST.getCode(), ErrorConstant.BAD_REQUEST.getMsg(), ex);
    }

    @ExceptionHandler({ServletException.class})
    @ResponseBody
    public ResponseResult<String> http404(ServletException ex)
    {
        return result(ErrorConstant.NOT_FOUND_REQUEST.getCode(), ErrorConstant.NOT_FOUND_REQUEST.getMsg(), ex);
    }

    //405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public ResponseResult<String> request405(HttpRequestMethodNotSupportedException ex)
    {
        return result(ErrorConstant.METHOD_NOT_ALLOWED.getCode(), ErrorConstant.METHOD_NOT_ALLOWED.getMsg(), ex);
    }

    //406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public ResponseResult<String> request406(HttpMediaTypeNotAcceptableException ex)
    {
        return result(ErrorConstant.NOT_ACCEPTABLE.getCode(), ErrorConstant.NOT_ACCEPTABLE.getMsg(), ex);
    }

    //500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    @ResponseBody
    public ResponseResult<String> server500(RuntimeException runtimeException)
    {
        return result(ErrorConstant.INTERNAL_SERVER_ERROR.getCode(), ErrorConstant.INTERNAL_SERVER_ERROR.getMsg(), runtimeException);
    }

    //app web 异常
    @ExceptionHandler({AppWebException.class})
    @ResponseBody
    public ResponseResult<String> appWebException(AppWebException appWebException)
    {
        return result(appWebException.getErrCode(), appWebException.getMessage(), appWebException);
    }

    @ExceptionHandler({JsonMappingException.class})
    @ResponseBody
    public ResponseResult<String> jsonMappingException(JsonMappingException jsonMappingException)
    {
        return result(ErrorConstant.ERROR_FORMAT_PARAMETER.getCode(), ErrorConstant.ERROR_FORMAT_PARAMETER.getMsg(), jsonMappingException);
    }


    /**
     * 结果集
     *
     * @param errCode
     * @param errMsg
     * @param e
     * @return
     */


    private ResponseResult<String> result(int errCode, String errMsg, Exception e)
    {
        CellContext ctx = null;
        if (e instanceof WrapContextException)
        {
            Exception exception = ((WrapContextException) e).getException();
            ctx = ((WrapContextException) e).getContext();
            if (exception instanceof BusinessException)
            {
                errCode = Integer.parseInt(((BusinessException) exception).getCode());
                errMsg = exception.getMessage();
            }
        }
        ResponseResult<String> ResponseResult = new ResponseResult<String>();
        ResponseResult.setCode(errCode);
        ResponseResult.setMessage(errMsg);

        logException(ctx, e);

        return ResponseResult;
    }

    /**
     * 异常记录
     *
     * @param e
     */


    private void logException(CellContext context, Exception e)
    {
        if (e instanceof AppWebException)
        {
            LOGGER.warn(e.getMessage(), e);
        } else
        {
            if (context == null)
            {
                LOGGER.error(e.getMessage(), e);
            } else
            {
                LOGGER.error(e.getMessage(), e, context, context.getRequestData());
            }
        }
    }
}
