package com.cell.base.common.annotation;

import io.grpc.ClientInterceptor;

import java.lang.annotation.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 10:42
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@RPCClient
public @interface GRPCClient
{
    String value();

    Class<? extends ClientInterceptor>[] interceptors() default {};

    String[] interceptorNames() default {};

    boolean sortInterceptors() default false;
}
