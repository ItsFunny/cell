package com.cell.grpc.client.base.framework.annotation;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-10-22 16:58
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ProxyAnno
{
    byte proxyId();

}
