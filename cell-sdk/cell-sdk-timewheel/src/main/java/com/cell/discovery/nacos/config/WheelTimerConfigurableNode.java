package com.cell.discovery.nacos.config;


import com.cell.enums.TimeWheelDeleyLevel;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-23 17:11
 */
public class WheelTimerConfigurableNode
{
    private WheelTimerConfigurableNode()
    {

    }

    private TimeWheelDeleyLevel level;
    private ThreadFactory threadFactory;
    private long tickTime;
    // wheel 中bucket容量
    private Integer wheelBucketTicks;
    // 因为不想和TimeWheelDeleyLevel 绑定太多
    private TimeUnit timeUnit;
    private boolean leakDetection;
    private long maxPendingTimeouts;

    public long getMaxPendingTimeouts()
    {
        return maxPendingTimeouts;
    }

    public boolean isLeakDetection()
    {
        return leakDetection;
    }


    public Integer getWheelBucketTicks()
    {
        return wheelBucketTicks;
    }

    public TimeWheelDeleyLevel getLevel()
    {
        return level;
    }

    public static class WheelTimerConfigurableNodeBuilder
    {
        private TimeWheelDeleyLevel level;
        private ThreadFactory threadFactory;
        private long tickTime;
        // wheel 中bucket容量
        // 因为不想和TimeWheelDeleyLevel 绑定太多
        private TimeUnit timeUnit;
        // wheel 中bucket容量
        private Integer wheelBucketTicks;
        private boolean leakDetection=true;
        private long maxPendingTimeouts=-1l;

        public WheelTimerConfigurableNodeBuilder setLeakDetection(boolean leakDetection)
        {
            this.leakDetection = leakDetection;
            return this;
        }

        public WheelTimerConfigurableNodeBuilder setMaxPendingTimeouts(long maxPendingTimeouts)
        {
            this.maxPendingTimeouts = maxPendingTimeouts;
            return this;
        }

        public WheelTimerConfigurableNodeBuilder setWheelBucketTicks(Integer wheelBucketTicks)
        {
            this.wheelBucketTicks = wheelBucketTicks;
            return this;
        }

        public WheelTimerConfigurableNodeBuilder setLevel(TimeWheelDeleyLevel level)
        {
            this.level = level;
            return this;
        }


        public WheelTimerConfigurableNodeBuilder setThreadFactory(ThreadFactory threadFactory)
        {
            this.threadFactory = threadFactory;
            return this;
        }


        public WheelTimerConfigurableNodeBuilder setTickTime(long tickTime)
        {
            this.tickTime = tickTime;
            return this;
        }

        public WheelTimerConfigurableNodeBuilder setTimeUnit(TimeUnit timeUnit)
        {
            this.timeUnit = timeUnit;
            return this;
        }

        public WheelTimerConfigurableNode build()
        {
            WheelTimerConfigurableNode node = new WheelTimerConfigurableNode();
            if (this.level == null)
            {
                throw new RuntimeException("level should not be nil");
            }
            if (this.tickTime <= 0)
            {
                throw new RuntimeException("tickTime cant be nil");
            }
            node.tickTime = this.tickTime;
            node.level = this.level;
            if (this.threadFactory == null)
            {
                this.threadFactory = Executors.defaultThreadFactory();
            }
            node.threadFactory = this.threadFactory;
            if (this.timeUnit == null)
            {
                this.timeUnit = TimeUnit.MINUTES;
            }
            node.timeUnit = this.timeUnit;
            if (this.wheelBucketTicks == null || this.wheelBucketTicks <= 0)
            {
                this.wheelBucketTicks = 512;
            }
            node.wheelBucketTicks=this.wheelBucketTicks;
            node.leakDetection=this.leakDetection;
            node.maxPendingTimeouts=this.maxPendingTimeouts;

            return node;
        }
    }

    public long getTickTime()
    {
        return tickTime;
    }


    public TimeUnit getTimeUnit()
    {
        return timeUnit;
    }


    public ThreadFactory getThreadFactory()
    {
        return threadFactory;
    }

}
