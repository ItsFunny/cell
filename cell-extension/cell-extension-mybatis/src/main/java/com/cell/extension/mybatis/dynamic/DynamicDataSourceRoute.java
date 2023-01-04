package com.cell.extension.mybatis.dynamic;


import com.cell.base.common.models.Module;
import com.cell.base.common.utils.StringUtils;
import com.cell.extension.mybatis.constants.DBConstants;
import com.cell.extension.mybatis.context.DataSourceContextHolder;
import com.cell.sdk.log.LOG;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSourceRoute extends AbstractRoutingDataSource
{
    @Override
    protected Object determineCurrentLookupKey()
    {
        String datasource = DataSourceContextHolder.getDbType();
        if (!StringUtils.isEmpty(datasource))
        {
            LOG.info(Module.DYNAMIC_DAO, "当前使用的数据源是： {}, threadId: {}", datasource, Thread.currentThread().getName());
        }
        return datasource;
    }

}
