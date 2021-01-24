package com.cell.log_origin.impl;

import com.cell.config.AbstractInitOnce;
import com.cell.enums.FilterEnums;
import com.cell.exceptions.ConfigException;
import com.cell.filters.DefaultStatefulFilterManager;
import com.cell.log_origin.ILogPolicy;
import com.cell.log_origin.LogEvent;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:08
 */
public class DefaultLogPolicy extends AbstractInitOnce implements ILogPolicy
{

    @Override
    public void handle(LogEvent logEvent)
    {
        // filters 过滤event
        DefaultStatefulFilterManager.getInstance().filter(logEvent, () ->
        {
            logEvent.setFiltered(true);
            return FilterEnums.BREAK;
        });
        if (logEvent.isFiltered())
        {
            return;
        }
        // encoder 进行封装

    }

    @Override
    protected void init() throws ConfigException
    {
        // 注册filters
    }
}
