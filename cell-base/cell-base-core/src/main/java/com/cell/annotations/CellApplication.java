package com.cell.annotations;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-09-03 14:40
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellApplication
{
    Class<? extends Annotation>[] interestAnnotations() default {};

    Class<?>[] excludeClasses() default {};
}
