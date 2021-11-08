package com.cell.base.core.log.impl;

import com.cell.base.core.log.factory.DefaultSlf4jLoggerFactory;
import com.cell.base.core.context.InitCTX;

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
