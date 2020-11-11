package com.pc.huangshan.utlis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    static Logger logger= LoggerFactory.getLogger(DateUtils.class);
    /**
     * 获取传入时间的 月的 一号
     * @param dateString
     * @param simpleDateFormat
     * @return
     */
     public static String getMonthFirstDateByDate(String dateString, String simpleDateFormat){
        SimpleDateFormat sdf=new SimpleDateFormat(simpleDateFormat);
         try {
             Date date = sdf.parse(dateString);
             Calendar calendar=Calendar.getInstance();
             calendar.setTime(date);
             int year = calendar.get(Calendar.YEAR);
             int month = calendar.get(Calendar.MONDAY);

             Calendar result=Calendar.getInstance();
             result.set(Calendar.YEAR,year);
             result.set(Calendar.MONDAY,month);
             result.set(Calendar.DAY_OF_MONTH,1);
             Date time = result.getTime();
             return sdf.format(time);
         } catch (ParseException e) {
             e.printStackTrace();
             logger.error(e.getMessage());
             return null;
         }
    }

    /**
     * 获取传入时间的 月的 最后一天
     * @param dateString
     * @param simpleDateFormat
     * @return
     */
    public static String getMonthLastDateByDate(String dateString, String simpleDateFormat){
        SimpleDateFormat sdf=new SimpleDateFormat(simpleDateFormat);
        try {
            Date date = sdf.parse(dateString);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); //获取当前月最后一天
            Date time = calendar.getTime();
            return sdf.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }



    /**
     * 获取传入时间的  **一月一号
     * @param dateString
     * @param simpleDateFormat
     * @return
     */
    public static String getYearFirstDateByDate(String dateString, String simpleDateFormat){
        SimpleDateFormat sdf=new SimpleDateFormat(simpleDateFormat);
        try {
            Date date = sdf.parse(dateString);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            Calendar result=Calendar.getInstance();
            result.set(Calendar.YEAR,year);
            result.set(Calendar.MONDAY,1);
            result.set(Calendar.DAY_OF_MONTH,1);
            Date time = result.getTime();
            return sdf.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }


    /**
     * 获取传入时间的  **12月31号
     * @param dateString
     * @param simpleDateFormat
     * @return
     */
    public static String getYearLastDateByDate(String dateString, String simpleDateFormat){
        SimpleDateFormat sdf=new SimpleDateFormat(simpleDateFormat);
        try {
            Date date = sdf.parse(dateString);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            Calendar result=Calendar.getInstance();
            result.set(Calendar.YEAR,year);
            result.set(Calendar.MONDAY,12);
            result.set(Calendar.DAY_OF_MONTH,31);
            Date time = result.getTime();
            return sdf.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }
}
