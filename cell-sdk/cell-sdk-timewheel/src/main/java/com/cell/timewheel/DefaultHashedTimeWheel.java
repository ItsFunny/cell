package com.cell.timewheel;

import com.cell.grpc.common.config.WheelTimerConfigurableNode;
import com.cell.enums.TimeWheelDeleyLevel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 06:32
 */
public class DefaultHashedTimeWheel extends AbstractBaseHashedTimeWheel
{
    private static final DefaultHashedTimeWheel instance = new DefaultHashedTimeWheel();

    public static final DefaultHashedTimeWheel getInstance()
    {
        return instance;
    }

    static
    {
        instance.initOnce(null);
    }

    @Override
    protected List<WheelTimerConfigurableNode> supporsedTimePrecision()
    {
        WheelTimerConfigurableNode node = new WheelTimerConfigurableNode.WheelTimerConfigurableNodeBuilder()
                .setTickTime(1)
                .setLevel(TimeWheelDeleyLevel.MIN)
                .setTimeUnit(TimeUnit.MINUTES)
                .setWheelBucketTicks(16)
                .build();
        WheelTimerConfigurableNode secondsNode = new WheelTimerConfigurableNode.WheelTimerConfigurableNodeBuilder()
                .setTickTime(1)
                .setLevel(TimeWheelDeleyLevel.SECONDS)
                .setWheelBucketTicks(30)
                .setTimeUnit(TimeUnit.SECONDS)
                .build();
        WheelTimerConfigurableNode hourNode = new WheelTimerConfigurableNode.WheelTimerConfigurableNodeBuilder()
                .setTickTime(1)
                .setLevel(TimeWheelDeleyLevel.HOUR)
                .setWheelBucketTicks(30)
                .setTimeUnit(TimeUnit.HOURS)
                .build();

        return Arrays.asList(node, secondsNode, hourNode);
    }
}
