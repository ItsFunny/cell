package com.cell.base.framework.proxy;

import com.cell.base.framework.dispatcher.IDispatcher;
import com.cell.proxy.IProxy;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 18:40
 */
public interface IFrameworkProxy extends IProxy
{
    IDispatcher getDispatcher();
}
