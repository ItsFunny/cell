package com.cell.annotation;

import io.grpc.ServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-10-26 18:35
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
@Bean
public @interface GRPCService
{
    Class<? extends ServerInterceptor>[] interceptors() default {};
    String[] interceptorNames() default {};
    boolean sortInterceptors() default false;
}
