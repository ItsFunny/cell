package com.cell.base.core.annotations;


import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.reactor.ICommandReactor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-08-30 10:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command
{
    boolean forceOverRide() default false;

    String protocol();

//    short commandId();

    Class<?> buzzClz() default Void.class;

    Class<? extends ICommandReactor> reactor();

    // TODO ,删除
    Class<? extends ICommand> couple() default ICommand.class;

    boolean async() default false;

    long timeOut() default 30000;

    // exception return
    String fallBackMethod() default "";
}
