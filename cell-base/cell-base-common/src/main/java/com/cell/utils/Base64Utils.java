package com.cell.utils;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-24 11:13
 */
public class Base64Utils
{
    public Base64Utils()
    {
    }

    public static void main(String[] args)
    {
        byte[] data = decode("FnCBVRdU3LazA2XoH7Qt1Zgsn97vtI3ARnpdLYctpFI=");
        System.out.println();
    }

    public static String encode(byte[] binaryData)
    {
        return Base64.getEncoder().encodeToString(binaryData);
    }

    public static byte[] decode(String base64String)
    {
        return Base64.getDecoder().decode(base64String);
    }

    public static byte[] longToBytes(Long x)
    {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public static Long bytesToLong(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static byte[] getBytes(String data)
    {
        try
        {
            return data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var2)
        {
            return null;
        }
    }

    public static String strToBase64Str(String data)
    {
        return encode(getBytes(data));
    }
}