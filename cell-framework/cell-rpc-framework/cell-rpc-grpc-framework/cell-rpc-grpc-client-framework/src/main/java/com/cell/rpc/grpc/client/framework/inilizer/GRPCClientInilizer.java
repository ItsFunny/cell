package com.cell.rpc.grpc.client.framework.inilizer;

import com.cell.initializer.CellSpringInitializer;
import com.cell.rpc.grpc.client.framework.annotation.GRPCClientRequestAnno;

import java.lang.annotation.Annotation;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 05:09
 */
public class GRPCClientInilizer implements CellSpringInitializer
{
    @Override
    public Class<? extends Annotation>[] getInterestAnnotation()
    {
        return new Class[]{GRPCClientRequestAnno.class};
    }
}
