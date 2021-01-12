package com.cell.annotations;

import com.cell.enums.FilterEnums;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joker
 * @When
 * @Description 用来标识filter的类型
 * @Detail
 * @date 创建时间：2021-01-12 09:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellFilter
{
    // 如果active 则会自动的注入到manager中
    boolean active() default true;

    // 属于哪个组,不同的filter会被不同的filterManager所接管
    int filterGroup() default 1;

}
