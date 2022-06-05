package com.cell.extension.mybatis;

import com.cell.extension.mybatis.config.MybatisConfig;
import com.cell.extension.mybatis.constants.DBConstants;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class ConfigExtension extends AbstractSpringNodeExtension
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
