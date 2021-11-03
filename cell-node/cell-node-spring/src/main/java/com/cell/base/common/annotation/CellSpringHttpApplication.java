package com.cell.base.common.annotation;

import com.cell.annotations.CellApplication;
import com.cell.extension.AbstractNodeExtension;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-03 15:07
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@CellApplication
@SpringBootApplication
public @interface CellSpringHttpApplication
{
    @AliasFor(annotation = CellApplication.class, attribute = "interestAnnotations")
    Class<? extends Annotation>[] scanInterestAnnotations() default {};

    Class<? extends AbstractNodeExtension>[] scanExcludeNodeExtensions() default {};

    @AliasFor(
            annotation = CellApplication.class,
            attribute = "excludeClasses"
    )
    Class<?>[] scanExcludeClasses() default {};

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "scanBasePackages"
    )
    String[] scanBasePackages() default {"com.cell"};
}
