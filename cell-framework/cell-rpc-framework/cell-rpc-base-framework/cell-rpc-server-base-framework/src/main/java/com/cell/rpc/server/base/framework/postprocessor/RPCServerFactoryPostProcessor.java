package com.cell.rpc.server.base.framework.postprocessor;


import com.cell.base.framework.postprocessor.AbstractReactorFactoryPostProcessor;
import com.cell.rpc.server.base.framework.annotation.RPCServerCmdAnno;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

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
    protected List<Class<? extends Annotation>> getTargetAnnotationClasses()
    {
//        return Arrays.asList(RPCServerAnno.class, RPCDispatcherAnno.class, RPCServerCmdAnno.class);
        return Arrays.asList(RPCServerCmdAnno.class);
    }
}
