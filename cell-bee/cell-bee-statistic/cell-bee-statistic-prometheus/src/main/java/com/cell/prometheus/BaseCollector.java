package com.cell.prometheus;

import com.cell.constants.StatConstants;
import com.cell.enums.EnumStatOperateMask;
import com.cell.enums.EnumStatisticType;
import com.cell.utils.BigDecimalUtils;
import com.cell.utils.PromethuesUtils;
import io.prometheus.client.SimpleCollector;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-11 20:21
 */
public abstract class BaseCollector<Child> extends SimpleCollector<Child>
{

    /**
     * 默认保留小数点后两位
     **/
    public static final int DEFAULT_SCALE = 2;
    protected long timestamp;
    protected boolean average = false;
    protected EnumStatisticType type = EnumStatisticType.SECOND;
    protected int typeValue = 1;
    protected long operateMask = EnumStatOperateMask.ORIGIN.getValue();
    protected Map<EnumStatOperateMask, String> operateMaskNames = new HashMap<>(
            EnumStatOperateMask.values().length);

    protected BaseCollector(Builder<?, ?> b)
    {
        super(b);
    }

    protected double getAverageDoubleValue(boolean average, double num, long now)
    {
        return formatDouble(PromethuesUtils.getAverageDoubleValue(average, type, typeValue, num, now, timestamp));
    }

    protected double getAverageDoubleValue(double num, long now)
    {
        return formatDouble(PromethuesUtils.getAverageDoubleValue(average, type, typeValue, num, now, timestamp));
    }

    public boolean needOperate(EnumStatOperateMask statOperateMask)
    {
        return (statOperateMask.getValue() & operateMask) == statOperateMask.getValue();
    }

    public String getOperateMaskName(EnumStatOperateMask operateMask)
    {
        return operateMaskNames.get(operateMask);
    }

    public double formatDouble(double num)
    {
        return formatDouble(num, DEFAULT_SCALE);
    }

    public double formatDouble(double num, int scale)
    {
        return BigDecimalUtils.getScaleValue(num, scale);
    }

    @Override
    public void clear()
    {
        super.clear();
        this.timestamp = System.currentTimeMillis();
    }

    public boolean canCollect()
    {
        if (!average || timestamp == 0L)
        {
            return true;
        }

        long now = System.currentTimeMillis();
        long time = now - timestamp;
        switch (type)
        {
            case SECOND:
                if (time >= typeValue * StatConstants.SECOND_SECTION * StatConstants.TIME_MULTIPLY)
                {
                    return true;
                }
                return false;
            case MINUTE:
                if (time >= typeValue * StatConstants.MINUTE_SECTION * StatConstants.TIME_MULTIPLY)
                {
                    return true;
                }
                return false;
            case HOUR:
                if (time >= typeValue * StatConstants.HOUR_SECTION * StatConstants.TIME_MULTIPLY)
                {
                    return true;
                }
                return false;
            case DAY:
                if (time >= typeValue * StatConstants.DAY_SECTION * StatConstants.TIME_MULTIPLY)
                {
                    return true;
                }
                return false;

            default:
                return true;
        }
    }

    public static String doubleToGoString(double d, int idx)
    {
        if (idx % 2 == 0)
        {
            return "0 -> " + d;
        }
        return d + " -> +Inf";
    }
}
