package com.cell.reactor;

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
    long getResultTimeout();
}
