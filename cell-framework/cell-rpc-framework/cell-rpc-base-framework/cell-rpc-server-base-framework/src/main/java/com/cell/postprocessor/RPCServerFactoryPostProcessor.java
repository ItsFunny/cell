package com.cell.postprocessor;

import com.cell.annotation.RPCServerCmdAnno;

import java.lang.annotation.Annotation;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-09 06:02
 */
public class RPCServerFactoryPostProcessor extends AbstractReactorFactoryPostProcessor
{
    @Override
    protected Class<? extends Annotation> getTargetAnnotationClasses()
    {
        return RPCServerCmdAnno.class;
    }
}
