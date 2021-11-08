package com.cell.base.core.annotations;

import com.cell.base.core.enums.EnumLifeCycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-08-29 08:28
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LifeCycle
{
    EnumLifeCycle lifeCycle() default EnumLifeCycle.ONCE;
}
