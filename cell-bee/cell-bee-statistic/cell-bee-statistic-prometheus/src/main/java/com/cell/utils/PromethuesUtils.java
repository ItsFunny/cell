package com.cell.utils;

import com.cell.constants.StatConstants;
import com.cell.enums.EnumStatisticType;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-11 20:27
 */
public class PromethuesUtils
{
    public static double[] listToArray(List<Double> list)
    {
        double[] result = new double[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            Double tmp = list.get(i);
            if (tmp != null)
            {
                result[i] = list.get(i);
            }
        }
        return result;
    }

    public static double getAverageDoubleValue(boolean average, EnumStatisticType type, int typeValue, double num, long now, long timestamp)
    {
        if (!average || timestamp == 0L || num == 0D)
        {
            return num;
        }

        long time = now - timestamp;

        double multiNum = StatConstants.SECOND_SECTION;
        switch (type)
        {
            case MINUTE:
                multiNum = StatConstants.MINUTE_SECTION;
                break;
            case HOUR:
                multiNum = StatConstants.HOUR_SECTION;
                break;
            case DAY:
                multiNum = StatConstants.DAY_SECTION;
                break;
            default:
                break;
        }
        if (time == 0D || (time / (multiNum * typeValue * StatConstants.TIME_MULTIPLY)) == 0D)
        {
            return num;
        }
        return num * StatConstants.TIME_MULTIPLY / (time / (multiNum * typeValue));
    }

}
