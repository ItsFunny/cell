package com.cell.inilizer;

import com.cell.http.framework.annotation.RPCServerReactorAnno;
import com.cell.http.framework.annotation.RPCService;
import com.cell.initializer.CellSpringInitializer;
import com.cell.rpc.server.base.annotation.RPCServerCmdAnno;

import java.lang.annotation.Annotation;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention: // 如果后续需要兼容其他框架,需要将这个类单独的抽离到rpc-spring-framekwork module中
 * @Date 创建时间：2021-10-09 06:06
 */
public class RPCServerInilizer implements CellSpringInitializer
{


    @Override
    public Class<? extends Annotation>[] getInterestAnnotation()
    {
        return new Class[]{RPCService.class, RPCServerCmdAnno.class, RPCServerReactorAnno.class};
    }
}
