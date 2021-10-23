package com.cell.proxy;

import com.cell.dispatcher.IDispatcher;

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
