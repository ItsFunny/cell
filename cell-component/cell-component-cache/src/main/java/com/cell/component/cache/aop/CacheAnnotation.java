package com.cell.component.cache.aop;

import com.cell.component.cache.constants.CacheConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheAnnotation
{
    int operation() default CacheConstants.CACHE_OPERATION_CACHE;
}
