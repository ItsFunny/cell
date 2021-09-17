package com.cell.channel;

import com.cell.handler.IChainHandler;
import com.cell.handler.IHandler;
import com.cell.hooks.IChainExecutor;
import com.cell.manager.WebHandlerManager;
import com.cell.protocol.ICommandSuit;
import com.cell.services.Pipeline;
import com.cell.services.impl.DefaultHookMutableChainExecutor;
import com.cell.services.impl.DefaultPipeline;
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
        IChainExecutor executor = this.pipeline.chainExecutor();
        executor.execute(cmdCtx).subscribe();

    }
}
