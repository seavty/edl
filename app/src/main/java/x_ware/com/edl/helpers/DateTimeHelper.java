package x_ware.com.edl.helpers;

/**
 * Created by buneavros on 3/7/18.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeHelper {

    public static String time_HH_MM(int hour, int minute){
        String hourStr = hour < 9 ? "0" + hour : hour + "";
        String minuteStr = minute < 9 ? "0" + minute : minute + "";
        return hourStr + ":" + minuteStr;
    }

    public static String convert_dd_mm_yyy_hh_mm_To_yyyy_mm_dd_hh_mm_ss(String dateTime){
        String[] dateTimeSpit = dateTime.split(" ");
        String[] dateSplit = dateTimeSpit[0].split("-");
        String dateStr = dateSplit[2] + "-" + dateSplit[1] + "-" + dateSplit[0];
        return dateStr + " " + dateTimeSpit[1] + ":00";
    }

    public static String convert_dd_mm_yyyy_To_yyyy_mm_dd(String date){
        String[] dataSplit = date.split("-");
        return dataSplit[2] + "-" + dataSplit[1] + "-" + dataSplit[0];
    }

    public static String convert_yyyy_mm_dd_t_hh_mm_ss_To_dd_mm_yyy_hh_mm(String s){
        if(s == null || s.equals(""))
            return "" ;
        String [] splitDateTime = s.split("T");

        String date[] = splitDateTime[0].split("-");
        String useDate = date[2] + "-" + date[1] + "-" + date[0];

        String time[] = splitDateTime[1].split(":");
        String useTime = time[0] + ":" + time[1];

        return useDate + " " + useTime;
    }

    public static String convert_yyyy_mm_dd_t_hh_mm_ss_To_hh_mm_With_am_pm(String s){
        if(s == null || s.equals(""))
            return "" ;
        String [] splitDateTime = s.split("T");

        String date[] = splitDateTime[0].split("-");
        String useDate = date[2] + "-" + date[1] + "-" + date[0];

        String time[] = splitDateTime[1].split(":");
        String useTime = time[0] + ":" + time[1];
        //return useTime;
        String amOrpm = "AM";
        int hour = Integer.parseInt(time[0]);
        String minute = time[1];

        if(hour >= 12) {
            amOrpm = "PM";
            if(hour > 12) {
                hour = Math.abs(hour - 12);
            }
        }

        String hourStr = ( hour < 9) ? "0" + hour: hour + "";
        //return hour + ":" + minute + " " +  amOrpm;
        return hourStr + ":" + minute + " " +  amOrpm;
    }

    public static String get_dd_mm_yyy(int year, int month, int day){
        String m = ( month < 10) ? "0" + month: month + "";
        String d = (day  < 10) ? "0" + day: day +"";
        return d + "-" + m + "-" + year;
    }

    public static String get_CurrentDate_dd_mm_yyyy(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar now = Calendar.getInstance();
        String nowDate= sdf.format(now.getTime());
        return nowDate;
    }

    public static String  get_yyyy_mm_dd(int year, int month, int day){
        String m = ( month < 9) ? "0" + month: month + "";
        String d = (day  < 9) ? "0" + day: day +"";
        return year + "-" + m + "-" + d;
    }
}
