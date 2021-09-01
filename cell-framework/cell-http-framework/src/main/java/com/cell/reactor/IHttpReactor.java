package com.cell.reactor;

import com.cell.command.IHttpCommand;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 22:51
 */
public interface IHttpReactor extends ICommandReactor
{
    List<Class<? extends IHttpCommand>> getHttpCommandList();
    Class<? extends IHttpCommand> getCmd(String uri);
    long getResultTimeout();
}
