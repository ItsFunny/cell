package com.cell.metrics.prometheus.sd.extension.utils;

import com.cell.base.common.utils.RandomUtils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-25 09:57
 */
public class SDUtils
{
    private static final Pattern WAIT_PATTERN = Pattern.compile("(\\d*)(m|s|ms|h)");

    public static long getWaitMillis(String wait)
    {
        // default from consul docu
        long millis = TimeUnit.MINUTES.toMillis(5);
        if (wait != null)
        {
            Matcher matcher = WAIT_PATTERN.matcher(wait);
            if (matcher.matches())
            {
                Long value = Long.valueOf(matcher.group(1));
                TimeUnit timeUnit = parseTimeUnit(matcher.group(2));
                millis = timeUnit.toMillis(value);
            } else
            {
                throw new IllegalArgumentException("Invalid wait pattern");
            }
        }
        return millis + RandomUtils.randomLong();
    }

    public static TimeUnit parseTimeUnit(String unit)
    {
        switch (unit)
        {
            case "h":
                return TimeUnit.HOURS;
            case "m":
                return TimeUnit.MINUTES;
            case "s":
                return TimeUnit.SECONDS;
            case "ms":
                return TimeUnit.MILLISECONDS;
            default:
                throw new IllegalArgumentException("No valid time unit");
        }
    }
}
