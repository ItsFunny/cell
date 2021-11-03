package com.cell.manager;

import com.cell.http.framework.annotation.ActiveMethod;
import com.cell.exception.ManagerException;
import com.cell.exceptions.ProgramaException;
import com.cell.executor.IChainExecutor;
import com.cell.executor.IReactorExecutor;
import com.cell.pipeline.DefaultPipeline;
import com.cell.pipeline.Pipeline;
import com.cell.utils.CollectionUtils;
import com.cell.utils.ReflectionUtils;
import lombok.Data;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-20 12:32
 */
public abstract class AbstractAutoSelectReflectManager<V> implements IReflectManager<IReactorExecutor<V>, IChainExecutor<V>, V>
{
    private boolean setted;
    protected Map<String, Pipeline<IReactorExecutor<V>, IChainExecutor<V>>> pipeline = new HashMap<>();

    public Mono<Void> execute(String method, V v)
    {
        Pipeline<IReactorExecutor<V>, IChainExecutor<V>> pip = this.pipeline.get(method);
        if (pip == null)
        {
            throw new ManagerException("asd");
        }
        return pip.chainExecutor().execute(v);
    }


    @Override
    public Pipeline<IReactorExecutor<V>, IChainExecutor<V>> pipeline()
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

    static abstract class SuperClass<T>
    {
        public Class<T> getGenericClass()
        {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
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
                    System.out.println(declaredMethod.getReturnType());
                    return Mono.empty();
                }
                try
                {
                    Type genericReturnType = declaredMethod.getGenericReturnType();
                    if (!(genericReturnType instanceof ParameterizedType))
                    {
                        genericReturnType = ReflectionUtils.getClzGenesicInterfaceTill(declaredMethod.getReturnType(), IReactorExecutor.class);
                        if (genericReturnType == null)
                        {
                            throw new ProgramaException("asd");
                        }
                    }
                    ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                    Type[] types = parameterizedType.getActualTypeArguments();
                    if (!ReflectionUtils.matchClazAllGenesic(this.getClass(), types[0]))
                    {
                        throw new ProgramaException("node 错误,node 必须为相同的泛型");
                    }
                    IReactorExecutor<V> ret = (IReactorExecutor<V>) declaredMethod.invoke(o);

                    ExecutorWrapper wp = new ExecutorWrapper();
                    wp.setExecutor(ret);
                    wp.setMethod(annotation);
                    return Mono.just(wp);
                } catch (Exception e)
                {
                    return Mono.empty();
                }
            }).collectList().block();
            if (CollectionUtils.isEmpty(executors)) return;
            for (ExecutorWrapper executor : executors)
            {
                Pipeline<IReactorExecutor<V>, IChainExecutor<V>> pip = this.pipeline.get(executor.method.id());
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
//            throw new ManagerException(e);
        }).subscribe();
    }
}
