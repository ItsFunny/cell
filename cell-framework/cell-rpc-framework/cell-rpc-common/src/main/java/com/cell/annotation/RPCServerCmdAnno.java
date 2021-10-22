package com.cell.annotation;

import com.cell.annotations.Command;
import com.cell.reactor.ICommandReactor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description server command
 * @Detail
 * @date 创建时间：2021-10-09 06:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
// TODO
@Command(commandId = 1, reactor = ICommandReactor.class)
public @interface RPCServerCmdAnno
{
    //
    float version() default 1.0f;
    String func();
    @AliasFor(annotation = Command.class, attribute = "buzzClz")
    Class<?> buzzClz() default Void.class;
}