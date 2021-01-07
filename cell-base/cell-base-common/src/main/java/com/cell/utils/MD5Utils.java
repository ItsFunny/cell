package com.cell.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5Utils {
    public MD5Utils() {
    }

    public static String md5(String string) {
        if (string != null && string.trim().length() >= 1) {
            try {
                return getMD5(string.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException(var2.getMessage(), var2);
            }
        } else {
            return null;
        }
    }

    public static String getMD5(File file) {
        Object var1 = null;

        try {
            byte[] fileBytes = getFileBytes(file);
            return getMD5(fileBytes);
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static byte[] getFileBytes(File file) throws IOException
    {
        int length = (int)file.length();
        byte[] data = new byte[length];
        (new FileInputStream(file)).read(data);
        return data;
    }

    public static String getMD5(byte[] source) {
        String s = null;
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte[] tmp = md.digest();
            char[] str = new char[32];
            int k = 0;

            for(int i = 0; i < 16; ++i) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            s = new String(str);
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return s;
    }

    public static void main(String[] args) {
        System.out.println(md5("pqSVfvpbzO96qUSg50zKtv9eNo/nHXmpF1PPXBJK5do="));
    }
}
