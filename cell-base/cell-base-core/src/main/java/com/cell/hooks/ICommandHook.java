package com.cell.hooks;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 04:45
 */
public interface ICommandHook extends IHook
{
    void onExceptionCaught(Throwable e);
}
