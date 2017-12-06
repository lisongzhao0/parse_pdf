package com.happy.gene.utility;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhaolisong on 06/06/2017.
 */
public class DateUtil {

    public static final String TIME_mmssSSS = "mmssSSS";
    public static final String TIME_HH_MM = "HH:mm:ss.SSS";
    public static final String DATE_YYYY_MM_DD_HH_MM_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_YYYY_MM_DD_HH_MM_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_YYYYMMDDHHMMssSSS = "yyyyMMddHHmmssSSS";
    public static final String DATE_YYYYMMDD = "yyyyMMdd";
    public static final String DATE_YYMMDD = "yyMMdd";

    public final static DateUtil newInstance() {
        return new DateUtil();
    }

    public long getHourMin(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_HH_MM);
        try {
            datetime += "00.000";
            Date time = sdf.parse(datetime);
            return time.getTime();
        } catch (Exception e) {
            return Long.MIN_VALUE;
        }
    }

    public Date getTime(String datetime, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date time = sdf.parse(datetime);
            return time;
        } catch (Exception e) {
            return null;
        }
    }

    public String timeToString(Date time, String pattern) {
        if (null==time) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.format(time);
        } catch (Exception e) {
            return "";
        }
    }

    public String timeToCn(Date date) {
        Calendar cal = Calendar.getInstance();
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day   = cal.get(Calendar.DAY_OF_MONTH);
        return year + "年" + (month>9 ? "" : " ") + month + "月" +(day>9 ? "" : " ") + day + "日";
    }

    /**
     * 判断起始时间中哪几天是距离今天已过去 workDays 个工作日
     * @param fromDates 起始时间
     * @param workDays  距今几个工作日以前
     * @return
     */
    public List<Boolean> workDays(List<Date> fromDates, int workDays) {
        List<Boolean> res = new ArrayList<>();
        if (null==fromDates || fromDates.isEmpty()) { return res; }

        Calendar now  = Calendar.getInstance();
        Calendar from = Calendar.getInstance();
        long     lNow = now.getTimeInMillis();
        long     lFrom= 0;
        int      f2n  = 0;
        int dayOfWeek = 0;

        for (Date f : fromDates) {
            if (null==f) { res.add(Boolean.FALSE); }

            from.setTime(f);
            lFrom     = from.getTimeInMillis();
            f2n       = 0;

            for (; lFrom < lNow; ) {
                from.add(Calendar.DAY_OF_YEAR, 1);
                dayOfWeek = from.get(Calendar.DAY_OF_WEEK);

                if (1 != dayOfWeek && 7 != dayOfWeek) { f2n++; }

                lFrom = from.getTimeInMillis();
            }

            res.add(f2n == workDays);
        }

        return res;
    }

    /**
     * 判断起始时间是否距离今天已过去 workDays 个工作日
     * @param fromDate  起始时间
     * @param workDays  距今几个工作日以前
     * @return
     */
    public boolean workDay(Date fromDate, int workDays) {
        if (null==fromDate) { return false; }

        List<Date> fromDates = Arrays.asList(new Date[]{fromDate});
        List<Boolean> res = workDays(fromDates, workDays);

        return null==res || res.isEmpty() ? false : res.get(0);
    }

    /**
     * 判断起始时间中哪几天是距离今天已过去等于超过 overdue 个工作日
     * @param fromDates 起始时间
     * @param overdue   距今逾期几个工作日
     * @return
     */
    public List<Boolean> workDaysOverdue(List<Date> fromDates, int overdue) {
        List<Boolean> res = new ArrayList<>();
        if (null==fromDates || fromDates.isEmpty()) { return res; }

        Calendar now  = Calendar.getInstance();
        Calendar from = Calendar.getInstance();
        long     lNow = now.getTimeInMillis();
        long     lFrom= 0;
        int      f2n  = 0;
        int dayOfWeek = 0;

        for (Date f : fromDates) {
            if (null==f) { res.add(Boolean.FALSE); }

            from.setTime(f);
            lFrom     = from.getTimeInMillis();
            f2n       = 0;

            for (; lFrom < lNow; ) {
                from.add(Calendar.DAY_OF_YEAR, 1);
                dayOfWeek = from.get(Calendar.DAY_OF_WEEK);

                if (1 != dayOfWeek && 7 != dayOfWeek) { f2n++; }

                lFrom = from.getTimeInMillis();
            }

            res.add(f2n >= overdue);
        }

        return res;
    }

    /**
     * 判断起始时间是否距离今天已过去等于超过 overdue 个工作日
     * @param fromDate  起始时间
     * @param overdue   距今逾期几个工作日
     * @return
     */
    public boolean workDayOverdue(Date fromDate, int overdue) {
        if (null==fromDate) { return false; }

        List<Date> fromDates = Arrays.asList(new Date[]{fromDate});
        List<Boolean> res = workDays(fromDates, overdue);

        return null==res || res.isEmpty() ? false : res.get(0);
    }

    /**
     * 判断今天是每月的第几周的星期几
     * @param week      每个月的第几周
     * @param weekDay   每周的第几天（1:周一 ...... 7:周日）
     * @return
     */
    public boolean isWeekAndDay(int week, int weekDay) {
        Calendar now  = Calendar.getInstance();
        int     iWeekDay = now.get(Calendar.DAY_OF_WEEK);
        int     iWeek    = now.get(Calendar.WEEK_OF_MONTH);

        if (1==iWeekDay) {
            iWeekDay = 7;
        }
        else if (2<=iWeekDay && iWeekDay<=7) {
            iWeekDay = iWeekDay -1;
        }

        return week==iWeek && weekDay==iWeekDay;
    }

    public static void main(String[] args) {

        System.out.println(DateUtil.newInstance().isWeekAndDay(2, 3));
        System.out.println(DateUtil.newInstance().isWeekAndDay(2, 4));
        System.out.println(DateUtil.newInstance().isWeekAndDay(2, 5));

        List<Date> fromDates = new ArrayList<>();
        Calendar from = null;
        for (int i = -7; i < 0; i ++) {
            from = Calendar.getInstance();
            from.add(Calendar.DAY_OF_YEAR, i);
            fromDates.add(from.getTime());
        }

        List<Boolean> res = DateUtil.newInstance().workDays(fromDates, 5);
        System.out.println(res);
    }
}
