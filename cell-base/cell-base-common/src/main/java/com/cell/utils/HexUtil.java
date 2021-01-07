package com.cell.utils;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-09-14 16:54
 */
public class HexUtil
{

    public static void main(String[] args) throws Exception
    {


        String mmm = "安全帽检测NEW";
        String eeew = "192.168.1.13-0";
        String bbbb = "895B68513D5EC0684B6D4E0045005700";
        String ccccc = "3100390032002E003100360038002E0031002E00310033002D003000";
        String chatset = "UTF-16LE";//UTF-8//GBK//Unicode//UTF-16LE

        //编码
        String aaa = string2HexUTF16LE(mmm);
        System.out.println(aaa);

        //解码
        String wqqqq = hexUTF16LE2String(bbbb);
        System.out.println(wqqqq);

    }


    /**
     * @param b 字节数组
     * @return 16进制字符串
     * @throws
     * @Title:bytes2HexString
     * @Description:字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b)
    {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++)
        {
            result.append(String.format("%02X", b[i]));
        }
        return result.toString();
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String src)
    {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++)
        {
            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }


    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexUTF8
     * @Description:字符UTF8串转16进制字符串
     */
    public static String string2HexUTF8(String strPart)
    {

        return string2HexString(strPart, "UTF-8");
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexUTF8
     * @Description:字符UTF-16LE串转16进制字符串,此UTF-16LE等同于C#中的Unicode
     */
    public static String string2HexUTF16LE(String strPart)
    {

        return string2HexString(strPart, "UTF-16LE");
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexUnicode
     * @Description:字符Unicode串转16进制字符串
     */
    public static String string2HexUnicode(String strPart)
    {

        return string2HexString(strPart, "Unicode");
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexGBK
     * @Description:字符GBK串转16进制字符串
     */
    public static String string2HexGBK(String strPart)
    {

        return string2HexString(strPart, "GBK");
    }

    /**
     * @param strPart    字符串
     * @param tochartype hex目标编码
     * @return 16进制字符串
     * @throws
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     */
    public static String string2HexString(String strPart, String tochartype)
    {
        try
        {
            return bytes2HexString(strPart.getBytes(tochartype));
        } catch (Exception e)
        {
            return "";
        }
    }

    ///

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexUTF82String
     * @Description:16进制UTF-8字符串转字符串
     */
    public static String hexUTF82String(String src)
    {

        return hexString2String(src, "UTF-8", "UTF-8");
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexUTF16LE2String
     * @Description:16进制UTF-8字符串转字符串，,此UTF-16LE等同于C#中的Unicode
     */
    public static String hexUTF16LE2String(String src)
    {

        return hexString2String(src, "UTF-16LE", "UTF-8");
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexGBK2String
     * @Description:16进制GBK字符串转字符串
     */
    public static String hexGBK2String(String src)
    {

        return hexString2String(src, "GBK", "UTF-8");
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexUnicode2String
     * @Description:16进制Unicode字符串转字符串
     */
    public static String hexUnicode2String(String src)
    {
        return hexString2String(src, "Unicode", "UTF-8");
    }

    /**
     * @param src 16进制字符串
     * @return 字节数组
     * @throws
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     */
    public static String hexString2String(String src, String oldchartype, String chartype)
    {
        byte[] bts = hexString2Bytes(src);
        try
        {
            if (oldchartype.equals(chartype))
            {
                return new String(bts, oldchartype);
            } else
            {
                return new String(new String(bts, oldchartype).getBytes(), chartype);
            }
        } catch (Exception e)
        {

            return "";
        }
    }


}