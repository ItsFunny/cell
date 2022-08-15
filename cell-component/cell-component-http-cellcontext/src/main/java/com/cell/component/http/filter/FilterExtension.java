package com.cell.component.http.filter;

import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class FilterExtension extends AbstractSpringNodeExtension
{
    @Bean
    public ContextPrepareFilter filter()
    {
        ContextPrepareFilter ret = new ContextPrepareFilter();
        return ret;
    }
//    @Bean
//    public FilterRegistrationBean requestBodyFilterRegistration()
//    {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        //添加过滤器
//        registration.setFilter(new ContextPrepareFilter());
//        //设置过滤路径，/*所有路径
//        registration.addUrlPatterns("/*");
//        registration.setName("context-prepare");
//        //设置优先级
////        registration.setOrder(-10000);
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
