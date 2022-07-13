package com.cell.base.common.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-11 20:29
 */
public class BigDecimalUtils
{

    public static DecimalFormat df = new DecimalFormat("#.0000");
    public static DecimalFormat df1 = new DecimalFormat("#.00");
    public static DecimalFormat df2 = new DecimalFormat("#.000");
    public static final BigDecimal one = new BigDecimal(1);

    public static BigDecimal fromDouble(Double num)
    {
        return new BigDecimal(num.toString());
    }

    /**
     * 设置保留的小数位数
     *
     * @param bd
     * @param scale
     * @return
     */
    public static BigDecimal setScale(BigDecimal bd, int scale)
    {
        switch (scale)
        {
            case 3:
                return new BigDecimal(df2.format(bd));
            case 4:
                return new BigDecimal(df.format(bd));
            default:
                return new BigDecimal(df1.format(bd));
        }
    }

    public static BigDecimal fromDouble(double num, int scale)
    {
        String tmp = String.format("%.2f", num);
        BigDecimal bd = new BigDecimal(tmp);
        return bd;
    }

    public static double getScaleValue(Double num, int scale)
    {
        String tmp = String.format("%.2f", num);
        return Double.valueOf(tmp);
    }

    // 保留最小有效位
    public static String formatBigDecimal(BigDecimal bigDecimal, int precision)
    {
        return param(bigDecimal, precision);
    }

    private static String param(BigDecimal value, int precision)
    {
        String str = value.stripTrailingZeros().toPlainString();
        String sub1 = str.substring(0, str.indexOf(".") + 1);
        String sub2 = str.substring(str.indexOf(".") + 1);
        StringBuilder buffer = new StringBuilder();
        int j = 0;
        for (int i = 0; i < sub2.length(); i++)
        {
            if (sub2.charAt(0) - '0' > 0)
            {
                return value.setScale(precision, RoundingMode.HALF_UP).toString();
            } else if (sub2.charAt(i) - '0' == 0&& j < precision)
            {
                buffer.append(sub2.charAt(i));
            } else if (sub2.charAt(i) - '0' > 0 && j < precision)
            {
                buffer.append(sub2.charAt(i));
                j++;
            }
        }
        return sub1 + buffer.toString();
    }

}
