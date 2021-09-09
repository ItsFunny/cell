package com.cell.internal;

import com.cell.Configuration;
import com.cell.annotations.CellOrder;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.exception.ConfigurationException;
import com.cell.extension.AbstractSpringNodeExtension;
import com.cell.log.LOG;
import com.cell.models.Module;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.io.File;
import java.nio.file.Files;
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
@CellOrder(OrderConstants.EXTESNION_MIN_NUM_ORDER - 998)
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
    public void init(INodeContext ctx) throws Exception
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
    public void start(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void ready(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void close(INodeContext ctx) throws Exception
    {

    }

    @Override
    public Options getOptions()
    {
        return this.options;
    }
}
