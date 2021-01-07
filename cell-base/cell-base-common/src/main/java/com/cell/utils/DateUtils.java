/**
 * @author joker
 * @date 创建时间：2018年8月8日 上午9:15:39
 */
package com.cell.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * @author joker
 * @date 创建时间：2018年8月8日 上午9:15:39
 */
public class DateUtils
{
    public static final int DAY_OF_MILLSECONDS = 1000 * 3600 * 24;

    public static void main(String[] args)
    {
        Long a = 1594452019403l;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(new Date(a));
        System.out.println(format);
        System.out.println(Long.parseLong(format));
        //        //获取当前的年月日
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DAY_OF_MONTH, 0);
//        c.add(Calendar.DAY_OF_WEEK, 1);
//        System.out.println(c.getTime());
//        System.out.println(c.getTimeInMillis());
//        long rightNow = Long.parseLong(sdf.format(c.getTime()));
//        System.out.println(rightNow);


        // 获取当前年份的最后一天
//        Date lastDayOrTheYear = getLastDayOrTheYear(2020);
//        System.out.println(lastDayOrTheYear);
        System.out.println(new Date().getTime());
        System.out.println(getCurrentDay());
    }

    public static Long getCurrentDay()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        long rightNow = Long.parseLong(sdf.format(c.getTime()));
        return rightNow;
    }


    public static int calcDateDays(Date early, Date later)
    {
        long between = later.getTime() - early.getTime();
        return (int) (between / DAY_OF_MILLSECONDS);
    }


    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getLastDayOrTheYear(int year)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }


    public static String formatStandardTemplate(Date date)
    {
        if (null == date)
        {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }


    // 一天的毫秒数
    public static final long ONE_DAY_MS = 24 * 60 * 60 * 1000;
    public static final long ONE_HOUR_MS = 1 * 60 * 60 * 1000;

    private static ThreadLocal<SimpleDateFormat> dateFormatLocal1 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal2 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal3 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal4 = new ThreadLocal<>();
    // private static DateFormat statDateFormat = new SimpleDateFormat("MM-dd
    // HH:mm");

    private static ThreadLocal<SimpleDateFormat> dateFormatLocal5 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal6 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal7 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal8 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal9 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal10 = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> dateFormatLocal11 = new ThreadLocal<>();

    private static final long CURRENT_NANOS = System.currentTimeMillis() * 1000 * 1000;
    private static final long CURRENT_BEGIN_NANOS = System.nanoTime();

    public static long currentNanos()
    {
        long gap = System.nanoTime() - CURRENT_BEGIN_NANOS;
        long currentNaos = gap + CURRENT_NANOS;
        return currentNaos;
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay(Date date)
    {
        return getDateFormat3().format(date);
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDay2(Date date)
    {
        return getDateFormat6().format(date);
    }

    private static SimpleDateFormat getDateFormat1()
    {
        SimpleDateFormat dateFormat = dateFormatLocal1.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatLocal1.set(dateFormat);
        }
        return dateFormat;
    }

    public static SimpleDateFormat getDateFormat2()
    {
        SimpleDateFormat dateFormat = dateFormatLocal2.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            dateFormatLocal2.set(dateFormat);
        }
        return dateFormat;
    }

    /**
     * 格式化后日期格式： yyyyMMddHHmmssSSS
     *
     * @param date
     * @return
     */
    public static String formatDateAll(Date date)
    {
        return getDateFormat7().format(date);
    }

    public static LocalDate string2LocalDate(String date, String pattern)
    {
        pattern = pattern == null ? "yyyy-MM-dd" : pattern;
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime string2LocalDateTime(String date)
    {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String localDate2String(LocalDate date)
    {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String localDateTime2String(LocalDateTime datetime)
    {
        return datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 清除时分秒、毫秒域
     *
     * @param date
     * @return
     */
    public static long getDayTime(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    private static SimpleDateFormat getDateFormat3()
    {
        SimpleDateFormat dateFormat = dateFormatLocal3.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatLocal3.set(dateFormat);
        }
        return dateFormat;
    }

    public static SimpleDateFormat getDateFormat4()
    {
        SimpleDateFormat dateFormat = dateFormatLocal4.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateFormatLocal4.set(dateFormat);
        }
        return dateFormat;
    }

    public static SimpleDateFormat getDateFormat6()
    {
        SimpleDateFormat dateFormat = dateFormatLocal6.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyyMMdd");
            dateFormatLocal6.set(dateFormat);
        }
        return dateFormat;
    }

    public static SimpleDateFormat getDateFormat7()
    {
        SimpleDateFormat dateFormat = dateFormatLocal7.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            dateFormatLocal7.set(dateFormat);
        }
        return dateFormat;
    }

    public static SimpleDateFormat getDateFormat8()
    {
        SimpleDateFormat dateFormat = dateFormatLocal8.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            dateFormatLocal8.set(dateFormat);
        }
        return dateFormat;
    }

    private static SimpleDateFormat getDateFormat5()
    {
        SimpleDateFormat dateFormat = dateFormatLocal5.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            dateFormatLocal5.set(dateFormat);
        }
        return dateFormat;
    }

    private static SimpleDateFormat getDateFormat9()
    {
        SimpleDateFormat dateFormat = dateFormatLocal9.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormatLocal9.set(dateFormat);
        }
        return dateFormat;
    }

    private static SimpleDateFormat getDateFormat10()
    {
        SimpleDateFormat dateFormat = dateFormatLocal10.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatLocal10.set(dateFormat);
        }
        return dateFormat;
    }

    private static SimpleDateFormat getDateFormat11()
    {
        SimpleDateFormat dateFormat = dateFormatLocal11.get();
        if (dateFormat == null)
        {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormatLocal11.set(dateFormat);
        }
        return dateFormat;
    }

    /**
     * 将日期转化为String 格式：MMddHHmm
     *
     * @param date
     * @return
     */
    public static String dateToMMDDHHMM(Date date)
    {
        return getDateFormat5().format(date);
    }

    /**
     * 将日期转化为String 格式：MMddHHmm
     *
     * @param date
     * @return
     */
    public static String dateToMMDDHHMM(long date)
    {
        return getDateFormat5().format(date);
    }

    public static String dateToyyyyMMddHHmm(long date)
    {
        return getDateFormat9().format(date);
    }

    public static String dateToyyyyMMddHHmmss(long date)
    {
        return getDateFormat10().format(date);
    }

    public static String dateToyyyy(long date)
    {
        return getDateFormat11().format(date);
    }

    /**
     * 描述：将毫秒数的时间转为字符串时间：yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return <p>
     * 2016年11月3日 下午4:03:22
     */
    public static String millisToStr(long time)
    {
        Date date = new Date(time);
        return getDateFormat1().format(date);

    }

    /**
     * @param ms
     * @return
     * @Desc 描述：将毫秒数转为日期
     * @Date 2017年3月17日 下午8:19:15
     */
    public static Date getFormatDateFromMS(long ms)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ms);
        return cal.getTime();
    }

    /**
     * 将日期转化为String 格式：yyyyMMddHHmmss
     *
     * @param date
     * @return
     */
    public static String dateToYYYYMMDDHHMMSS(Date date)
    {
        return getDateFormat2().format(date);
    }

    /**
     * 将日期转化为String 格式：yyyyMMddHHmmss
     *
     * @return
     */
    public static String dateToYYYYMMDDHHMMSS()
    {
        return dateToYYYYMMDDHHMMSS(new Date());
    }

    /**
     * 将String 格式：yyyyMMddHHmmss转化为日期
     *
     * @return
     */
    public static Date stringYYYYMMDDHHMMSSToDate(String dateStr)
    {
        try
        {
            return getDateFormat2().parse(dateStr);
        } catch (ParseException e)
        {
        }
        return null;
    }

    /**
     * 获取日期。格式：yyyy-MM-dd
     *
     * @param date
     * @return 2015-6-29 下午3:56:40
     */
    public static String dateToStardardYYYYMMDD(Date date)
    {
        return getDateFormat3().format(date);
    }

    /**
     * @param millSecond
     * @return
     * @Desc 描述：根据给定的毫秒数，计算这个毫秒数代表的时间的下一天的零点时间戳。
     * @Date 2017年5月24日 上午9:50:19
     */
    public static long getTomorrowZeroTime(long millSecond)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millSecond);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, day + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * @return int
     * @Description 获取时间是一年中的那一天
     */
    public static int todayOfYear(long selectTime)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectTime);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取当前年份
     *
     * @param nowtime
     * @return 2016年2月11日 上午12:07:36
     */
    public static int getCurrentYear(long nowtime)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nowtime);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 字符串格式转毫秒,
     *
     * @param date yyyy-MM-dd HH:mm:ss
     * @return 2015-5-20 上午10:50:34
     * @throws ParseException
     */
    public static long strDateToMills(String date) throws ParseException
    {
        long day = 0;
        day = strDateToDate(date).getTime();
        return day;
    }

    /**
     * @param date yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static long strDateToSeconds(String date) throws ParseException
    {
        long seconds = 0;
        seconds = strDateToMills(date) / 1000;
        return seconds;
    }

    /**
     * @param date yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static Date strDateToDate(String date) throws ParseException
    {
        return getDateFormat1().parse(date);
    }

    /**
     * 获取一天的时分秒：格式:hh:mm:ss
     *
     * @param date
     * @return
     */
    public static String timeOfDayHHMMSS(Date date)
    {
        return getDateFormat4().format(date);
    }

    /**
     * yyyy-MM-dd-HH-mm-ss
     *
     * @param date
     * @return
     */
    public static String timeofAll(Date date)
    {
        return getDateFormat8().format(date);
    }

    /**
     * 获取时间格式为：HH:MM的形式
     *
     * @param date
     * @return 2016年1月7日 下午6:55:25
     */
    public static String timeOfDayHHMM(Date date)
    {
        SimpleDateFormat timerOfDayFormater12 = new SimpleDateFormat("HH:mm");
        return timerOfDayFormater12.format(date);
    }

    /**
     * 获取时间格式为：HH:MM的形式
     *
     * @return 2016年1月7日 下午6:55:25
     */
    public static String timeOfDayHHMM(long time)
    {

        return timeOfDayHHMM(new Date(time));
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day)
    {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day)
    {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.add(Calendar.DATE, -day);
        return now.getTime();
    }

    /**
     * 获取当天时间的开始时间 即：yyyy-MM-dd 00:00:00
     *
     * @param d
     * @return
     */
    public static Date getStartTimeByDay(Date d)
    {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTime();
    }

    /**
     * 获取当天时间的最后时间 即：yyyy-MM-dd 23:59:59
     *
     * @param d
     * @return
     */
    public static Date getLastTimeByDay(Date d)
    {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);

        return now.getTime();
    }

    /**
     * 判断是否同一天
     *
     * @param time1
     * @param time2
     * @return true 同一天；false 不在同一天
     */
    public static boolean isOneDay(long time1, long time2)
    {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time1);
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTimeInMillis(time2);

        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);

        if (year1 == year2 && month1 == month2 && day1 == day2)
        {
            return true;
        }

        return false;
    }

    /**
     * 返回两个时间之间相差的天数(默认参数1比参数2小)
     *
     * @return
     */
    public static int differenceNumberOfDays(long startTime, long currentTime)
    {
        int year1 = getCurrentYear(startTime);
        int year2 = getCurrentYear(currentTime);
        if (year1 != year2)
        {
            return 1;
        }
        int firstDay = todayOfYear(startTime);
        int secondDay = todayOfYear(currentTime);

        return Math.abs(secondDay - firstDay);
    }

    /**
     * 描述：获取当前时间的字符串格式时间：yyyy-MM-dd HH:mm:ss
     *
     * @return <p>
     * 2016年11月3日 下午7:01:28
     */
    public static String getNowDateStr()
    {
        Date date = new Date();
        return dateToStr(date);
    }

    /**
     * 描述：date转string字符串，格式：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return <p>
     * 2016年11月3日 下午6:59:52
     */
    public static String dateToStr(Date date)
    {
        return getDateFormat1().format(date);

    }

    /**
     * <p>
     * Title: isDayOfWeek
     * </p>
     * <p>
     * Description: 判断今天是周几
     * </p>
     *
     * @param day 按正常的周几输入，比如周一，就是1
     */
    public static boolean isDayOfWeek(int day)
    {
        Calendar strDate = Calendar.getInstance();
        if ((strDate.get(Calendar.DAY_OF_WEEK) - 1) == day)
        {
            return true;
        }
        return false;
    }

    /**
     * @param hhmmss
     * @return
     * @Desc 描述：把一个hh:mm:ss的时间转化为对应的毫秒数，时间格式必须是24小时制
     * @Date 2018年5月31日  下午4:19:40
     */
    public static long hhmmssTomills(String hhmmss)
    {
        if (hhmmss == null)
        {
            throw new IllegalArgumentException("时间格式不能为null" + ",应该是hh:mm:ss的格式");
        }
        String[] strs = hhmmss.split(":");
        if (strs.length != 3)
        {
            throw new IllegalArgumentException("时间格式不正确：" + hhmmss + ",应该是hh:mm:ss的格式");
        } else
        {
            int h = StringUtils.valueOfInt(strs[0]);
            int m = StringUtils.valueOfInt(strs[1]);
            int s = StringUtils.valueOfInt(strs[2]);
            long mills = (h * 3600L + m * 60L + s) * 1000L;
            return mills;
        }

    }

    public static long todayHhMmSsToMillis(String hhmmss)
    {
        if (hhmmss == null)
        {
            throw new IllegalArgumentException("时间格式不能为null" + ",应该是hh:mm:ss的格式");
        }
        String[] strs = hhmmss.split(":");
        if (strs.length != 3)
        {
            throw new IllegalArgumentException("时间格式不正确：" + hhmmss + ",应该是hh:mm:ss的格式");
        } else
        {
            return LocalDateTime.of(LocalDate.now(), LocalTime.parse(hhmmss)).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }

    }

    public static Long localDate2Long(LocalDate localDate)
    {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Long localDateTime2Long(LocalDateTime localDateTime)
    {
        if (localDateTime == null)
        {
            return null;
        }
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    public static Long localTime2Long(LocalTime localTime)
    {
        return localTime.toSecondOfDay() * 1000L;
    }

    public static LocalDate long2LocalDate(long timestamp)
    {
        return new Date(timestamp).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime long2LocalDateTime(long timestamp)
    {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static LocalTime long2LocalTime(long timestamp)
    {
        return LocalTime.ofSecondOfDay(timestamp / 1000);
    }

    // 一天内剩余的毫秒数
    public static long getLeftMillSecondsInDay()
    {
        LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long millSeconds = ChronoUnit.MILLIS.between(LocalDateTime.now(), midnight);
        return millSeconds;
    }

    //	获取当天零点时间对象
    public static LocalDateTime getTodayStartLocalDateTime()
    {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//		System.out.println(todayStart.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        return todayStart;
    }

    //	获取当天零点时间对象
    public static long getTodayStartLong()
    {
        return DateUtils.localDateTime2Long(getTodayStartLocalDateTime());
    }

    public static String getBeforeTimeStr(Date date)
    {
        Date now = new Date();
        long detSec = (now.getTime() - date.getTime()) / 1000;
        int month = 2592000;
        int day = 86400;
        int hour = 3600;
        int min = 60;

        String result;

        if (detSec > month)
        {
            //大于一个月，直接显示时间
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
            result = dateFormat.format(date.getTime());
        } else if (detSec > day)
        {
            //大于一天，显示几天前
            result = (int) (detSec / day) + "天";
        } else if (detSec > hour)
        {
            //大于一小时，显示几小时前
            result = (int) (detSec / hour) + "小时";
        } else if (detSec > min)
        {
            //大于一分钟，显示几分钟前
            result = (int) (detSec / min) + "分钟";
        } else
        {
            //显示一分钟前
            result = "1分钟";
        }

        return result;
    }

    // 获取当前时间,精度为秒
    public static int getTimeTillSeconds(Date date)
    {
        return (int) (date.getTime() / 1000);
    }
}


