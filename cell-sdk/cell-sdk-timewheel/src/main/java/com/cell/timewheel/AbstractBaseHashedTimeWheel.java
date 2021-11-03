package com.cell.timewheel;

import com.cell.discovery.nacos.config.AbstractInitOnce;
import com.cell.discovery.nacos.config.WheelTimerConfigurableNode;
import com.cell.context.InitCTX;
import com.cell.enums.TimeWheelDeleyLevel;
import com.cell.task.DefaultTimerTask;
import com.cell.task.TaskFuncs;
import io.netty.util.HashedWheelTimer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021/10/29 17:02
 */
public abstract class AbstractBaseHashedTimeWheel extends AbstractInitOnce
{
    private Map<TimeUnit, SimpleHiercalHashedWheelTimer> timeWheels = null;

    public void addTask(TaskFuncs funcs, TimeUnit unit, long delay)
    {
        SimpleHiercalHashedWheelTimer simpleHiercalHashedWheelTimer = this.timeWheels.get(unit);
        if (simpleHiercalHashedWheelTimer == null)
        {
            return;
        }
        simpleHiercalHashedWheelTimer.newTimeout(new DefaultTimerTask(funcs), delay, unit);
    }

    protected abstract List<WheelTimerConfigurableNode> supporsedTimePrecision();

    @Override
    protected void onInit(InitCTX ctx)
    {
        timeWheels = new HashMap<>();
        List<WheelTimerConfigurableNode> wheelTimerConfigurableNodes = this.supporsedTimePrecision();
        // FOR LOCK LESS
        for (WheelTimerConfigurableNode node : wheelTimerConfigurableNodes)
        {
            // FIXME : 写到配置文件中
            // FABRIC  时间精度只需要于分钟就行
            SimpleHiercalHashedWheelTimer hashedWheelTimer = new SimpleHiercalHashedWheelTimer(node.getThreadFactory(), node.getTickTime(), node.getTimeUnit(), node.getWheelBucketTicks(), node.isLeakDetection(), node.getMaxPendingTimeouts());
            timeWheels.put(node.getTimeUnit(), hashedWheelTimer);
        }
    }

    static class SimpleHiercalHashedWheelTimer extends HashedWheelTimer
    {
        public SimpleHiercalHashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection, long maxPendingTimeouts)
        {
            super(threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, maxPendingTimeouts);
        }
    }

    public static void main(String[] args) throws Exception
    {
        WheelTimerConfigurableNode node = new WheelTimerConfigurableNode.WheelTimerConfigurableNodeBuilder()
                .setTickTime(1)
                .setLevel(TimeWheelDeleyLevel.MIN)
                .setTimeUnit(TimeUnit.SECONDS)
                .setWheelBucketTicks(16)
                .build();

        SimpleHiercalHashedWheelTimer h = new SimpleHiercalHashedWheelTimer(node.getThreadFactory(),
                node.getTickTime(),
                node.getTimeUnit(),
                node.getWheelBucketTicks(),
                node.isLeakDetection(),
                node.getMaxPendingTimeouts());
        h.newTimeout((t) ->
        {
            System.out.println(t.isCancelled());
            System.out.println(t.task());
            System.out.println(t.timer());
        }, 1, TimeUnit.SECONDS);


        TimeUnit.HOURS.sleep(1);
    }
}
