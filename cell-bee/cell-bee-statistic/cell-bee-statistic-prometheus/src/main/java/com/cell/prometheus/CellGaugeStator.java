package com.cell.prometheus;

import com.cell.enums.EnumStatisticType;
import com.cell.log.LOG;
import com.cell.models.Module;
import io.prometheus.client.*;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 20:56
 */
public class CellGaugeStator extends BaseCollector<CellGaugeStator.Child> implements Collector.Describable
{
    protected CellGaugeStator(Builder b)
    {
        super(b);
        this.average = b.average;
        this.type = b.type;
        timestamp = System.currentTimeMillis();
        this.typeValue = b.typeValue;
    }

    public static Builder build(String name, String help)
    {
        return new Builder().name(name).help(help);
    }

    public static class Builder extends SimpleCollector.Builder<Builder, CellGaugeStator>
    {
        boolean average = false;
        EnumStatisticType type;
        int typeValue = 1;

        @Override
        public CellGaugeStator create()
        {
            return new CellGaugeStator(this);
        }

        public Builder average()
        {
            this.average = true;
            this.type = EnumStatisticType.SECOND;
            return this;
        }

        public Builder average(EnumStatisticType type)
        {
            this.average = true;
            this.type = type;
            return this;
        }

        public Builder average(EnumStatisticType type, int typeValue)
        {
            this.average = true;
            this.type = type;
            this.typeValue = typeValue;
            return this;
        }
    }

    public static class Timer implements Closeable
    {
        private final Child child;
        private final long start;

        private Timer(Child child)
        {
            this.child = child;
            start = Child.timeProvider.nanoTime();
        }

        public double setDuration()
        {
            double elapsed = (Child.timeProvider.nanoTime() - start) / NANOSECONDS_PER_SECOND;
            child.set(elapsed);
            return elapsed;
        }

        @Override
        public void close()
        {
            setDuration();
        }
    }

    public static class Child
    {
        private final DoubleAdder value = new DoubleAdder();

        static TimeProvider timeProvider = new TimeProvider();

        public void inc()
        {
            inc(1);
        }

        public void inc(double amt)
        {
            value.add(amt);
        }

        public void dec()
        {
            dec(1);
        }

        public void dec(double amt)
        {
            value.add(-amt);
        }

        public void set(double val)
        {
            synchronized (this)
            {
                value.reset();
                value.add(val);
            }
        }

        public void setToCurrentTime()
        {
            set(timeProvider.currentTimeMillis() / MILLISECONDS_PER_SECOND);
        }

        public Timer startTimer()
        {
            return new Timer(this);
        }

        public double setToTime(Runnable timeable)
        {
            Timer timer = startTimer();

            double elapsed;
            try
            {
                timeable.run();
            } finally
            {
                elapsed = timer.setDuration();
            }

            return elapsed;
        }

        public <E> E setToTime(Callable<E> timeable)
        {
            Timer timer = startTimer();

            try
            {
                return timeable.call();
            } catch (Exception e)
            {
                throw new RuntimeException(e);
            } finally
            {
                timer.setDuration();
            }
        }

        public double get()
        {
            synchronized (this)
            {
                return value.sum();
            }
        }
    }

    static class TimeProvider
    {
        long currentTimeMillis()
        {
            return System.currentTimeMillis();
        }

        long nanoTime()
        {
            return System.nanoTime();
        }
    }

    public void inc()
    {
        inc(1);
    }

    public void inc(double amt)
    {
        noLabelsChild.inc(amt);
    }

    public void dec()
    {
        dec(1);
    }

    public void dec(double amt)
    {
        noLabelsChild.dec(amt);
    }

    public void set(double val)
    {
        noLabelsChild.set(val);
    }

    public void setToCurrentTime()
    {
        noLabelsChild.setToCurrentTime();
    }

    public Timer startTimer()
    {
        return noLabelsChild.startTimer();
    }

    public double setToTime(Runnable timeable)
    {
        return noLabelsChild.setToTime(timeable);
    }

    public <E> E setToTime(Callable<E> timeable)
    {
        return noLabelsChild.setToTime(timeable);
    }

    public double get()
    {
        return noLabelsChild.get();
    }

    @Override
    public List<MetricFamilySamples> collect()
    {
        long now = System.currentTimeMillis();
        if (!canCollect())
        {
            LOG.debug(Module.STATISTIC, "忽略此次pull操作，type = {}, interval = {} ms.", this.type, now - this.timestamp);
            return Collections.emptyList();
        }

        List<MetricFamilySamples.Sample> samples = new ArrayList<>(children.size());
        for (Map.Entry<List<String>, Child> c : children.entrySet())
        {
            samples.add(new MetricFamilySamples.Sample(fullname, labelNames, c.getKey(),
                    getAverageDoubleValue(c.getValue().get(), now)));
        }

        if (this.average)
        {
            this.clear();
        }
        List<MetricFamilySamples> result = familySamplesList(Type.GAUGE, samples);
        return result;
    }

    @Override
    public List<MetricFamilySamples> describe()
    {
        return Collections.<MetricFamilySamples>singletonList(new GaugeMetricFamily(fullname, help, labelNames));
    }

    @Override
    protected Child newChild()
    {
        return new Child();
    }
}
