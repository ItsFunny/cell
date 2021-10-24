package com.cell.couple;

import com.cell.protocol.IServerRequest;

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
