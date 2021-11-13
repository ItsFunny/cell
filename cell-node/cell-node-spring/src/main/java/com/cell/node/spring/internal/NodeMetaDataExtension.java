package com.cell.node.spring.internal;

import com.cell.base.common.constants.CommandLineConstants;
import com.cell.base.common.constants.OrderConstants;
import com.cell.base.common.exceptions.ConfigException;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.annotations.CellOrder;
import com.cell.node.core.configuration.NodeConfiguration;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.context.SpringNodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
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
        options.addOption(CommandLineConstants.NODE_ID, true, "node id");
        options.addOption(CommandLineConstants.META, true, "-meta metadata name");
        options.addOption(CommandLineConstants.CLUSTER, true, "cluster");
        return options;
    }

    @Override
    public void onInit(INodeContext ctx) throws Exception
    {
        CommandLine cmd = ctx.getCommandLine();
        String meta = "CELL";
        if (cmd.hasOption(CommandLineConstants.META))
        {
            meta = cmd.getOptionValue("meta");
        }

        if (!cmd.hasOption(CommandLineConstants.NODE_ID))
        {
            throw new ConfigException("nodeId must be setted");
        }
        String id = cmd.getOptionValue(CommandLineConstants.NODE_ID);


        long version = 1;
        if (cmd.hasOption(CommandLineConstants.VERSION))
        {
            version = Long.valueOf(cmd.getOptionValue(CommandLineConstants.VERSION));
        }

        String cluster = cmd.getOptionValue(CommandLineConstants.CLUSTER);
        cluster = StringUtils.isEmpty(cluster) ? CommandLineConstants.DEFAULT_CLSUTER_VALUE : cluster;

        NodeConfiguration.Node node = NodeConfiguration.getInstance().mustGetNode(id);
        SpringNodeContext springNodeContext = (SpringNodeContext) ctx;
        springNodeContext.setMetadataName(meta);
        springNodeContext.setVersion(version);
        springNodeContext.setCluster(cluster);
        springNodeContext.setNode(node);
        springNodeContext.getApp().setApplicationName(id);
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
