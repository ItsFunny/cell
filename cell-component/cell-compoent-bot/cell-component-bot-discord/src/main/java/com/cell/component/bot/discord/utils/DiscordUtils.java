package com.cell.component.bot.discord.utils;

import com.cell.base.common.utils.RandomUtils;

public class DiscordUtils
{
    private static final String userExistPrefix = "d_";

    public static String generateRandomStr(Integer length)
    {
        return userExistPrefix + RandomUtils.randomString(length);
    }

    public static boolean isUserExistString(String str)
    {
        return str.startsWith(userExistPrefix);
    }

}
