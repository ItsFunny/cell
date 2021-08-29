package com.cell.annotations;

import com.cell.constants.ManagerConstants;
import com.cell.enums.EnumLifeCycle;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-08-28 13:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@CellOrder(value = 0)
public @interface ManagerNode
{
    String name() default ManagerConstants.defaultManagerNodeName;

    boolean override() default false;

    String group() default ManagerConstants.defaultManagerName;

    EnumLifeCycle lifeCycle() default EnumLifeCycle.ONCE;

    @AliasFor(
            annotation = CellOrder.class,
            attribute = "value"
    )
    int orderValue() default 0;
}
