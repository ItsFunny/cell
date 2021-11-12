package com.cell.node.spring.internal;

import com.cell.base.core.annotations.CellOrder;
import com.cell.base.common.constants.OrderConstants;
import com.cell.base.common.models.Module;
import com.cell.sdk.configuration.Configuration;
import com.cell.sdk.configuration.exception.ConfigurationException;
import com.cell.sdk.log.LOG;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-05 07:44
 */
@CellOrder(OrderConstants.MIN_ORDER - 998)
public class ConfigurationExtension extends AbstractSpringNodeExtension
{
    private Options options;
    private String DEFAULT_PATH = "/Users/joker/Java/cell";

    public ConfigurationExtension()
    {
        options = new Options();
        options.addOption("type", true, "-type 配置类型,");
        options.addOption("path", true, "-path 根路径");
    }

    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
        CommandLine cmd = ctx.getCommandLine();
        String type = null;
        if (cmd.hasOption("type"))
        {
            type = cmd.getOptionValue("type");
        }
        String path = DEFAULT_PATH;
        if (cmd.hasOption("path"))
        {
            path = cmd.getOptionValue("path");
        }
        try
        {
            LOG.info(Module.CONFIGURATION, "配置文件初始化，path =[{}]  type=[{}]", path, type);
            Configuration.getDefault().initialize(path, type);
        } catch (Throwable e)
        {
            tryLocal(path, type);
        }
    }

    private void tryLocal(String originPath, String type)
    {
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + currentRelativePath.getFileSystem().getSeparator() + "config";
        try
        {
            Configuration.getDefault().initialize(path, type);
        } catch (ConfigurationException e)
        {
            LOG.error(Module.CONFIGURATION, e, "初始化配置失败，path =[{}]  type=[{}],localPath=[{}]", originPath, type, path);
        }
    }

    @Override
    public void onStart(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void onClose(INodeContext ctx) throws Exception
    {

    }

    @Override
    public Options getOptions()
    {
        return this.options;
    }
}