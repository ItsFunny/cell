package com.cell.channel;

import com.cell.base.core.channel.IHttpChannel;
import com.cell.base.core.handler.IChainHandler;
import com.cell.base.core.handler.IHandler;
import com.cell.pipeline.DefaultPipeline;
import com.cell.pipeline.Pipeline;
import com.cell.base.core.protocol.ICommandSuit;
import com.cell.base.core.services.impl.DefaultHookMutableChainExecutor;
import lombok.Data;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-19 14:58
 */
@Data
public class DefaultHttpChannel implements IHttpChannel
{
    private Pipeline<IHandler, IChainHandler> pipeline;

    private static final DefaultHttpChannel instance = new DefaultHttpChannel();

    public static DefaultHttpChannel getInstance()
    {
        return instance;
    }

    private DefaultHttpChannel()
    {
        this.pipeline = new DefaultPipeline<>(DefaultHookMutableChainExecutor::new);
    }

    @Override
    public Pipeline pipeline()
    {
        return this.pipeline;
    }

    @Override
    public Mono<Void> exceptionCaught(ICommandSuit suit, Throwable e)
    {
        return this.pipeline.chainExecutor().exceptionCaught(suit, e);
    }

    @Override
    public void readCommand(ICommandSuit cmdCtx)
    {
        this.pipeline.chainExecutor().execute(cmdCtx).onErrorResume((e) ->
                this.exceptionCaught(cmdCtx, e)).subscribe();
    }
}
