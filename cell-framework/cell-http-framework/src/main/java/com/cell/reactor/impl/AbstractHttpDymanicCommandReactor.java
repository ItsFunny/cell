package com.cell.reactor.impl;

import com.cell.reactor.IDynamicHttpReactor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-01 21:12
 */
public abstract class AbstractHttpDymanicCommandReactor extends AbstractHttpCommandReactor implements IDynamicHttpReactor
{
    @Override
    public void handleRequestResponse(HttpServletRequest request, HttpServletResponse response)
    {

    }
}
