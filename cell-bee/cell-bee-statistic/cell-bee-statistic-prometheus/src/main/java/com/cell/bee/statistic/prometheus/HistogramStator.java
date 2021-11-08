package com.cell.bee.statistic.prometheus;

import com.cell.base.common.models.Module;
import com.cell.base.common.utils.BigDecimalUtils;
import com.cell.base.common.utils.MathUtils;
import com.cell.base.common.utils.StringUtils;
import com.cell.bee.statistic.base.enums.EnumStatOperateMask;
import com.cell.bee.statistic.base.enums.EnumStatisticType;
import com.cell.bee.statistic.prometheus.utils.PromethuesUtils;
import com.cell.sdk.log.LOG;
import io.prometheus.client.Collector;
import io.prometheus.client.DoubleAdder;
import io.prometheus.client.SimpleCollector;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 21:02
 */
public class HistogramStator extends BaseCollector<HistogramStator.Child> implements Collector.Describable
{
    private static final int LIMIT = 100000;
    private final double[] buckets;

    HistogramStator(Builder b)
    {
        super(b);
        buckets = b.buckets;
        average = b.average;
        this.type = b.type;
        timestamp = b.timestamp;
        this.operateMaskNames = b.operateMaskNames;
        this.operateMask = b.operateMask;
        this.typeValue = b.typeValue;
        initializeNoLabelsChild();
    }

    public static Builder build(String name, String help)
    {
        return new Builder().name(name).help(help);
    }

    public static Builder build()
    {
        return new Builder();
    }

    public void observe(double amt)
    {
        noLabelsChild.observe(amt);
    }

    public Timer startTimer()
    {
        return noLabelsChild.startTimer();
    }

    public double time(Runnable timeable)
    {
        return noLabelsChild.time(timeable);
    }

    public <E> E time(Callable<E> timeable)
    {
        return noLabelsChild.time(timeable);
    }

    public boolean isAverage()
    {
        return average;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    @Override
    public List<MetricFamilySamples> collect()
    {
        long now = System.currentTimeMillis();
        if (!canCollect())
        {
            LOG.info(Module.STATISTIC, "忽略此次pull操作，type = {}, interval = {} ", this.type, now - this.timestamp);
            return Collections.emptyList();
        }

        List<MetricFamilySamples.Sample> samples = new ArrayList<MetricFamilySamples.Sample>();
        collectOrigin(samples, now);

        int total = operateMaskNames.size();
        int index = 0;
        boolean isLast = false;
        for (EnumStatOperateMask mask : EnumStatOperateMask.values())
        {
            if (!needOperate(mask))
            {
                continue;
            }
            index++;
            if (index == total + 1)
            {
                isLast = true;
            }

            String name = getOperateMaskName(mask);
            switch (mask)
            {
                case MIN_VALUE:
                    collectMinValue(samples, name, isLast);
                    break;
                case MAX_VALUE:
                    collectMaxValue(samples, name, isLast);
                    break;
                case AVERAGE:
                    collectAverage(samples, name, isLast);
                    break;
                case STANDARD_DEVIATION:
                    collectStandardDeviation(samples, name, isLast);
                    break;

                default:
                    break;
            }
        }

        if (this.average)
        {
            this.clear();
        }

        List<MetricFamilySamples> result = familySamplesList(Type.HISTOGRAM, samples);
//		if (!StringUtil.isEmpty(result)) {
//			LOG.info(Module.STATISTICS_COMMON, "name collect statistics data, data = %s, result = %s", this.fullname, result);
//		}
        return result;
    }

    protected void collectOrigin(List<MetricFamilySamples.Sample> samples, long now)
    {
        for (Map.Entry<List<String>, Child> c : children.entrySet())
        {
            Child.Value v = c.getValue().get();
            List<String> labelNamesWithLe = new ArrayList<String>(labelNames);
            labelNamesWithLe.add("le");
            for (int i = 0; i < v.buckets.length; ++i)
            {
                List<String> labelValuesWithLe = new ArrayList<String>(c.getKey());
                labelValuesWithLe.add(doubleToGoString(buckets[revertIndex(i)], i));
                samples.add(new MetricFamilySamples.Sample(fullname + "", labelNamesWithLe, labelValuesWithLe,
                        getAverageDoubleValue(v.buckets[i], now)));
            }
            samples.add(new MetricFamilySamples.Sample(fullname + "_count", labelNames, c.getKey(),
                    getAverageDoubleValue(c.getValue().getCount(), now)));
            samples.add(new MetricFamilySamples.Sample(fullname + "_sum", labelNames, c.getKey(), getAverageDoubleValue(v.sum, now)));
        }
    }

    private int revertIndex(int index)
    {
        return index / 2;
    }

    /**
     * 计算标准差
     *
     * @return
     */
    public void collectStandardDeviation(List<MetricFamilySamples.Sample> samples, String name, boolean isLast)
    {
        long now = System.currentTimeMillis();
        for (Map.Entry<List<String>, Child> c : children.entrySet())
        {
            Child child = c.getValue();
            List<String> labelNamesWithLe = new ArrayList<String>(labelNames);
            List<String> labelValuesWithLe = new ArrayList<String>(c.getKey());
            samples.add(new MetricFamilySamples.Sample(name, labelNamesWithLe, labelValuesWithLe,
                    getAverageDoubleValue(false, child.getStandardDeviation(), now)));
            if (canClearChild(isLast, child))
            {
                child.clear();
            }
        }
    }

    /**
     * 计算最小值
     *
     * @return
     */
    public void collectMinValue(List<MetricFamilySamples.Sample> samples, String name, boolean isLast)
    {
        long now = System.currentTimeMillis();
        for (Map.Entry<List<String>, Child> c : children.entrySet())
        {
            Child child = c.getValue();
            List<String> labelNamesWithLe = new ArrayList<String>(labelNames);
            List<String> labelValuesWithLe = new ArrayList<String>(c.getKey());
            samples.add(new MetricFamilySamples.Sample(name, labelNamesWithLe, labelValuesWithLe,
                    getAverageDoubleValue(false, child.getMinValue(), now)));
            if (canClearChild(isLast, child))
            {
                child.clear();
            }
        }
    }

    private boolean canClearChild(boolean isLast, Child child)
    {
        return (isLast && average) || child.items.size() > LIMIT;
    }

    /**
     * 计算最大值
     *
     * @return
     */
    public void collectMaxValue(List<MetricFamilySamples.Sample> samples, String name, boolean isLast)
    {
        long now = System.currentTimeMillis();
        for (Map.Entry<List<String>, Child> c : children.entrySet())
        {
            Child child = c.getValue();
            List<String> labelNamesWithLe = new ArrayList<String>(labelNames);
            List<String> labelValuesWithLe = new ArrayList<String>(c.getKey());
            samples.add(new MetricFamilySamples.Sample(name, labelNamesWithLe, labelValuesWithLe,
                    getAverageDoubleValue(false, child.getMaxValue(), now)));
            if (canClearChild(isLast, child))
            {
                child.clear();
            }
        }
    }

    /**
     * 计算平均值
     *
     * @return
     */
    public void collectAverage(List<MetricFamilySamples.Sample> samples, String name, boolean isLast)
    {
        long now = System.currentTimeMillis();
//		List<MetricFamilySamples.Sample> tmp = new ArrayList<>();
        for (Map.Entry<List<String>, Child> c : children.entrySet())
        {
            Child child = c.getValue();
            List<String> labelNamesWithLe = new ArrayList<String>(labelNames);
            List<String> labelValuesWithLe = new ArrayList<String>(c.getKey());
            MetricFamilySamples.Sample sample = new MetricFamilySamples.Sample(name, labelNamesWithLe, labelValuesWithLe,
                    getAverageDoubleValue(false, child.getAverage(), now));
            samples.add(sample);
//			tmp.add(sample);
            if (canClearChild(isLast, child))
            {
                child.clear();
            }
        }

//		if (!StringUtil.isEmpty(tmp)) {
//			LOG.info(Module.STATISTICS_COMMON, "统计平局值： %s", tmp);
//		}
    }

//	private double getAverageLongValue(double num, long now) {
//		if (!average || this.timestamp == 0L) {
//			return num;
//		}
//
//		long time = now - this.timestamp;
//		if (time == 0) {
//			return num;
//		}
//
//		return num * 1000L / time;
//	}

    @Override
    public List<MetricFamilySamples> describe()
    {
        return Collections.singletonList(new MetricFamilySamples(fullname, Type.HISTOGRAM, help,
                Collections.<MetricFamilySamples.Sample>emptyList()));
    }

    @Override
    protected Child newChild()
    {
        return new Child(buckets);
    }

    public static class Child
    {

        public double time(Runnable timeable)
        {
            Timer timer = startTimer();

            double elapsed;
            try
            {
                timeable.run();
            } finally
            {
                elapsed = timer.observeDuration();
            }
            return elapsed;
        }

        public <E> E time(Callable<E> timeable)
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
                timer.observeDuration();
            }
        }

        public static class Value
        {
            public final double sum;
            public final double[] buckets;

            public Value(double sum, double[] buckets)
            {
                this.sum = sum;
                this.buckets = buckets;
            }
        }

        private Child(double[] buckets)
        {
            upperBounds = buckets;
            cumulativeCounts = new DoubleAdder[buckets.length * 2];
            for (int i = 0; i < cumulativeCounts.length; ++i)
            {
                cumulativeCounts[i] = new DoubleAdder();
            }
        }

        private final double[] upperBounds;
        private final DoubleAdder[] cumulativeCounts;
        private final DoubleAdder sum = new DoubleAdder();
        private List<Double> items = new CopyOnWriteArrayList<>();

        public void clear()
        {
            this.items = new CopyOnWriteArrayList<>();
        }

        public void observe(double amt)
        {
            items.add(amt);
            for (int i = 0; i < upperBounds.length; ++i)
            {
                // The last bucket is +Inf, so we always increment.
                if (amt <= upperBounds[i])
                {
                    cumulativeCounts[i * 2].add(1);
                } else
                {
                    cumulativeCounts[i * 2 + 1].add(1);
                }
                sum.add(amt);
            }

//			int index = upperBounds.length - 2;
//			if (amt > upperBounds[index]) {
//				cumulativeCounts[index + 1].add(1);
//			}
//			sum.add(amt);
        }

        /**
         * 获取最大值
         *
         * @return
         */
        public double getMaxValue()
        {
            if (items.size() == 0)
            {
                return 0D;
            }

            double max = 0D;
            for (Double value : items)
            {
                if (value != null)
                {
                    max = value > max ? value : max;
                }
            }
            return max;
        }

        /**
         * 获取最小值
         *
         * @return
         */
        public double getMinValue()
        {
            if (items.size() == 0)
            {
                return 0D;
            }

            double min = Double.MAX_VALUE;
            try
            {
                for (Double value : items)
                {
                    if (value != null)
                    {
                        min = value < min ? value : min;
                    }
                }
            } catch (Exception e)
            {
                LOG.error(Module.STATISTIC, e, "items = %s", items);
                return 0D;
            }
            return min;
        }

        /**
         * 获取平均值
         *
         * @return
         */
        public double getAverage()
        {
            if (items.size() == 0)
            {
                return 0D;
            }

            double sum = 0D;
            for (Double value : items)
            {
                if (value != null)
                {
                    sum += value;
                }
            }
            return BigDecimalUtils.getScaleValue(sum / items.size(), 2);
        }

        /**
         * 计算标准差
         *
         * @return
         */
        public double getStandardDeviation()
        {
            if (items.size() == 0)
            {
                return 0D;
            }

            List<Double> copy = new ArrayList<>(items);
            double[] nums = PromethuesUtils.listToArray(copy);
            return MathUtils.standardDeviation(nums);
        }

        public Timer startTimer()
        {
            return new Timer(this, SimpleTimer.defaultTimeProvider.nanoTime());
        }

        public double getCount()
        {
            double acc = 0;
            for (int i = 0; i < cumulativeCounts.length; ++i)
            {
                acc += cumulativeCounts[i].sum();
            }
            return acc;
        }

        public Value get()
        {
            double[] buckets = new double[cumulativeCounts.length];
            double acc = 0;
            for (int i = 0; i < cumulativeCounts.length; ++i)
            {
                acc = cumulativeCounts[i].sum();
                buckets[i] = acc;
            }
            return new Value(sum.sum(), buckets);
        }
    }

    public static class Timer implements Closeable
    {
        private final Child child;
        private final long start;

        private Timer(Child child, long start)
        {
            this.child = child;
            this.start = start;
        }

        public double observeDuration()
        {
            double elapsed = SimpleTimer.elapsedSecondsFromNanos(start, SimpleTimer.defaultTimeProvider.nanoTime());
            child.observe(elapsed);
            return elapsed;
        }

        @Override
        public void close()
        {
            observeDuration();
        }
    }

    double[] getBuckets()
    {
        return buckets;
    }

    public static class Builder extends SimpleCollector.Builder<Builder, HistogramStator>
    {
        private long timestamp = System.currentTimeMillis();
        private double[] buckets = new double[]{.005, .01, .025, .05, .075, .1, .25, .5, .75, 1, 2.5, 5, 7.5, 10};
        private boolean average = false;
        EnumStatisticType type = EnumStatisticType.SECOND;
        int typeValue;
        protected long operateMask = EnumStatOperateMask.ORIGIN.getValue();
        protected Map<EnumStatOperateMask, String> operateMaskNames = new HashMap<>(
                EnumStatOperateMask.values().length);

        public Builder operate(EnumStatOperateMask mask, String operateMaskName)
        {
            if (mask == null || StringUtils.isNullEmpty(operateMaskName))
            {
                throw new IllegalArgumentException("operateMask and operateMaskName should not be null!");
            }

            operateMaskNames.put(mask, operateMaskName);
            this.operateMask = (this.operateMask | mask.getValue());
            return this;
        }

        @Override
        public HistogramStator create()
        {
            for (int i = 0; i < buckets.length - 1; i++)
            {
                if (buckets[i] >= buckets[i + 1])
                {
                    throw new IllegalStateException("HistogramStator buckets must be in increasing order: " + buckets[i]
                            + " >= " + buckets[i + 1]);
                }
            }
            if (buckets.length == 0)
            {
                throw new IllegalStateException("HistogramStator must have at least one bucket.");
            }

//			if (buckets[buckets.length - 1] != Double.POSITIVE_INFINITY) {
//				double[] tmp = new double[buckets.length + 1];
//				System.arraycopy(buckets, 0, tmp, 0, buckets.length);
//				tmp[buckets.length] = Double.POSITIVE_INFINITY;
//				buckets = tmp;
//			}
            return new HistogramStator(this);
        }

        public Builder buckets(double... buckets)
        {
            this.buckets = buckets;
            return this;
        }

        /**
         * collect时将会根据时间计算平均值
         *
         * @return
         */
        public Builder average()
        {
            this.average = true;
            this.type = EnumStatisticType.SECOND;
            this.typeValue = 1;
            return this;
        }

        public Builder average(EnumStatisticType type)
        {
            this.average = true;
            this.type = type;
            this.typeValue = 1;
            return this;
        }

        public Builder average(EnumStatisticType type, int typeValue)
        {
            this.average = true;
            this.type = type;
            this.typeValue = typeValue;
            return this;
        }

        public Builder linearBuckets(double start, double width, int count)
        {
            buckets = new double[count];
            for (int i = 0; i < count; i++)
            {
                buckets[i] = start + i * width;
            }
            return this;
        }

        public Builder exponentialBuckets(double start, double factor, int count)
        {
            buckets = new double[count];
            for (int i = 0; i < count; i++)
            {
                buckets[i] = start * Math.pow(factor, i);
            }
            return this;
        }

    }
}
