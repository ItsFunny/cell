package com.cell.annotations;

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
public @interface ReactorAnno
{
    ForceOverride withForce() default @ForceOverride();

    String group() default "/cell";
}
