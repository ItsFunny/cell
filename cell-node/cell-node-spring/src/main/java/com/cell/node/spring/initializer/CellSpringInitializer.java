package com.cell.node.spring.initializer;

import java.lang.annotation.Annotation;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-16 19:08
 */
public interface CellSpringInitializer
{

    Class<? extends Annotation>[] getInterestAnnotation();
}
