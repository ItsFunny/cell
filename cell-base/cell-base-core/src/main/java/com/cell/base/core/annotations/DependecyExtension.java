package com.cell.base.core.annotations;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-09-08 22:33
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// Configuration
@Inherited
@Deprecated
public @interface DependecyExtension
{
}

