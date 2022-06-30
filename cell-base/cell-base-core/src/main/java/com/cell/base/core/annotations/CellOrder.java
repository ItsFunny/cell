package com.cell.base.core.annotations;



import com.cell.base.core.constants.Constants;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
// 升序排序, 越小的越在前面
public @interface CellOrder
{
    int value() default Constants.DEFAULT_ORDER;
}

