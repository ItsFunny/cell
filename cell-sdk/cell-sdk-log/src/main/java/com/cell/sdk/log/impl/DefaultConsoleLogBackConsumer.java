package com.cell.sdk.log.impl;

import com.cell.base.common.context.InitCTX;
import com.cell.sdk.log.factory.DefaultSlf4jLoggerFactory;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-02-01 21:56
 */
public class DefaultConsoleLogBackConsumer extends AbstractLogBackLogEventConsumer
{
    @Override
    protected void onInit(InitCTX ctx)
    {
        this.logger = DefaultSlf4jLoggerFactory.getInstance().getConsoleLogger();
    }
}
