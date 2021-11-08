package com.cell.sdk.log.impl;

import com.cell.base.common.context.InitCTX;
import com.cell.base.common.models.ModuleInterface;
import com.cell.sdk.log.ILogConsumer;
import com.cell.sdk.log.LogLevel;
import com.cell.sdk.log.LogTypeEnums;
import com.cell.sdk.log.internal.LogCache;
import com.cell.sdk.log.internal.LogPolicy;

import java.io.Serializable;
import java.util.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-02-01 22:42
 */
public class DefaultCellLogger extends AbstractCellLogger
{
    private static final LogCache LOG_CACHE = new LogCache();

    static
    {

    }

    @Override
    protected Set<ILogConsumer> getLogConsumers(ModuleInterface module, LogTypeEnums logType, LogLevel logLevel)
    {
        return LOG_CACHE.getLogReceivers(module, logLevel, logType.getCode());
    }

    @Override
    public void registerConsumers(ILogConsumer consumer)
    {
        LOG_CACHE.registerConsumer(consumer.getClass(), consumer);
    }

    @Override
    public Serializable getType()
    {
        return null;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.setLogLevel(LogLevel.INFO);
        init();
    }

    private void init()
    {
        InitCTX ctx = new InitCTX();
        DefaultFileLogBackEventConsumer fileLogBackEventConsumer = new DefaultFileLogBackEventConsumer();
        fileLogBackEventConsumer.initOnce(ctx);
        DefaultConsoleLogBackConsumer consoleLogBackConsumer = new DefaultConsoleLogBackConsumer();
        consoleLogBackConsumer.initOnce(ctx);
        this.registerConsumers(fileLogBackEventConsumer);
        this.registerConsumers(consoleLogBackConsumer);

        Map<Class<? extends ILogConsumer>, Boolean> recevierPolicies = new HashMap<>();
        recevierPolicies.put(DefaultConsoleLogBackConsumer.class, true);
        recevierPolicies.put(DefaultFileLogBackEventConsumer.class, true);
        LogPolicy logPolicy = new LogPolicy(null, null, null, null, recevierPolicies, 0);
        List<LogPolicy> logPolicies = new ArrayList<>();
        logPolicies.add(logPolicy);
        LOG_CACHE.setPolicies(logPolicies);
        LOG_CACHE.start();
    }

    public boolean haveReceiver(ModuleInterface module, LogLevel logLevel, Long logType)
    {
        return LOG_CACHE.getLogReceivers(module, logLevel, logType).size() != 0;
    }
}
