package com.cell.annotations;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.protocol.ICommand;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-08-29 07:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActivePlugin
public @interface ReactorAnno
{
    ForceOverride withForce() default @ForceOverride();

    Class<? extends ICommand>[] cmds() default {};

    String group() default "";

    byte type() default ProtocolConstants.REACTOR_TYPE_HTTP;
}
