package com.cell.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Charlie
 * @When
 * @Description 业务的util, 如生成组织机构代码等
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-11-19 13:32
 */
public class LogicUtils
{
    //生成企业组织机构代码
    public static String getORGANIZATION_CODE()
    {
        int[] in = {3, 7, 9, 10, 5, 8, 4, 2};
        String data = "";
        String yz = "";
        int a = 0;
        //随机生成英文字母和数字
        for (int i = 0; i < in.length; i++)
        {
            String word = getCharAndNumr(1, 0).toUpperCase();
            if (word.matches("[A-Z]"))
            {
                a += in[i] * getAsc(word);
            } else
            {
                a += in[i] * Integer.parseInt(word);
            }
            data += word;
        }
        //确定序列
        int c9 = 11 - a % 11;
        //判断c9大小，安装 X 0 或者C9
        if (c9 == 10)
        {
            yz = "X";
        } else if (c9 == 11)
        {
            yz = "0";
        } else
        {
            yz = c9 + "";
        }
        data += "-" + yz;
        return data.toUpperCase();
    }

    public static String getOrganizationCodeFromSocialCode(String socialCode)
    {
        int length = socialCode.length();
        int end = length - 1;
        int start = length - 1 - 9;
        return socialCode.substring(start, end);
    }

    //生成营业执照代码
    public static String getBUSINESS_LISENSE_CODE()
    {
        String data = "";
        //随机生成14位数字
        String number = getCharAndNumr(14, 1);
        //获取校验后的第15位
        String yz = getBusinesslicenseCheckBit(number) + "";
        //拼凑
        data = number + yz;
        return data.toUpperCase();
    }

    //生成税务登记号码
    public static String getTAX_REGISTRATION_CODE()
    {
        String data = "";
        String first = "73" + getCharAndNumr(4, 2);
        String end = getORGANIZATION_CODE();
        data = first + end;
        data = data.toUpperCase().replaceAll("-", "");
        if (!test5(data.toUpperCase())) getTAX_REGISTRATION_CODE();
        return data;
    }

    //生成统一社会信用代码
    public static String getSOCIAL_CREDIT_CODE()
    {
        String data = "";
        String first = "Y2" + getCharAndNumr(6, 3) + getCharAndNumr(9, 3);
        String end = String.valueOf(getUSCCCheckBit(first));
        data = first + end;
        if (!test4(data.toUpperCase())) getSOCIAL_CREDIT_CODE();
        return data.toUpperCase();
    }

    public static String getCharAndNumr(int length, int status)
    {
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        String charStr = "0123456789abcdefghijklmnopqrstuvwxy";
        if (status == 1) charStr = "0123456789";
        if (status == 2) charStr = "0123456789";
        if (status == 3) charStr = "0123456789ABCDEFGHJKLMNPQRTUWXY";
        int charLength = charStr.length();
        for (int i = 0; i < length; i++)
        {
            int index = random.nextInt(charLength);
            if (status == 1 && index == 0)
            {
                index = 3;
            }
            valSb.append(charStr.charAt(index));
        }
        return valSb.toString();
    }

    private static char getUSCCCheckBit(String businessCode)
    {
        if (("".equals(businessCode)) || businessCode.length() != 17)
        {
            return 0;
        }
        String baseCode = "0123456789ABCDEFGHJKLMNPQRTUWXY";
        char[] baseCodeArray = baseCode.toCharArray();
        Map<Character, Integer> codes = new HashMap<Character, Integer>();
        for (int i = 0; i < baseCode.length(); i++)
        {
            codes.put(baseCodeArray[i], i);
        }
        char[] businessCodeArray = businessCode.toCharArray();

        int[] wi = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};
        int sum = 0;
        for (int i = 0; i < 17; i++)
        {
            Character key = businessCodeArray[i];
            if (baseCode.indexOf(key) == -1)
            {
                return 0;
            }
            sum += (codes.get(key) * wi[i]);
        }
        int value = 31 - sum % 31;
        if (value == 31)
        {
            value = 0;
        }
        return baseCodeArray[value];
    }

    public static int getAsc(String st)
    {
        byte[] gc = st.getBytes();
        int ascNum = (int) gc[0] - 55;
        return ascNum;
    }

    /**
     * 校验 营业执照注册号
     *
     * @param businesslicense
     * @return
     */
    public static int getBusinesslicenseCheckBit(String businesslicense)
    {
        if (businesslicense.length() != 14)
        {
            return 0;
        }

        char[] chars = businesslicense.toCharArray();
        int[] ints = new int[chars.length];
        for (int i = 0; i < chars.length; i++)
        {
            ints[i] = Integer.parseInt(String.valueOf(chars[i]));
        }
        return getCheckCode(ints);
    }

    /**
     * 获取 营业执照注册号的校验码
     *
     * @param
     * @return bit
     */
    private static int getCheckCode(int[] ints)
    {
        if (null != ints && ints.length > 1)
        {
            int ti = 0;
            int si = 0;// pi|11+ti
            int cj = 0;// （si||10==0？10：si||10）*2
            int pj = 10;// pj=cj|11==0?10:cj|11
            for (int i = 0; i < ints.length; i++)
            {
                ti = ints[i];
                pj = (cj % 11) == 0 ? 10 : (cj % 11);
                si = pj + ti;
                cj = (0 == si % 10 ? 10 : si % 10) * 2;
                if (i == ints.length - 1)
                {
                    pj = (cj % 11) == 0 ? 10 : (cj % 11);
                    return pj == 1 ? 1 : 11 - pj;
                }
            }
        }
        return -1;

    }

    public static String getCheckBit(String code)
    {
        String yz = "";
        int[] in = {3, 7, 9, 10, 5, 8, 4, 2};
        int a = 0;
        for (int i = 0; i < in.length; i++)
        {
            if (code.substring(i, i + 1).matches("[A-Z]"))
            {
                a += in[i] * getAsc(code.substring(i, i + 1));
            } else
            {
                a += in[i] * Integer.parseInt(code.substring(i, i + 1));
            }
        }
        int c9 = 11 - a % 11;
        if (c9 == 10)
        {
            yz = "X";
        } else if (c9 == 11)
        {
            yz = "0";
        } else
        {
            yz = c9 + "";
        }

        return yz;
    }

    public static boolean test(String data)
    {
        String code = data.replace("-", "");
        return data.endsWith(getCheckBit(code.substring(0, code.length() - 1)));
    }

    public static boolean test1(String data)
    {
        return data.endsWith(String.valueOf(getBusinesslicenseCheckBit(data.substring(0, data.length() - 1))));
    }

    public static boolean test2(String data)
    {
        return data.endsWith(String.valueOf(getCheckBit(data.substring(6, data.length() - 1))));
    }

    public static boolean test3(String data)
    {
        return data.endsWith(String.valueOf(getUSCCCheckBit(data.substring(0, data.length() - 1))));
    }

    public static boolean test4(String data)
    {
        if (data == null)
        {
            return false;
        }
        if (data.length() != 18)
        {
            return false;
        }
        if (!data.matches("[a-zA-Z0-9]+"))
        {
            return false;
        }
        String regex = "^([159Y]{1})([1239]{1})([0-9ABCDEFGHJKLMNPQRTUWXY]{6})([0-9ABCDEFGHJKLMNPQRTUWXY]{9})([0-90-9ABCDEFGHJKLMNPQRTUWXY])$";
        if (!data.matches(regex))
        {
            return false;
        }
        return true;
    }

    public static boolean test5(String data)
    {
        String regex = "[1-8][1-6]\\d{4}[a-zA-Z0-9]{9}$";
        if (!data.matches(regex))
        {
            return false;
        } else
        {
            return true;
        }
    }

    public static void main(String[] args) throws Exception
    {


        //
//
        String code = getSOCIAL_CREDIT_CODE();
        System.out.println(code);
        code = getOrganizationCodeFromSocialCode(code);
        System.out.println(code);

//        code = getBUSINESS_LISENSE_CODE();
//        System.out.println(code);
//
//        System.out.println(test1(code));
//        code = getTAX_REGISTRATION_CODE();
//        System.out.println(code);
//        System.out.println(test2(code));
//        System.out.println(test5(code));
//        code = getSOCIAL_CREDIT_CODE();
//        System.out.println(code);
//        System.out.println(test3(code));
//        System.out.println("test4: " + test4(code));
//        System.out.println(object.dataVerify(code2));
//        System.out.println("仿真:" + object.simulation(code2));

//        System.out.println("屏蔽:" + object.maskingOut(code2));
//        System.out.println("替换:" + object.substitution(code2));
//        System.out.println("仿真:" + object.simulation(code2));
//        System.out.println("变形:" + object.variance(code2));
//        System.out.println("加密:" + object.encrypt(code2));
    }
//
//    }


}
