package com.cell.base.core.annotations;

import java.lang.annotation.*;

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
    Class<? extends Annotation>[] interestAnnotations() default {};
}
