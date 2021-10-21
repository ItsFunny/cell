package com.cell.annotation;

import com.cell.annotations.ReactorAnno;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joker
 * @When
 * @Description 相当于rpcClient
 * @Detail
 * @date 创建时间：2021-10-09 05:21
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ReactorAnno
public @interface RPCServerReactorAnno
{
    @AliasFor(annotation = ReactorAnno.class, attribute = "group")
    String group() default "";
}
