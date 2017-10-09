package com.tino.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {
    private static String DATE_FORMAT = "M月d日 HH:mm";
    private static String DAY = "天前";
    private static String HOUR = "小时前";
    private static String JUST_NOW = "刚刚";

    private static int MILL_MIN = 60000;
    private static String MIN = "分钟前";
    private static String MONTH = "月前";
    private static String THE_DAY_BEFORE_YESTER_DAY = "前天";
    private static String TODAY = "今天";
    private static String YEAR = "年前";
    private static String YEAR_FORMAT = "yyyy年 M月d日 HH:mm";
    private static String YESTER_DAY = "昨天";
    private static SimpleDateFormat dateFormat = null;
    private static SimpleDateFormat dayFormat = null;
    private static Calendar msgCalendar = null;
    private static SimpleDateFormat yearFormat = null;

    private static int MILL_HOUR = (MILL_MIN * 60);
    private static int MILL_DAY = (MILL_HOUR * 24);
    public static long gmtToLong(String gmt) {
        DateFormat Gmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        long lMofifyTime = System.currentTimeMillis();
        try {
            return Gmt.parse(gmt).getTime() - 28800000;
        } catch (ParseException e) {
            e.printStackTrace();
            return lMofifyTime;
        }
    }

    public static String getTimeDistance(String gmt) {
        return getListTime(gmtToLong(gmt));
    }

    public static String getListTime(long time) {
        long now = System.currentTimeMillis();
        long msg = time;
        Calendar nowCalendar = Calendar.getInstance();
        if (msgCalendar == null) {
            msgCalendar = Calendar.getInstance();
        }
        msgCalendar.setTimeInMillis(time);
        long calSeconds = (now - msg) / 1000;
        if (calSeconds < 60) {
            return JUST_NOW;
        }
        long calMins = calSeconds / 60;
        if (calMins < 60) {
            return calMins + MIN;
        }
        long calHours = calMins / 60;
        if (calHours >= 24 || !isSameDay(nowCalendar, msgCalendar)) {
            long calDay = calHours / 24;
            if (calDay < 31) {
                if (isYesterDay(nowCalendar, msgCalendar)) {
                    if (dayFormat == null) {
                        dayFormat = new SimpleDateFormat("HH:mm");
                    }
                    return new StringBuilder(YESTER_DAY).append(" ").append(dayFormat.format(msgCalendar.getTime())).toString();
                } else if (isTheDayBeforeYesterDay(nowCalendar, msgCalendar)) {
                    if (dayFormat == null) {
                        dayFormat = new SimpleDateFormat("HH:mm");
                    }
                    return new StringBuilder(THE_DAY_BEFORE_YESTER_DAY).append(" ").append(dayFormat.format(msgCalendar.getTime())).toString();
                } else {
                    if (dateFormat == null) {
                        dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    }
                    return new StringBuilder(dateFormat.format(msgCalendar.getTime())).toString();
                }
            } else if (calDay / 31 >= 12 || !isSameYear(nowCalendar, msgCalendar)) {
                if (yearFormat == null) {
                    yearFormat = new SimpleDateFormat(YEAR_FORMAT);
                }
                return yearFormat.format(msgCalendar.getTime());
            } else {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(DATE_FORMAT);
                }
                return dateFormat.format(msgCalendar.getTime());
            }
        }
        if (dayFormat == null) {
            dayFormat = new SimpleDateFormat("HH:mm");
        }
        return TODAY + " " + dayFormat.format(msgCalendar.getTime());
    }

    private static boolean isSameHalfDay(Calendar now, Calendar msg) {
        int nowHour = now.get(Calendar.HOUR);
        int msgHOur = msg.get(Calendar.HOUR);
        if (((msgHOur <= 12 ? 1 : 0) & (nowHour <= 12 ? 1 : 0)) != 0) {
            return true;
        }
        int i;
        if (nowHour >= 12) {
            i = 1;
        } else {
            i = 0;
        }
        if (((msgHOur >= 12 ? 1 : 0) & i) == 0) {
            return false;
        }
        return true;
    }

    private static boolean isSameDay(Calendar now, Calendar msg) {
        return now.get(Calendar.DAY_OF_YEAR) == msg.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isYesterDay(Calendar now, Calendar msg) {
        if (now.get(Calendar.DAY_OF_YEAR) - msg.get(Calendar.DAY_OF_YEAR) == 1) {
            return true;
        }
        return false;
    }

    private static boolean isTheDayBeforeYesterDay(Calendar now, Calendar msg) {
        return now.get(Calendar.DAY_OF_YEAR) - msg.get(Calendar.DAY_OF_YEAR) == 2;
    }

    private static boolean isSameYear(Calendar now, Calendar msg) {
        if (now.get(Calendar.YEAR) == msg.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }
}
