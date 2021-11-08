package com.cell.base.common.utils;

public class NetworkUtil
{

    public static long longLE2BE(long i)
    {
        return Long.reverseBytes(i);
    }

    public static long longBE2LE(long i)
    {
        return (i & 0xffL) << 56 | (i & 0xff00L) << 40 | (i & 0xff0000L) << 24 | (i & 0xff000000L) << 8
                | ((i >>> 56) & 0xffL) | ((i >>> 40) & 0xff00L) | ((i >>> 24) & 0xff0000L) | ((i >>> 8) & 0xff000000L);
    }

    public static int intBE2LE(int i)
    {
        return ((i & 0xff) << 24) | ((i & 0xff00) << 8) | ((i & 0xff0000) >> 8) | ((i >>> 24) & 0xff);
    }

    public static int intLE2BE(int i)
    {
        return intBE2LE(i);
    }

    public static short shortLE2BE(short i)
    {
        return (short) (((i & (short) 0xff) << 8) | ((i & (short) 0xff00) >> 8));
    }

    public static short shortBE2LE(short i)
    {
        return shortLE2BE(i);
    }

    public static long errorCode(int moduleId, int cmdId)
    {
        return (moduleId << 32 + cmdId);
    }

    public static void main(String[] args)
    {
        long m = 0x1023456789ABCDEFL;
        long r = longBE2LE(m);
        System.out.println(StringUtils.LongToHexString(r));
        System.out.println(StringUtils.LongToHexString(m));

        int m02 = 0x10234567;
        int r02 = intBE2LE(m02);

        System.out.println(StringUtils.LongToHexString(r02));
        System.out.println(StringUtils.LongToHexString(m02));


        short m03 = 0x1023;
        short r03 = shortLE2BE(m03);

        System.out.println(StringUtils.LongToHexString(r03));
        System.out.println(StringUtils.LongToHexString(m03));
    }
}
