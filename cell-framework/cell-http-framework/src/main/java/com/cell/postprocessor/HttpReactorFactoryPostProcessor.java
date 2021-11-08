package com.cell.postprocessor;

import com.cell.annotations.LifeCycle;
import com.cell.enums.EnumLifeCycle;
import com.cell.rpc.grpc.client.framework.annotation.HttpCmdAnno;

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
