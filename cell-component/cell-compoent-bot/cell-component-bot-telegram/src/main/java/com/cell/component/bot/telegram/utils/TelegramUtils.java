package com.cell.component.bot.telegram.utils;

import com.cell.base.common.utils.RandomUtils;

public class TelegramUtils
{
    private static final String userExistPrefix = "e_";

    public static String generateRandomStr(Integer length)
    {
        return userExistPrefix + RandomUtils.randomString(length);
    }

    public static boolean isUserExistString(String str)
    {
        return str.startsWith(userExistPrefix);
    }

}
