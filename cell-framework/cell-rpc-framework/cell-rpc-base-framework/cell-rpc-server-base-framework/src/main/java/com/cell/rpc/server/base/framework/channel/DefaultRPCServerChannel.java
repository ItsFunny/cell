package com.cell.rpc.server.base.framework.channel;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 09:26
 */
public class DefaultRPCServerChannel extends AbstractRPCServerChannel
{

    private static final DefaultRPCServerChannel instance = new DefaultRPCServerChannel();

    public static DefaultRPCServerChannel getInstance()
    {
        return instance;
    }
}
