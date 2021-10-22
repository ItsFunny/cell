package com.cell.annotation;

import com.cell.annotations.ActivePlugin;
import com.cell.annotations.ForceOverride;
import com.cell.cmd.IRPCServerCommand;

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
@ActivePlugin
public @interface RPCServerReactorAnno
{
    ForceOverride withForce() default @ForceOverride();

    Class<? extends IRPCServerCommand>[] cmds() default {};

    String group() default "";
}
