package com.cell.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

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

}
