package com.cell.utils;

import com.cell.base.common.utils.DateUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtilsTest
{

    @Test
    public void test1(){
        int currentMonth = DateUtils.getCurrentMonth();
        System.out.println(currentMonth);
    }
    @Test
    public void test2(){
        int currentDay = DateUtils.getCurrentDay();
        System.out.println(currentDay);
    }
    @Test
    public void test3()throws Exception{
        long s1 = System.currentTimeMillis();
        TimeUnit.SECONDS.sleep(10);
        long s2 = System.currentTimeMillis();
        System.out.println(s2-s1);
    }
    @Test
    public void test4()throws Exception{
        BigDecimal bigDecimal=new BigDecimal("67437693279.49671");
        BigDecimal b2=new BigDecimal("0.9991496960982161");
        BigDecimal divide = bigDecimal.divide(b2,4, BigDecimal.ROUND_HALF_UP);
        System.out.println(String.format("%.2f",divide));
        String str="query tokens {\n" +
                "  now: tokens(\n" +
                "      orderBy: tradeVolumeUSD\n" +
                "      orderDirection: desc\n" +
                "    ) {\n" +
                "      id\n" +
                "      symbol\n" +
                "      name\n" +
                "      derivedBNB\n" +
                "      derivedUSD\n" +
                "      tradeVolumeUSD\n" +
                "      totalTransactions\n" +
                "      totalLiquidity\n" +
                "    }\n" +
                "  }";
    }
    @Test
    public void testDate()throws Exception{
        System.out.println(new Date().getTime());
        long t=1654614510*1000l;
        Date date = new Date(1654614510000l);
        System.out.println(date.toString());
        System.out.println(date.getTime());
        System.out.println(t);
        Date date2=new Date();
        System.out.println(date2.getTime());
        System.out.println(date2.toString());
        TimeUnit.SECONDS.sleep(10);
        Date date1 = new Date();
        long time = date1.getTime();
        System.out.println(time);
        System.out.println(date1.toString());
        System.out.println(time-date2.getTime());
    }
    @Test
    public void testDate2(){
        Date date = new Date();
        Date dateAfterSeconds = DateUtils.getDateAfterSeconds(date, 10);
        long time1 = date.getTime();
        long time2 = dateAfterSeconds.getTime();
        System.out.println(time2-time1);
    }
}
