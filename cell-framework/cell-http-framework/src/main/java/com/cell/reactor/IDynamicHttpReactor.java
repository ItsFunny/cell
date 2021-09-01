package com.cell.reactor;

import com.cell.command.IDynamicHttpCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:22
 */
public interface IDynamicHttpReactor extends IHttpReactor
{
    //    List<IDynamicHttpCommand> getCmds();
    void handleRequestResponse(HttpServletRequest request, HttpServletResponse response);
}
