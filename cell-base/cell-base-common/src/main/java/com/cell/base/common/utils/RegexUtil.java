package com.cell.base.common.utils;

import java.util.regex.Pattern;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-08-04 14:18
 */
public class RegexUtil
{
    // 判断是否包含特殊字符
    private static final Pattern specialCharacterPattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");
    private static final Pattern phonePattern = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");

    public static boolean containsSpecialCharacter(String str)
    {
        return specialCharacterPattern.matcher(str).find();
    }

    public static boolean validPhonePattern(String phone)
    {
        return phonePattern.matcher(phone).matches();
    }

    public static void main(String[] args)
    {
        System.out.println(validPhonePattern("123"));
        System.out.println(validPhonePattern("18757883747"));

    }
}
