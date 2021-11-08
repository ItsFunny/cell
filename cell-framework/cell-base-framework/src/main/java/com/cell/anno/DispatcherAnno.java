package com.cell.anno;

import com.cell.base.core.annotations.ActivePlugin;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-10-26 17:32
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActivePlugin
public @interface DispatcherAnno
{
}
