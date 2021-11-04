package com.cell.rpc.client.base.annotation;

import java.lang.annotation.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 15:09
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RPCClientCmdAnno
{
    String protocol();

    Class<?> responseType();
}
