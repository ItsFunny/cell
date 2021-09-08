package com.cell.utils;

import com.google.common.net.InetAddresses;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 20:59
 */
public class IPUtils
{
    public static int ipStringToInt(String ip)
    {
        return InetAddresses.coerceToInteger(InetAddresses.forString(ip));
    }

    public static String intToipString(int ip)
    {
        return String.format("%d.%d.%d.%d", (ip >>> 24 & 0xff), (ip >>> 16 & 0xff), (ip >>> 8 & 0xff), (ip & 0xff));
    }


    public static String getLocalAddress()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

    public static String ipIntToString(int userIP)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((userIP >>> 24) & 0xff);
        bytes[1] = (byte) ((userIP >>> 16) & 0xff);
        bytes[2] = (byte) ((userIP >>> 8) & 0xff);
        bytes[3] = (byte) (userIP & 0xff);
        try
        {
            InetAddress address = InetAddress.getByAddress(bytes);
            return address.getHostAddress();
        } catch (Throwable e)
        {
            return "0.0.0.0";
        }
    }

    public static boolean isIPv4(String ipAddress)
    {
        try
        {
            if (ipAddress == null || ipAddress.isEmpty())
            {
                return false;
            }

            String[] parts = ipAddress.split("\\.");
            if (parts.length != 4)
            {
                return false;
            }

            for (String s : parts)
            {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255))
                {
                    return false;
                }
            }
            if (ipAddress.endsWith("."))
            {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe)
        {
            return false;
        }
    }
}
