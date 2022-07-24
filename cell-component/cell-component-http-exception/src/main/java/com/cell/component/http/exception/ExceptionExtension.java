package com.cell.component.http.exception;

import com.cell.component.http.exception.handler.DefaultExceptionHandler;
import com.cell.component.http.exception.handler.IExceptionHandler;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class ExceptionExtension extends AbstractSpringNodeExtension
{
    private IExceptionHandler exceptionHandler;

    @ConditionalOnMissingBean
    @Bean
    public IExceptionHandler handler()
    {
        return this.exceptionHandler;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        this.exceptionHandler = new DefaultExceptionHandler();
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {
    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
