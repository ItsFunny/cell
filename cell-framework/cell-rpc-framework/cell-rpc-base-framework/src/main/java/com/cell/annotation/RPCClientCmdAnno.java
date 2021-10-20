package com.cell.annotation;

import com.cell.cmd.server.IRPCServerInvoker;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-10-20 21:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RPCClientCmdAnno
{
    // 属于哪个server
    Class<? extends IRPCServerInvoker> server();

}
