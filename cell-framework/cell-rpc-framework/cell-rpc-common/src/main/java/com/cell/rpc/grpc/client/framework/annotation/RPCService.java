package com.cell.rpc.grpc.client.framework.annotation;

import com.cell.base.core.annotations.ActivePlugin;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-10-25 12:58
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ActivePlugin
public @interface RPCService
{
}
