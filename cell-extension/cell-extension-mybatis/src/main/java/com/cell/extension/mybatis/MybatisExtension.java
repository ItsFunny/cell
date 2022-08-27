package com.cell.extension.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.cell.base.common.models.Module;
import com.cell.extension.mybatis.config.MybatisConfig;
import com.cell.extension.mybatis.config.MysqlConfig;
import com.cell.extension.mybatis.constants.DBConstants;
import com.cell.extension.mybatis.dynamic.DynamicDataSourceRoute;
import com.cell.extension.mybatis.log.CellMybatisLog;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.sdk.log.LOG;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MybatisExtension extends AbstractSpringNodeExtension
{
    @Override
    public Options getOptions()
    {
        Options options = new Options();
        options.addOption(DBConstants.MYSQL_COMMAND, true, String.format("-%s assign mysql db", DBConstants.MYSQL_COMMAND));
        return options;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        MybatisConfig.getInstance().initConfig();
        CommandLine cmd = ctx.getCommandLine();
        if (cmd.hasOption(DBConstants.MYSQL_COMMAND))
        {
            MybatisConfig.getInstance().setMysqlKey(cmd.getOptionValue(DBConstants.MYSQL_COMMAND));
        }
        this.paginationInterceptor = new PaginationInterceptor();
        this.paginationInterceptor.setLimit(MybatisConfig.getInstance().getPageLimit());
    }


    @Bean
    @Primary
    public DataSource multipleDataSource()
    {
        DynamicDataSourceRoute dynamicDataSource = new DynamicDataSourceRoute();
        Map<Object, Object> targetDataSources = new HashMap<>();
        List<MysqlConfig> mysqlConfigs = MybatisConfig.getInstance().getMysqlList();
        DruidDataSource defaultDataSource = null;
        String defaultMysqlKey = MybatisConfig.getInstance().getMysqlKey();
        for (MysqlConfig mysqlConfig : mysqlConfigs)
        {
            DruidDataSource dataSource = createDruidDataSource(mysqlConfig);
            targetDataSources.put(mysqlConfig.getMysqlKey(), dataSource);
            if (mysqlConfig.getMysqlKey().equals(defaultMysqlKey))
            {
                defaultDataSource = dataSource;
            }
        }
        if (defaultDataSource != null)
        {
            LOG.info(Module.MYBATIS, "当前使用的数据源是{}:{}", defaultMysqlKey, defaultDataSource.getUrl());
        }
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource); // 程序默认数据源，这个要根据程序调用数据源频次，经常把常调用的数据源作为默认
        return dynamicDataSource;
    }

    public DruidDataSource createDruidDataSource(MysqlConfig mysqlConfig)
    {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(mysqlConfig.getDriverClass());
        dataSource.setUrl(mysqlConfig.getUrl());
        dataSource.setUsername(mysqlConfig.getUsername());
        dataSource.setPassword(mysqlConfig.getPassword());
        dataSource.setInitialSize(mysqlConfig.getInitialSize());
        dataSource.setMinIdle(mysqlConfig.getMinIdle());
        dataSource.setMaxActive(mysqlConfig.getMaxActive());
        dataSource.setTimeBetweenEvictionRunsMillis(mysqlConfig.getTimeBetweenEvictionRunsMillis());
        dataSource.setValidationQuery(mysqlConfig.getValidationQuery());
        dataSource.setTestWhileIdle(mysqlConfig.getTestWhileIdle());
        dataSource.setTestOnBorrow(mysqlConfig.getTestOnBorrow());
        dataSource.setTestOnReturn(mysqlConfig.getTestOnReturn());
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(DataSource dataSource, PaginationInterceptor pageInterceptor) throws IOException
    {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(CellMybatisLog.class);
        configuration.addInterceptor(pageInterceptor);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return sqlSessionFactoryBean;
    }

    private PaginationInterceptor paginationInterceptor;

    @Bean
    public PaginationInterceptor paginationInterceptor()
    {
        return this.paginationInterceptor;
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor()
    {
        return new OptimisticLockerInterceptor();
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
