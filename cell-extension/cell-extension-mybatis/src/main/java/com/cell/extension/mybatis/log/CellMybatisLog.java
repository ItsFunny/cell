package com.cell.extension.mybatis.log;

import com.cell.base.common.models.Module;
import com.cell.sdk.log.LOG;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;

public class CellMybatisLog extends Log4j2Impl
{
    public CellMybatisLog(String clazz)
    {
        super(clazz);
    }

    private static boolean debugEnable = true;

    public static void setDebugEnable(boolean b)
    {
        debugEnable = b;
    }

    @Override
    public boolean isDebugEnabled()
    {
        return debugEnable;
    }

    @Override
    public boolean isTraceEnabled()
    {
        return false;
    }

    @Override
    public void error(String s, Throwable throwable)
    {
        LOG.error(Module.MYBATIS, throwable, "{}", s);
    }

    @Override
    public void error(String s)
    {
        LOG.erroring(Module.MYBATIS, s);
    }

    @Override
    public void debug(String s)
    {
        LOG.info(Module.MYBATIS, s);
    }

    @Override
    public void trace(String s)
    {
    }

    @Override
    public void warn(String s)
    {
        LOG.warn(Module.MYBATIS, s);
    }
}
