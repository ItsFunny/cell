package com.cell.base.common.utils;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 20:41
 */
public class UriUtils
{
    // FIXME
    public static String mergeUri(String prefix, String... uri)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (String s : uri)
        {
            sb.append(s).append("/");
        }
        String ret = removeExtraSlashOfUrl(sb.toString()).replaceAll("/$", "");
        return ret;
    }

    public static String removeExtraSlashOfUrl(String url)
    {
        if (url == null || url.length() == 0)
        {
            return url;
        }
        return url.replaceAll("(?<!(http:|https:))/+", "/");
    }
}
