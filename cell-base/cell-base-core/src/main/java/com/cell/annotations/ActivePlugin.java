package com.cell.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// service
@Inherited
public @interface ActivePlugin
{
    String name() default "";
}
