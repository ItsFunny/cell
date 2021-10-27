package com.cell.interceptor;

import com.google.common.collect.ImmutableList;
import io.grpc.ClientInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 18:55
 */
public class GlobalClientInterceptorRegistry
{
    private final ApplicationContext applicationContext;

    private ImmutableList<ClientInterceptor> sortedClientInterceptors;

    public GlobalClientInterceptorRegistry(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    public ImmutableList<ClientInterceptor> getClientInterceptors()
    {
        if (this.sortedClientInterceptors == null)
        {
            this.sortedClientInterceptors = ImmutableList.copyOf(initClientInterceptors());
        }
        return this.sortedClientInterceptors;
    }

    protected List<ClientInterceptor> initClientInterceptors()
    {
        final List<ClientInterceptor> interceptors = new ArrayList<>();
        for (final GlobalClientInterceptorConfigurer configurer : this.applicationContext
                .getBeansOfType(GlobalClientInterceptorConfigurer.class).values())
        {
            configurer.configureClientInterceptors(interceptors);
        }
        sortInterceptors(interceptors);
        return interceptors;
    }

    public void sortInterceptors(final List<? extends ClientInterceptor> interceptors)
    {
        interceptors.sort(AnnotationAwareOrderComparator.INSTANCE);
    }
}
