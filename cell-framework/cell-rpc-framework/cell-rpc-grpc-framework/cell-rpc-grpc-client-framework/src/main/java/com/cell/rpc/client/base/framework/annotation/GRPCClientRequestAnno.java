package com.cell.rpc.client.base.framework.annotation;

import com.cell.serialize.ISerializable;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021/10/29 13:25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface GRPCClientRequestAnno
{
    String protocol();

    boolean async();

    Class<? extends ISerializable> responseType();

    // seconds: 0 means sync
    byte timeOut() default 0;
}
