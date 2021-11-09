package com.cell.http.framework.postprocessor;

import com.cell.base.core.annotations.LifeCycle;
import com.cell.base.core.enums.EnumLifeCycle;
import com.cell.base.framework.postprocessor.AbstractReactorFactoryPostProcessor;
import com.cell.http.framework.annotation.HttpCmdAnno;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:53
 */
@LifeCycle(lifeCycle = EnumLifeCycle.ONCE)
public class HttpReactorFactoryPostProcessor extends AbstractReactorFactoryPostProcessor
{
    @Override
    protected List<Class<? extends Annotation>> getTargetAnnotationClasses()
    {
        return Arrays.asList(HttpCmdAnno.class);
    }
}
