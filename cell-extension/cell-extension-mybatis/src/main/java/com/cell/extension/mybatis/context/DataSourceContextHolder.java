package com.cell.extension.mybatis.context;

import com.cell.base.common.models.Module;
import com.cell.extension.mybatis.config.MybatisConfig;
import com.cell.sdk.log.LOG;

public class DataSourceContextHolder
{
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>(); //实际上就是开启多个线程，每个线程进行初始化一个数据源

    /**
     * 设置数据源，必须在事务开启前调用
     *
     * @param dbType
     */
    public static void setDbType(String dbType)
    {
        if (MybatisConfig.getInstance().getMysqlConfig(dbType) == null)
        {
            throw new RuntimeException("未知的数据库：" + dbType);
        }
        contextHolder.set(dbType);
        LOG.info(Module.DYNAMIC_DAO, "切换数据源： %s, threadId = %s", dbType, Thread.currentThread().getName());
    }

    public static String getDbType()
    {
        return contextHolder.get();
    }

    public static void clearDbType()
    {
        String dbType = contextHolder.get();
        contextHolder.remove();
        LOG.info(Module.DYNAMIC_DAO, "清除当前方法的数据源： %s, threadId = %s", dbType, Thread.currentThread().getName());
    }
}
