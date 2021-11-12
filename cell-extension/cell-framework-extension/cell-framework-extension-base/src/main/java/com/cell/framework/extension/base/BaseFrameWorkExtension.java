package com.cell.framework.extension.base;

import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.apache.commons.cli.Options;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-12 16:24
 */
public class BaseFrameWorkExtension extends AbstractSpringNodeExtension
{
    public static final String CONFIG_OPEN_ADDRESS = "openAddress";
    public static final String CONFIG_OPEN_PORT = "openPort";

    @Override
    public Options getOptions()
    {
        Options options = new Options();
        options.addOption("openAddress", true, "公网可以访问的地址,ip或者域名");
        options.addOption("openPort", true, "公网可以访问的端口号");
        return options;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {

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
