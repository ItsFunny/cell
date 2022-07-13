package com.cell;

import com.cell.base.common.utils.BigDecimalUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sign
{
    @Test
    public void test1() throws Exception
    {
        BigDecimal bg = new BigDecimal(0.00000022223);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(f1);
//        BigDecimal ret = new BigDecimal(0.00000022223).round(new MathContext(3, RoundingMode.HALF_UP));
//        System.out.println(ret);
    }

    @Test
    public void test2() throws Exception
    {
        double d = 0.0000545;
        int scale = 2;
        double d1 = significand(d, scale);
        double d2 = decimal(d, scale);
        System.out.println(d + "保留" + scale + "位有效数字：" + d1);
        System.out.println(d + "保留小数点之后" + scale + "位小数：" + d2);
    }

    @Test
    public void test3() throws Exception
    {
//        System.out.println(BigDecimalUtils.diviValidOrFraction(new BigDecimal(123.3333),new BigDecimal(1),2).toString());
//        System.out.println(BigDecimalUtils.diviValidOrFraction(new BigDecimal(0.0000002),new BigDecimal(1),2));

        BigDecimal b1 = new BigDecimal("0.0000002");
        BigDecimal bigDecimal = diviValidOrFraction("0.000231", "1", 2);
        System.out.println(param(new BigDecimal("0.000000000003"), 2));
        System.out.println(bigDecimal.toString());

        BigDecimal b2 = new BigDecimal("12.233");
        BigDecimal bigDecimal2 = diviValidOrFraction("12.22222", "1", 2);
        System.out.println(param(new BigDecimal("12.2222"), 2));
        System.out.println(bigDecimal2.toString());
    }

    public static BigDecimal param(BigDecimal value, int precision)
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
                return value.setScale(precision, RoundingMode.HALF_UP);
            } else if (sub2.charAt(i) - '0' == 0)
            {
                buffer.append(sub2.charAt(i));
            } else if (sub2.charAt(i) - '0' > 0 && j < precision)
            {
                buffer.append(sub2.charAt(i));
                j++;
            }
        }
        return new BigDecimal(sub1 + buffer.toString());
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
    public static BigDecimal diviValidOrFraction(String v1, String v2, int precision)
    {
        if (precision < 0)
        {
            throw new IllegalArgumentException("The precision must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        //如果这里不写保留的小数位（eg:10）其默认就会保留一位小数
        //这样之后的判断就会出错
        BigDecimal divide = b1.divide(b2, 30, RoundingMode.DOWN);
        return validOrFraction(divide, precision);
    }

    @Test
    public void test23123() throws Exception
    {
//        System.out.println(BigDecimalUtils.formatBigDecimal(new BigDecimal(1.1233333123123), 4));
        System.out.println(BigDecimalUtils.formatBigDecimal(new BigDecimal(0.0000000034123), 4));
        System.out.println(BigDecimalUtils.formatBigDecimal(new BigDecimal(0.096920133333330), 4));
    }

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
        return divide.setScale(precision, RoundingMode.HALF_UP);
    }

    @Test
    public void test44() throws Exception
    {
    }

    public static double significand(double oldDouble, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "scale指定的精度为非负值");
        }
        /**
         * RoundingMode：舍入模式
         * UP：远离零方向舍入的舍入模式；
         * DOWN：向零方向舍入的舍入模式；
         * CEILING： 向正无限大方向舍入的舍入模式；
         * FLOOR：向负无限大方向舍入的舍入模式；
         * HALF_DOWN：向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向下舍入；
         * HALF_UP：向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向上舍入；
         * HALF_EVEN：向最接近数字方向舍入的舍入模式，如果与两个相邻数字的距离相等，则向相邻的偶数舍入;(在重复进行一系列计算时,此舍入模式可以将累加错误减到最小)
         * UNNECESSARY：用于断言请求的操作具有精确结果的舍入模式，因此不需要舍入。
         */
        RoundingMode rMode = null;
        //rMode=RoundingMode.FLOOR;
        //下面这种情况，其实和FLOOR一样的。
        if (oldDouble > 0)
        {
            rMode = RoundingMode.DOWN;
        } else
        {
            rMode = RoundingMode.UP;
        }
        //此处的scale表示的是，几位有效位数
        BigDecimal b = new BigDecimal(Double.toString(oldDouble), new MathContext(scale, rMode));
        return b.doubleValue();
    }

    /**
     * 小数点之后保留几位小数(此处，我们用BigDecimal提供的（除以div）方法实现)
     *
     * @param oldDouble
     * @param scale
     * @return
     */
    public static double decimal(double oldDouble, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(oldDouble));
        BigDecimal one = new BigDecimal("1");
        //return b.divide(one, scale, BigDecimal.ROUND_FLOOR).doubleValue();
        if (oldDouble > 0)
        {
            //此处的scale表示的是，小数点之后的精度。
            return b.divide(one, scale, BigDecimal.ROUND_DOWN).doubleValue();
        } else
        {
            return b.divide(one, scale, BigDecimal.ROUND_UP).doubleValue();
        }
    }


    public static void main(String[] args) throws UnsupportedEncodingException
    {
        // 请替换⾃⼰的 accessKey, accessSecert
        // signatureNonce 为随机字符串, 每次请求都应重新随机⼀个字符串
        String accessKey = "gCgBfncbyOHRzpCXKKHxYOZB";
        String accessSecert = "morTwRTMrssnVXvySKAIeUlU";
        String signatureNonce = "12345678iuytrrewwqqw";
        long timestamp = System.currentTimeMillis();
        System.out.println("timestamp----------------" + timestamp);
        String params = "accessKey=" + accessKey + "&signatureNonce=" + signatureNonce + "&timestamp=" + timestamp;
        MessageDigest md = DigestUtils.getSha256Digest();
        md.update(accessSecert.getBytes("UTF-8"));
        byte[] result = md.digest(params.getBytes("UTF-8"));
        String signautre = Hex.encodeHexString(result);
        System.out.println("signautre----------------" + signautre);
    }
}
