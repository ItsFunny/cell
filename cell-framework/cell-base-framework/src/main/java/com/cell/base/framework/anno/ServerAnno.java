package com.cell.base.framework.anno;

import com.cell.base.core.annotations.ActivePlugin;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-10-26 17:31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActivePlugin
public @interface ServerAnno
{
}
