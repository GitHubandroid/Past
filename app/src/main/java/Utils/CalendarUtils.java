package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SHLSY on 2015/6/1.
 */
public class CalendarUtils {
    public static SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");

    public static int get_between_days(Date date1,Date date2){
          return (int) ((date1.getTime()-date2.getTime())/(24*60*60*1000));
    }

    //几天前的日期
    public static Date getdatebyDays(int DAYS){
        Date today =new Date();
        Long Before_Date =(today.getTime()/1000)-60*60*24*DAYS;
        today.setTime(Before_Date*1000);
        return today;
    }

    public static String getDatebyDays(int DAYS){
        Date today =new Date();
        Long Before_Date =(today.getTime()/1000)-60*60*24*DAYS;
        today.setTime(Before_Date*1000);
        return format.format(today);
    }
    //根据日期生成date
    public static Date  getDateByCalendar(String year,String month,String day){
        Date date =null;
        try {
            date=format.parse(new StringBuffer().append(year).append("-").append(month).append("-").append(day).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
