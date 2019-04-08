package com.vonallin.lib.base.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtil {

    public final static String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
    public final static String DATE_FORMAT_SOLIDUS = "yyyy/MM/dd";
    public final static String DATE_FORMAT_COMPACT = "yyyyMMdd";
    public final static String DATE_FORMAT_UTC_DEFAULT = "MM-dd-yyyy";
    public final static String DATE_FORMAT_UTC_SOLIDUS = "MM/dd/yyyy";
    public final static String DATE_FORMAT_COMPACT_MONTH = "yyyyMM";
    public final static String DATE_FORMAT_CHINESS_MIN = "MM月dd日HH时mm分";
    public final static String DATE_FORMAT_MIN = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT_CHINESE = "yyyy年MM月dd日";

    public final static String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_FORMAT_SOLIDUS = "yyyy/MM/dd HH:mm:ss";
    public final static String DATE_TIME_FORMAT_UTC_DEFAULT = "MM-dd-yyyy HH:mm:ss";
    public final static String DATE_TIME_FORMAT_UTC_SOLIDUS = "MM/dd/yyyy HH:mm:ss";
    public static final String DRAW_SEQUENCE_FORMAT = "yyyyMMddhhmmss";
    public static final String DRAW_SEQUENCE_FORMAT_2 = "yyyyMMddHHmmss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String DATE_FORMAT_DEFAULT_MONTH = "yyyy-MM";

    private static Map<String, String> dateFormatRegisterMap = new HashMap<String, String>();

    static {
        dateFormatRegisterMap.put(DATE_FORMAT_COMPACT, "^\\d{8}$");
        dateFormatRegisterMap.put(DATE_FORMAT_DEFAULT, "^\\d{4}-\\d{1,2}-\\d{1,2}$");
        dateFormatRegisterMap.put(DATE_FORMAT_SOLIDUS, "^\\d{4}/\\d{1,2}/\\d{1,2}$");
        dateFormatRegisterMap.put(DATE_FORMAT_UTC_DEFAULT, "^\\d{1,2}-\\d{1,2}-\\d{4}$");
        dateFormatRegisterMap.put(DATE_FORMAT_UTC_SOLIDUS, "^\\d{1,2}/\\d{1,2}/\\d{4}$");
        dateFormatRegisterMap.put(DATE_TIME_FORMAT_DEFAULT, "^\\d{4}-\\d{1,2}-\\d{1,2}\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}$");
        dateFormatRegisterMap.put(DATE_TIME_FORMAT_SOLIDUS, "^\\d{4}/\\d{1,2}/\\d{1,2}\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}$");
        dateFormatRegisterMap.put(DATE_TIME_FORMAT_UTC_DEFAULT, "^\\d{1,2}-\\d{1,2}-\\d{4}\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}$");
        dateFormatRegisterMap.put(DATE_TIME_FORMAT_UTC_SOLIDUS, "^\\d{1,2}/\\d{1,2}/\\d{4}\\s*\\d{1,2}:\\d{1,2}:\\d{1,2}$");
    }

    /**
     * 是否日期字符串
     * @param dateStr
     * @param formatString
     * @return
     */
    public static boolean isDateString(String dateStr, String formatString) {
        if (StringUtils.isEmpty(dateStr)) {
            return false;
        }
        try {
            new SimpleDateFormat(formatString).parse(dateStr);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 日期format
     * @param dateTemplate
     * @param date
     * @return
     */
    public static String format(String dateTemplate, Date date) {
        if (null == date || StringUtils.isEmpty(dateTemplate)) {
            return null;
        }
        return new SimpleDateFormat(dateTemplate).format(date);
    }

    /**
     * 字符串转成日期对象
     *
     * @param src
     * @return
     */
    public static Date parseDate(String src) {
        if (StringUtils.isEmpty(src)) {
            return null;
        }

        return parseDate(src, DATE_FORMAT_DEFAULT);
    }

    /**
     * 按指定template转换日期对象
     * @param src
     * @param dateTemplate
     * @return
     */
    public static Date parseDate(String src, String dateTemplate) {
        if (StringUtils.isEmpty(src)) {
            return null;
        }

        try {
            return new SimpleDateFormat(dateTemplate).parse(src);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("unsupported date template:%s", src), e);
        }
    }

    /**
     * 按指定模板输入当前日期
     * @param dateTemplate
     * @return
     */
    public static String getNow(String dateTemplate) {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat(dateTemplate);
        String sysDatetime = fmt.format(rightNow.getTime());
        return sysDatetime;
    }

    /**
     * 返回Long型日期
     * @return
     */
    public static Long generatePartitionKey() {
        String currentDate = getNow(DATE_FORMAT_COMPACT_MONTH);
        return  Long.valueOf(currentDate);
    }

    /**
     * 是否每月第一天
     * @param date
     * @return
     */
    public static boolean isFirstDayOfMonth(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int today = c.get(c.DAY_OF_MONTH);
        if(today ==1){
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @return
     */
    public static long getPartitionKeyPreMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT_COMPACT_MONTH);
        String sysDatetime = fmt.format(cal.getTime().getTime());
        return Long.valueOf(sysDatetime);
    }

    public static List<Long> getPartitionKeyBeforeMonth(int beforeMonth) {
        if(beforeMonth > 12){
            return new ArrayList<Long>();
        }
        List<Long> result = new ArrayList<Long>();
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT_COMPACT_MONTH);
        for(int i=0; i< beforeMonth; i++){
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -i);
            String sysDatetime = fmt.format(cal.getTime().getTime());
            result.add(Long.valueOf(sysDatetime));
        }

        return result;
    }


    /**
     * 相差的秒
     * @param start
     * @param end
     * @return
     */
    public static long diffSecond(Date start, Date end) {
        return render(end.getTime() - start.getTime(), 999, 1000);
    }

    /**
     * 相差的分钟
     * @param end
     * @return
     */
    public static long diffMinute(Date end) {
        return diffMinute(new Date(System.currentTimeMillis()), end);
    }

    /**
     * 相差的分钟
     * @param start
     * @param end
     * @return
     */
    public static long diffMinute(Date start, Date end) {
        return render(diffSecond(start, end), 59, 60);
    }

    /**
     * 相差的小时
     * @param start
     * @param end
     * @return
     */
    public static long diffHour(Date start, Date end) {
        return render(diffMinute(start, end), 59, 60);
    }

    /**
     * 相差的天数
     * @param start
     * @param end
     * @return
     */
    public static long diffDay(Date start, Date end) {
        return offset(start, end, Calendar.DAY_OF_YEAR);
    }

    /**
     * 相差的月
     * @param start
     * @param end
     * @return
     */
    public static long diffMonth(Date start, Date end) {
        return offset(start, end, Calendar.MONTH) + diffYear(start, end);
    }

    /**
     * 相差的年
     * @param start
     * @param end
     * @return
     */
    public static long diffYear(Date start, Date end) {
        Calendar s = Calendar.getInstance();
        Calendar e = Calendar.getInstance();

        s.setTime(start);
        e.setTime(end);

        return e.get(Calendar.YEAR) - s.get(Calendar.YEAR);
    }

    private static long render(long i, int j, int k) {
        return (i + (i > 0 ? j : -j)) / k;
    }

    private static long offset(Date start, Date end, int offsetCalendarField) {
        boolean bool = start.before(end);
        long rtn = 0;
        Calendar s = Calendar.getInstance();
        Calendar e = Calendar.getInstance();

        s.setTime(bool ? start : end);
        e.setTime(bool ? end : start);

        rtn -= s.get(offsetCalendarField);
        rtn += e.get(offsetCalendarField);

        while (s.get(Calendar.YEAR) < e.get(Calendar.YEAR)) {
            rtn += s.getActualMaximum(offsetCalendarField);
            s.add(Calendar.YEAR, 1);
        }

        return bool ? rtn : -rtn;
    }

    /**
     * 添加日期
     * @param date
     * @param n
     * @param calendarField
     * @return
     */
    public static Date add(Date date, int n, int calendarField) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, n);

        return c.getTime();
    }

    /**
     * 根据用户生日计算年龄
     */
    public static Integer getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();
        if(birthday == null){
            return null;
        }
        if (cal.before(birthday)) {
            return null;
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    /**
     * 根据生日获取年龄
     * @param birthday
     * @return
     */
    public static Integer getAgeByBirthday(String birthday) {
        Date date = parseDate(birthday);
        if (date == null) {
            return null;
        }
        return getAgeByBirthday(date);
    }


    /**
     * 获取年份的第一天
     * @param date
     * @return
     */
    public static Date startDayOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, 1);
        return startOfDay(c.getTime());
    }

    public static Date startOfDay(Date date) {
        return new DateTime(date).dayOfYear().roundFloorCopy().toDate();
    }

    public static String formatDateTime(Date date) {
        return (date == null) ? null : formatDate(date, DATE_TIME_FORMAT);
    }
    public static String formatDate(Date date, String dateTemplate) {
        if (date == null || StringUtils.isEmpty(dateTemplate)) {
            return null;
        }

        return getSimpleDateFormat(dateTemplate).format(date);
    }

    public static SimpleDateFormat getSimpleDateFormat(String dateTemplate) {
        /*synchronized (dateFormatMap) {
            if (!dateFormatMap.containsKey(dateTemplate)) {
                dateFormatMap.put(dateTemplate, new SimpleDateFormat(dateTemplate));
            }

            return dateFormatMap.get(dateTemplate);
        }*/
        return new SimpleDateFormat(dateTemplate);
    }

    public static String formatTimeToChiness(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long leavingMs = ms - day * dd - hour * hh - minute * mi;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            minute = leavingMs > 0?(minute+1):minute;
            //解决向上取整导致显示60分钟的问题
            if(minute == 60){
                minute = 59L;
            }
            sb.append(minute+"分");
        }else if(leavingMs > 0){
            sb.append("1分");
        }
        return sb.toString();
    }

    public static List<String> getMonthBetween(Date minDate, Date maxDate) {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    public static void main (String[] args){
        Date date = new Date();
       System.out.println(date.before(date));
        Date date2 =  DateUtil.parseDate("2018-06-11","yyyy-MM-dd");
        List<Long> list = getPartitionKeyBeforeMonth(6);
//        System.out.println(JSONObject.toJSON(list));

    }


    public static Date firstTimeOfDay(Date date) {
        DateTime dt = new DateTime(date.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        return dt.toDate();
    }

    public static Date addDate(Date date, int addNum) {
        DateTime dt = new DateTime(date.getTime()).plusDays(addNum);
        return dt.toDate();
    }

    public static boolean isBeforeDate(Date date) {
        return date.compareTo(new Date()) < 0;
    }
}
