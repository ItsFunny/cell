package com.cell.base.common.annotation;

import java.lang.annotation.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-24 07:50
 */
@Target(value = ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CellAutoAble
{
    // 如果active 则会自动的注入到manager中
    boolean active() default true;

    // 属于哪个组,不同的filter会被不同的filterManager所接管
    int group() default 1;

}
