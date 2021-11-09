package com.cell.http.framework.inilizer;

import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.node.spring.initializer.CellSpringInitializer;

import java.lang.annotation.Annotation;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-09 22:33
 */
public class HttpInilizer implements CellSpringInitializer
{
    @Override
    public Class<? extends Annotation>[] getInterestAnnotation()
    {
        return new Class[]{HttpCmdAnno.class};
    }
}
