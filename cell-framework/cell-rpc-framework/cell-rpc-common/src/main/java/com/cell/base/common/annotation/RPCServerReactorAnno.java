package com.cell.base.common.annotation;

import com.cell.annotations.ForceOverride;
import com.cell.annotations.ReactorAnno;
import com.cell.cmd.IRPCServerCommand;
import com.cell.grpc.server.framework.constants.ProtocolConstants;
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
@ReactorAnno(type = ProtocolConstants.REACTOR_TYPE_RPC_GRPC_SERVER)
public @interface RPCServerReactorAnno
{
    @AliasFor(
            annotation = ReactorAnno.class,
            attribute = "withForce"
    )
    ForceOverride withForce() default @ForceOverride();

    @AliasFor(
            annotation = ReactorAnno.class,
            attribute = "cmds"
    )
    Class<? extends IRPCServerCommand>[] cmds() default {};

}
