package com.cell.grpc.client.base.framework.inilizer;

import com.cell.initializer.CellSpringInitializer;
import com.cell.grpc.client.base.framework.annotation.GRPCClientRequestAnno;

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
