package com.cell.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-08-31 22:19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellScan
{
}
