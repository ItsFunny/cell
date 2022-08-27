package com.cell.component.cache.aop;

import com.cell.base.common.utils.StringUtils;
import com.cell.component.cache.service.CacheKey;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.CellContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = Integer.MAX_VALUE - 1)
public class CacheAop
{
    @Pointcut("@annotation(com.cell.component.cache.aop.CacheAnnotation)")
    public void cacheCheck() {}

    @Autowired(required = false)
    private ICacheService<String, String> cacheService;

    @Around("cacheCheck()")
    public Object doArgumentCheck(ProceedingJoinPoint joinPoint) throws Throwable
    {
        Object[] args = joinPoint.getArgs();
        if (args == null)
        {
            return joinPoint.proceed();
        }
        CellContext context = null;
        CacheKey key = null;
        for (Object arg : args)
        {
            if (arg instanceof CellContext)
            {
                context = (CellContext) arg;
            } else if (arg instanceof CacheKey)
            {
                key = (CacheKey) arg;
            }
        }
        if (context != null && context.disableCache())
        {
            return joinPoint.proceed();
        }
        if (key != null)
        {
            String keyInfo = key.cacheKey();
            if (StringUtils.isEmpty(keyInfo))
            {
                return joinPoint.proceed();
            }
            String valueInfo = this.cacheService.get(keyInfo);
            if (StringUtils.isNotEmpty(valueInfo))
            {
                Object ret = key.from(valueInfo);
                if (ret != null)
                {
                    return ret;
                }
            }
        }

        Object ret = null;
        ret = joinPoint.proceed();
        if (key != null && ret != null)
        {
            String serialize = key.serialize(ret);
            if (StringUtils.isNotEmpty(serialize))
            {
                String keyInfo = key.cacheKey();
                if (key.expire() > 0)
                {
                    this.cacheService.set(keyInfo, serialize, key.expire());
                } else
                {
                    this.cacheService.set(keyInfo, serialize);
                }
            }
//            if (ret instanceof CacheValue)
//            {
//                CacheValue value = (CacheValue) ret;
//                String valueInfo = value.serialize();
//                String keyInfo = key.cacheKey();
//                this.cacheService.set(keyInfo, valueInfo);
//            }
        }
        return ret;
    }

}
