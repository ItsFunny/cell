package com.cell.sdk.log.impl;

// bad idea
public class FilterManager implements ILogFilter
{
    public static ILogFilter logFilter = null;

    public static ILogFilter getLogFilter()
    {
        return logFilter;
    }

    public static void unsafeSetFilter(ILogFilter filter)
    {
        logFilter = filter;
    }
}
