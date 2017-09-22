package com.example.zengqiang.mycloud.utils;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zengqiang on 2017/8/29.
 */

public class TimeUtil {

    private static long timeCurrentMills;

    public static String getTranslateTime(String time){
        SimpleDateFormat sdf=new SimpleDateFormat("");
        Date nowTime=new Date();
        String currentTime=sdf.format(nowTime);
        Long currentTimeMills=nowTime.getTime();
        Date date=null;
        try {
            date=sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return currentTime;
        }
        if(date!=null){
            timeCurrentMills=date.getTime();
        }
        long timeMills=currentTimeMills-timeCurrentMills;
        if(timeMills<60000){
            return "刚刚";
        }

        if(timeMills<3600000){
            long timeMinute=timeMills/60000;
            int minute=(int)timeMinute%100;
            return minute+"分钟前";
        }
        long l = 24 * 60 * 60 * 1000;
        if(timeMills<l){
            long timeHour=l/3600000;
            int hour=(int)timeHour%24;
            return hour+"小时前";
        }
        if (timeMills >= l) {
            String currYear = currentTime.substring(0, 4);
            String year = time.substring(0, 4);
            if (!year.equals(currYear)) {
                return time.substring(0, 10);
            }
            return time.substring(5, 10);
        }
        return time;
    }

    public static String getData(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String date=format.format(new Date());
        return date;
    }

    public static ArrayList<String> getLastTime(String year,String month,String day){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
        int inDay=calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE,inDay-1);
        ArrayList<String> list=new ArrayList<>();
        list.add(calendar.get(Calendar.YEAR)+"");
        list.add(calendar.get(Calendar.MONTH)+1+"");
        list.add(calendar.get(Calendar.DATE)+"");
        return list;
    }

    public static boolean isRightTime(){
        Time time=new Time();
        time.setToNow();

        int hour=time.hour;
        int min=time.minute;
        if(hour>12||(min>30&&hour==12)){
            return true;
        }
        return false;
    }

}
