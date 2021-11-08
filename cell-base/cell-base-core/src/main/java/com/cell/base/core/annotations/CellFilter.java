package com.cell.base.core.annotations;


import com.cell.base.common.annotation.CellAutoAble;

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
@CellAutoAble
public @interface CellFilter
{

}
