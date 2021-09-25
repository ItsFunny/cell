package com.cell.context;

import com.cell.protocol.IBuzzContext;
import com.cell.reactor.IHttpReactor;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 15:31
 */
public interface IHttpCommandContext extends IBuzzContext
{
    IHttpReactor getHttpReactor();
    HttpServletRequest getHttpRequest();
    Map<String, String> getUriRegexValue();

    DeferredResult<Object> getResult();

    String getURI();
    Map<String, String> getPathUri();
}
