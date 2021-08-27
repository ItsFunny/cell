package com.cell.protocol;

import com.cell.constant.HttpConstants;
import com.cell.constants.NetworkConstants;
import com.cell.util.HttpUtils;
import lombok.Data;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private DeferredResult<Object> responseResult;
    private long requestTimestamp;
    private Throwable exception;
    private String sessionKey;
    private String funcName;

    private HttpSummary summary;

    public CommandContext(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                          DeferredResult<Object> responseResult, String thirdPath)
    {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
        this.responseResult = responseResult;
        this.requestTimestamp = System.currentTimeMillis();
        this.funcName = thirdPath;
        summary = collectSummary(); // 从servletRequest中收集基本信息
        sessionKey = getHeaderData(NetworkConstants.SESSION_KEY);
    }

    private HttpSummary collectSummary()
    {
        HttpSummary httpSummary = new HttpSummary();
        httpSummary.setRequestIP(HttpUtils.getIpAddress(httpRequest));
        httpSummary.setRequestUrl(httpRequest.getRequestURL().toString());
        httpSummary.setToken(getHeaderData(TOKEN));
        httpSummary.setReceiveTimestamp(System.currentTimeMillis());
        httpSummary.setVersionInt(getHeaderData(HttpConstants.VERSION_INT));
        httpSummary.setVersion(getHeaderData(HttpConstants.VERSION));
        httpSummary.setSequenceId(getHeaderData(HttpConstants.SEQUENCE_ID));
        return httpSummary;
    }

    private String getHeaderData(String headerName)
    {
        return this.httpRequest.getHeader(headerName);
    }

}
