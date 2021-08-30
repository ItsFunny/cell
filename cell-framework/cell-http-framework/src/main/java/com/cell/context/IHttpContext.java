package com.cell.context;

import com.cell.command.IHttpCommand;
import com.cell.hook.IHttpCommandHook;
import com.cell.protocol.IContext;
import org.springframework.http.HttpStatus;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 15:31
 */
public interface IHttpContext extends IContext
{
    String getURI();

    void response(HttpStatus status, Object obj);
}
