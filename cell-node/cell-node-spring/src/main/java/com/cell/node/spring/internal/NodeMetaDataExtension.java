package com.cell.node.spring.internal;

import com.cell.annotations.CellOrder;
import com.cell.constants.CommandLineConstants;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.node.spring.context.SpringNodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import com.cell.utils.ClassUtil;
import com.cell.utils.StringUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-05 07:56
 */
@CellOrder(OrderConstants.MIN_ORDER - 997)
public class NodeMetaDataExtension extends AbstractSpringNodeExtension
{

    @Override
    public Options getOptions()
    {
        Options options = new Options();
        options.addOption("meta", true, "-meta metadata name");
        options.addOption("name", true, "-name node name");
        options.addOption(CommandLineConstants.CLUSTER, true, "cluster");
        return options;
    }

    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
        CommandLine cmd = ctx.getCommandLine();
        String meta = "CELL";
        if (cmd.hasOption("meta"))
        {
            meta = cmd.getOptionValue("meta");
        }
        String nodeName = meta + "_" + ClassUtil.getMainApplicationClass().getName();
        if (cmd.hasOption("name"))
        {
            // 设置nodeName,
            nodeName = cmd.getOptionValue("name");
        }
        ctx.getApp().setApplicationName(nodeName);
        int id = 0;
        if (cmd.hasOption("id"))
        {
            id = Integer.valueOf(cmd.getOptionValue("id"));
        }
        long version = 1;
        if (cmd.hasOption("id"))
        {
            version = Long.valueOf(cmd.getOptionValue("version"));
        }

        String cluster = cmd.getOptionValue(CommandLineConstants.CLUSTER);
        cluster = StringUtils.isEmpty(cluster) ? CommandLineConstants.DEFAULT_CLSUTER_VALUE : cluster;

        SpringNodeContext springNodeContext = (SpringNodeContext) ctx;
        springNodeContext.setNodeId(id);
        springNodeContext.setNodeName(nodeName);
        springNodeContext.setMetadataName(meta);
        springNodeContext.setVersion(version);
        springNodeContext.setCluster(cluster);

        // FIXME ,这里还需要进行拉取数据,从配置文件中/或者是url 中加载元数据信息
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
}
