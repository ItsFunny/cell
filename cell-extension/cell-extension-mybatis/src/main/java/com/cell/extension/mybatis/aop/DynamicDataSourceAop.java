package com.cell.extension.mybatis.aop;

import com.cell.base.common.utils.StringUtils;
import com.cell.extension.mybatis.annotation.DynamicDataSource;
import com.cell.extension.mybatis.context.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class DynamicDataSourceAop
{

    @Pointcut("@annotation(com.cell.extension.mybatis.annotation.DynamicDataSource)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object clearDynamicDataSource(ProceedingJoinPoint pjp) throws Throwable
    {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        DynamicDataSource annotation = method.getAnnotation(DynamicDataSource.class);
        if (StringUtils.isNotEmpty(annotation.dbType()))
        {
            DataSourceContextHolder.setDbType(annotation.dbType());
        }
        Object result = null;
        try
        {
            result = pjp.proceed();
        } finally
        {
            DataSourceContextHolder.clearDbType();
        }
        return result;
    }
}
