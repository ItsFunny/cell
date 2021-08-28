package com.cell.annotation;

import com.cell.constants.ManagerConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-08-28 13:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagerNode
{
    String name() default ManagerConstants.defaultManagerNodeName;

    boolean override() default false;

    String group() default ManagerConstants.defaultManagerName;
}
