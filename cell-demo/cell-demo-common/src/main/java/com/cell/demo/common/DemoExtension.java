package com.cell.demo.common;

import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.annotations.Plugin;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.apache.commons.cli.Options;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-12 10:08
 */
public class DemoExtension extends AbstractSpringNodeExtension
{
    private DemoServiceImpl demoService;

    @Override
    public Options getOptions()
    {
        Options options = new Options();
        options.addOption("rpcName", true, "测试使用的,rpc 名称");
        return options;
    }

    @Plugin
    public DemoServiceImpl demoService()
    {
        return this.demoService;
    }

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        String rpcName = ctx.getCommandLine().getOptionValue("rpcName");
        if (StringUtils.isEmpty(rpcName))
        {
            throw new RuntimeException("asd");
        }
        this.demoService = new DemoServiceImpl(rpcName);
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
