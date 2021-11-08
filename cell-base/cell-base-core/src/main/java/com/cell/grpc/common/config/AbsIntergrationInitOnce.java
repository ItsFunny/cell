package com.cell.grpc.common.config;

import com.cell.context.InitCTX;
import com.cell.enums.ConfigurableEnums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-17 22:17
 */
public abstract class AbsIntergrationInitOnce extends AbstractInitOnce implements  IIntegrationConfigurable
{
    @Override
    protected void onInit(InitCTX ctx)
    {
        this.config();
    }

    @Override
    public ConfigurableEnums configurable()
    {
        return ConfigurableEnums.INTEGRATED;
    }
}
