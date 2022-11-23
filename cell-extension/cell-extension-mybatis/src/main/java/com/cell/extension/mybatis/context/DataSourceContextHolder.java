package com.cell.extension.mybatis.context;

import com.cell.base.common.models.Module;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.extension.mybatis.config.MybatisConfig;
import com.cell.sdk.log.LOG;

import java.util.ArrayList;
import java.util.List;

public class DataSourceContextHolder
{
    private static final ThreadLocal<List<String>> contextHolder = new ThreadLocal<>(); //实际上就是开启多个线程，每个线程进行初始化一个数据源

    public static void setDbType(String dbType)
    {
        if (MybatisConfig.getInstance().getMysqlConfig(dbType) == null)
        {
            throw new RuntimeException("未知的数据库：" + dbType);
        }
        List<String> dbTypes = contextHolder.get();
        if (dbTypes==null){
            dbTypes=new ArrayList<>();
            contextHolder.set(dbTypes);
        }
        dbTypes.add(dbType);
        LOG.info(Module.DYNAMIC_DAO, "切换数据源： {},当前登记的数据源:{}, threadId: {}", dbType,dbTypes, Thread.currentThread().getName());
    }

    public static String getDbType()
    {
        List<String> dbTypes = contextHolder.get();
        if (CollectionUtils.isEmpty(dbTypes)){
            return null;
        }
        return dbTypes.get(dbTypes.size()-1);
    }

    public static void clearDbType()
    {
        List<String> dbTypes = contextHolder.get();
        if (CollectionUtils.isEmpty(dbTypes)){
            return;
        }
        String dbType = dbTypes.remove(dbTypes.size() - 1);
        if (CollectionUtils.isEmpty(dbTypes)){
            contextHolder.remove();
        }
        LOG.info(Module.DYNAMIC_DAO, "清除当前方法的数据源： {},当前登记的数据源:{}, threadId = {}", dbType,dbTypes, Thread.currentThread().getName());
    }
}
