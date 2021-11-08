package com.cell.sdk.log.bridge;

import ch.qos.logback.classic.LoggerContext;
import io.netty.util.internal.ConcurrentSet;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-27 22:47
 */
public class DefaultCellLoggerContext extends LoggerContext
{
    private ConcurrentSet<String> set = new ConcurrentSet<>();

    public boolean needDecorate(String name)
    {
        return !this.set.contains(name);
    }

    public void tag(String name)
    {
        this.set.add(name);
    }

}
