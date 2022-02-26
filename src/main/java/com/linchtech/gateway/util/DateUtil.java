package com.linchtech.gateway.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: 107
 * @date: 2020-03-22 14:54
 * @description:
 **/
public class DateUtil {

    private static final ThreadLocal<SimpleDateFormat> YYYY_MM_DD_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
    private static final ThreadLocal<SimpleDateFormat> YYYY_MM_DD_HH_MM_SS_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final ThreadLocal<SimpleDateFormat> HOUR_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("HH"));

    private static long DAY_OF_MILL = 24 * 3600 * 1000;

    private static Long millisecondOfHours = 60L * 60L * 1000L;


    public static int getYear(final int date) {
        return date / 10000;
    }

    public static int addYear(final int date, final int year) {
        return date + year * 10000;
    }

    public static Timestamp timestampNow() {
        return new Timestamp(System.currentTimeMillis());
    }


    public static String now(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    public static int intTimeNow() {
        return toTimeSeconds(new Date());
    }


    /**
     * 获取指定日期从0点开始经过的秒数，范围在0--86400之间
     *
     * @param date
     * @return
     */
    public static int toTimeSeconds(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .get(ChronoField.SECOND_OF_DAY);
    }

    /**
     * 时间戳转换字符串
     *
     * @param mill
     * @return
     */
    public static String getDateString(long mill) {
        SimpleDateFormat simpleDateFormat = YYYY_MM_DD_HH_MM_SS_THREAD_LOCAL.get();
        String format = simpleDateFormat.format(new Date(mill));
        YYYY_MM_DD_HH_MM_SS_THREAD_LOCAL.remove();
        return format;
    }


    /**
     * 获取某天0点0分0秒的时间戳
     *
     * @param mill
     * @return
     */
    public static Long get0ClockMillSecond(long mill) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mill));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取几小时以前的时间戳
     *
     * @param hour
     * @return
     */
    public static long getHoursAgoMillSecond(int hour) {
        return System.currentTimeMillis() - millisecondOfHours * hour;
    }

    /**
     * 毫秒时间戳转换int类型日期
     *
     * @param mill
     * @return
     */
    public static Integer millSecondToIntDate(Long mill) {
        SimpleDateFormat format = YYYY_MM_DD_THREAD_LOCAL.get();
        String date = format.format(new Date(mill));
        YYYY_MM_DD_THREAD_LOCAL.remove();
        return Integer.parseInt(date);
    }

    /**
     * 秒时间戳转换int类型日期
     *
     * @param dateTime 秒时间戳
     * @return
     */
    public static Integer intMillToIntDate(Integer dateTime) {
        SimpleDateFormat format = YYYY_MM_DD_THREAD_LOCAL.get();
        String date = format.format(new Date(dateTime.longValue() * 1000));
        YYYY_MM_DD_THREAD_LOCAL.remove();
        return Integer.parseInt(date);
    }

    /**
     * 根据时间戳获取日期是星期几
     *
     * @param mill
     * @return
     */
    public static int getWeekdayByMillSecond(long mill) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(mill));
        int w = instance.get(Calendar.DAY_OF_WEEK) - 1;
        // Calendar的星期天是返回的0,这里转换为7,方便计算
        if (w == 0) {
            return 7;
        } else {
            return w;
        }
    }

    /**
     * 根据时间戳获取日期是星期几
     *
     * @param date
     * @return
     */
    public static int getWeekdayByMillSecond(Integer date) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(intDateToMillSecond(date));
        int w = instance.get(Calendar.DAY_OF_WEEK) - 1;
        // Calendar的周日是返回的0,这里转换为7,方便计算
        if (w == 0) {
            return 7;
        } else {
            return w;
        }
    }

    /**
     * 获取某天23:59分59秒的时间戳
     *
     * @param mill
     * @return
     */
    public static long get24ClockMillSecond(long mill) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mill));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime().getTime();
    }

    /**
     * 昨天0点的秒时间戳
     *
     * @return
     */
    public static int getYesterday0Clock() {
        Long clockMillSecond = get0ClockMillSecond(System.currentTimeMillis());
        return (int) ((clockMillSecond - DAY_OF_MILL) / 1000);
    }

    /**
     * 获取时间戳的小时数
     *
     * @param mill
     * @return
     */
    public static int getHourNumber(long mill) {
        SimpleDateFormat format = HOUR_THREAD_LOCAL.get();
        String now = format.format(new Date(mill));
        HOUR_THREAD_LOCAL.remove();
        return Integer.parseInt(now);
    }

    /**
     * yyyyMMdd格式日期转换为毫秒时间戳
     *
     * @param date yyyyMMdd格式日期
     * @return 时间戳
     */
    public static Long intDateToMillSecond(Integer date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date dt = sdf.parse(date.toString());
            return dt.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("日期格式错误");
    }

    public static Integer intDateToIntSecond(Integer date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date dt = sdf.parse(date.toString());
            return (int) (dt.getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("日期格式错误");
    }

}
