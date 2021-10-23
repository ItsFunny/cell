package com.cell.protocol;

import com.cell.constant.HttpConstants;
import com.cell.constants.NetworkConstants;
import com.cell.couple.IHttpServerRequest;
import com.cell.couple.IHttpServerResponse;
import com.cell.util.HttpUtils;
import com.cell.utils.StringUtils;
import com.cell.utils.UUIDUtils;
import lombok.Data;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:41
 */
@Data
public class CommandContext
{
    public static final String TOKEN = "token";
    private IHttpServerRequest httpRequest;
    private IHttpServerResponse httpResponse;
    // 返回给 http上层的结果值
    private DeferredResult<Object> responseResult;
    private Throwable exception;
    private String sessionKey;
    private String funcName;
    private HttpSummary summary;

    public CommandContext(IHttpServerRequest httpRequest, IHttpServerResponse httpResponse,
                          long defaultTimeount)
    {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;

        this.funcName = httpRequest.getInternalRequest().getRequestURI();
        summary = collectSummary(); // 从servletRequest中收集基本信息
        sessionKey = getHeaderData(NetworkConstants.SESSION_KEY);
        String timeout = getHeaderData(NetworkConstants.TIME_OUT);
        if (!StringUtils.isEmpty(timeout))
        {
            try
            {
                defaultTimeount = Long.parseLong(timeout);
            } catch (Exception e)
            {
            }
        }
        DeferredResult<Object> responseResult = new DeferredResult<>(defaultTimeount);
        this.responseResult = responseResult;
        this.httpResponse.setDeferredResponse(responseResult);
    }


    private HttpSummary collectSummary()
    {
        HttpSummary httpSummary = new HttpSummary();
        httpSummary.setRequestIP(HttpUtils.getIpAddress(this.httpRequest.getInternalRequest()));
        httpSummary.setRequestUrl(this.httpRequest.getInternalRequest().getRequestURL().toString());
        httpSummary.setToken(getHeaderData(TOKEN));
        httpSummary.setReceiveTimestamp(System.currentTimeMillis());
        httpSummary.setVersionInt(getHeaderData(HttpConstants.VERSION_INT));
        httpSummary.setVersion(getHeaderData(HttpConstants.VERSION));
        httpSummary.setSequenceId(getHeaderData(HttpConstants.SEQUENCE_ID, UUIDUtils.uuid2()));
        return httpSummary;
    }

    private String getHeaderData(String headerName)
    {
        return this.httpRequest.getInternalRequest().getHeader(headerName);
    }

    private String getHeaderData(String headerName, String defaultValue)
    {
        String ret = this.httpRequest.getInternalRequest().getHeader(headerName);
        ret = StringUtils.isEmpty(ret) ? defaultValue : ret;
        return ret;
    }
    // FIXME ,需要定制化
//    public void discard() throws IOException
//    {
//        if (!getResponseResult().isSetOrExpired())
//        {
//            Throwable cause = this.getException();
//            if (cause != null)
//            {
//                getHttpResponse().setStatus(HttpStatus.BAD_REQUEST.value());
//                if (cause instanceof IOException)
//                {
//                    if (cause.getCause() != null && cause.getCause() instanceof MessageNotDoneException)
//                    {
//                        getHttpResponse().addHeader("errorCode", String.valueOf(0));
//                        getHttpResponse().addHeader("errorDesc", cause.getMessage());
//                        getHttpResponse().sendError(HttpStatus.OK.value());
//                        return;
//                    }
//                } else if (cause instanceof CommonBusinessException)
//                {
//                    CommonBusinessException ex = (CommonBusinessException) cause;
////                    String errorDesc = ex.getMessage();
//                    getHttpResponse().addHeader("errorCode", String.valueOf(0));
//                    getHttpResponse().addHeader("errorDesc", cause.getMessage());
//                    getHttpResponse().setStatus(HttpStatus.OK.value());
//                    getResponseResult().setResult(null);
//                    return;
//                } else
//                {
//                    getHttpResponse().sendError(HttpStatus.BAD_REQUEST.value());
//                }
//            } else
//            {
//                if (!getHttpResponse().isCommitted())
//                {
//                    getHttpResponse().sendError(HttpStatus.BAD_REQUEST.value());
//                }
//                getResponseResult().setErrorResult("Bad Request !!!");
//            }
//        }
//    }

    public String getURI()
    {
        return this.httpRequest.getInternalRequest().getRequestURI();
    }
}
