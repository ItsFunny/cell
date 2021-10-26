package com.cell.annotation;

import com.cell.anno.DispatcherAnno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-10-26 17:34
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DispatcherAnno
public @interface RPCDispatcherAnno
{
}
