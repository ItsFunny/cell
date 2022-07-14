package com.cell.http.framework.filter;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.core.annotations.CellOrder;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

public class FilterExtension extends AbstractSpringNodeExtension
{
//    @Bean
//    public FilterRegistrationBean requestBodyFilterRegistration()
//    {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        //添加过滤器
//        registration.setFilter(new ContextPrepareFilter());
//        //设置过滤路径，/*所有路径
//        registration.addUrlPatterns("/**");
//        registration.setName("context-prepare");
//        //设置优先级
//        registration.setOrder(1);
//        return registration;
//    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {

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
