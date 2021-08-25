package com.cell.annotation;

import com.cell.constants.Constants;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CellOrder
{
	int value() default  Constants.DEFAULT_ORDER;
}

