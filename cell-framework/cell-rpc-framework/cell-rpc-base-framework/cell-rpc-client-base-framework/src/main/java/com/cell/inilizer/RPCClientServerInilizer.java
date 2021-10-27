package com.cell.inilizer;

import com.cell.annotation.RPCClient;
import com.cell.initializer.CellSpringInitializer;

import java.lang.annotation.Annotation;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 04:44
 */
public class RPCClientServerInilizer implements CellSpringInitializer
{

    @Override
    public Class<? extends Annotation>[] getInterestAnnotation()
    {
        return new Class[]{RPCClient.class};
    }
}
