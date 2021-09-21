package com.cell.center;

import com.cell.annotations.ActiveMethod;
import com.cell.annotations.ActivePlugin;
import com.cell.annotations.LifeCycle;
import com.cell.annotations.Plugin;
import com.cell.exceptions.ProgramaException;
import com.cell.hooks.IChainExecutor;
import com.cell.hooks.IReactorExecutor;
import com.cell.log.LOG;
import com.cell.log.impl.DefaultLogEventWrapper;
import com.cell.manager.IReflectManager;
import com.cell.models.Module;
import com.cell.protocol.IContext;
import com.cell.services.Pipeline;
import com.cell.services.impl.DefaultPipeline;
import com.cell.utils.CollectionUtils;
import lombok.Data;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.stream.Stream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 12:32
 */
public abstract class AbstractAutoSelectReflectManager implements IReflectManager<IReactorExecutor, IChainExecutor>
{
    private boolean setted;
    protected Map<String, Pipeline<IReactorExecutor, IChainExecutor>> pipeline = new HashMap<>();

    public Mono<Void> execute(String method, IContext ctx)
    {
        Pipeline<IReactorExecutor, IChainExecutor> pip = this.pipeline.get(method);
        if (pip == null)
        {
            throw new ProgramaException("asd");
        }
        return pip.chainExecutor().execute(ctx);
    }


    @Override
    public Pipeline<IReactorExecutor, IChainExecutor> pipeline()
    {
        throw new RuntimeException("not supported");
    }

    @Override
    public void invokeInterestNodes(Collection<Object> nodes)
    {
        if (this.setted || CollectionUtils.isEmpty(nodes)) return;
        this.batchCollect(nodes);
        this.setted = true;
    }

    class AAA extends MonoOperator<Integer, String>
    {
        protected AAA(Mono<? extends Integer> source)
        {
            super(source);
        }

        @Override
        public void subscribe(CoreSubscriber<? super String> actual)
        {
        }
    }


    @Data
    class ExecutorWrapper
    {
        ActiveMethod method;
        IReactorExecutor executor;
    }

    // 每个node 都是 IReactorExecutor
    private void batchCollect(Collection<Object> nodes)
    {
        Flux<Object> flux = Flux.fromIterable(nodes);
        flux.doOnNext((o) ->
        {
            Method[] declaredMethods = o.getClass().getDeclaredMethods();
            Flux<Method> methodFlux = Flux.fromStream(Stream.of(declaredMethods));
            List<ExecutorWrapper> executors = methodFlux.flatMap(declaredMethod ->
            {
                ActiveMethod annotation = declaredMethod.getAnnotation(ActiveMethod.class);
                if (annotation == null)
                {
                    return Mono.empty();
                }
                if (!IReactorExecutor.class.isAssignableFrom(declaredMethod.getReturnType()))
                {
                    return Mono.empty();
                }
                try
                {
                    IReactorExecutor ret = (IReactorExecutor) declaredMethod.invoke(o);
                    ExecutorWrapper wp = new ExecutorWrapper();
                    wp.setExecutor(ret);
                    wp.setMethod(annotation);
                    return Mono.just(wp);
                } catch (Exception e)
                {
                    LOG.warn(Module.MANAGER, "获取executor失败:{}", e.getMessage());
                    return Mono.empty();
                }
            }).collectList().block();
            for (ExecutorWrapper executor : executors)
            {
                Pipeline<IReactorExecutor, IChainExecutor> pip = this.pipeline.get(executor.method.id());
                if (pip == null)
                {
                    pip = new DefaultPipeline<>();
                    this.pipeline.put(executor.method.id(), pip);
                }
                pip.addLast(executor.executor);
            }
        }).doOnError(e ->
        {
            // FIXME NOT RIGHT
            throw new RuntimeException(e);
        }).subscribe();
    }
}
