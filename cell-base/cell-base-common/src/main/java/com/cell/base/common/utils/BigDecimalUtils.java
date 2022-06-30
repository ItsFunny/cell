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
    public static BigDecimal formatBigDecimal(BigDecimal bigDecimal, int precision)
    {
        return diviValidOrFraction(bigDecimal, one, precision);
    }

    /**
     * 相除时保留2位
     * 相除时，如果商的整数部分是0，则保留precision有效位
     * 否则，就保留precision位小数位。<br>
     * 例如：<table>
     * <tr>
     * <td>被除数</td>
     * <td>除数</td>
     * <td>结果</td>
     * </tr>
     * <tr><td>12341231.124125124D</td><td>10000</td><td>1234.12(保留小数)</td></tr>
     * <tr><td>0.124125124D</td><td>10000</td><td>0.000012(有效位)</td></tr>
     * </table>
     *
     * @param v1
     * @param v2
     * @param precision
     * @return
     * @author yutao
     * @date 2018年1月19日下午11:23:30
     */
    public static BigDecimal diviValidOrFraction(BigDecimal b1, BigDecimal b2, int precision)
    {
        if (precision < 0)
        {
            throw new IllegalArgumentException("The precision must be a positive integer or zero");
        }
        //如果这里不写保留的小数位（eg:10）其默认就会保留一位小数
        //这样之后的判断就会出错
        BigDecimal divide = b1.divide(b2, 10, RoundingMode.HALF_UP);
        return validOrFraction(divide, precision);
    }


    /**
     * 正数部分为0，保留有效位，否则保留小数位
     *
     * @param precision
     * @param b1
     * @param b2
     * @return
     * @author yutao
     * @date 2018年1月19日下午1:28:29
     */
    private static BigDecimal validOrFraction(BigDecimal divide, int precision)
    {

        Pattern p = Pattern.compile("-?(\\d+)(\\.*)(\\d*)");
        Matcher m = p.matcher(divide.toString());
        if (m.matches())
        {
            Long ll = Long.valueOf(m.group(1));
            //正数位为0，保留指定的有效位
            if (ll == 0)
            {
                MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
                //保留指定的有效位
                return divide.divide(BigDecimal.ONE, mc);
            }
        }
        //保留指定小数位
        return divide.setScale(precision, BigDecimal.ROUND_HALF_UP);
    }

}
