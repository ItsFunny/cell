package com.cell.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:38
 */
public class HttpUtils
{
    public static String getIpAddress(HttpServletRequest request)
    {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0)
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0)
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0)
        {
            ip = request.getRemoteAddr();
        }
        if (ip != null)
        {
            String[] temp = ip.split(",");
            if (temp.length > 1)
            {
                ip = temp[0];
            }
        }
        return ip.trim();
    }


}
