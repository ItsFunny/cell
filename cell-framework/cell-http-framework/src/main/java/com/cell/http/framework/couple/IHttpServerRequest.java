package com.cell.http.framework.couple;

import com.cell.base.core.protocol.IServerRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-23 00:30
 */
public interface IHttpServerRequest extends IServerRequest
{
    HttpServletRequest getInternalRequest();
}
