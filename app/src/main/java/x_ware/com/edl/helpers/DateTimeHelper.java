package x_ware.com.edl.helpers;

/**
 * Created by buneavros on 3/7/18.
 */

import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelper {

    private static final String TAG = DateTimeHelper.class.getSimpleName();
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



    public static String convert_dd_MM_yyyy_EEEE_To_yyyy_MM_dd(String date) throws ParseException {
        DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy, EEEE", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        return targetFormat.format(originalFormat.parse(date));
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

    public static String convert_year_month_day_To_dd_MM_yyy_EEEE(int year, int month, int day) throws ParseException {
        String m = ( month < 10) ? "0" + month: month + "";
        String d = (day  < 10) ? "0" + day: day +"";
        String date = d + "-" + m + "-" + year;
        DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy, EEEE");
        return targetFormat.format(originalFormat.parse(date));
    }

    public static String get_CurrentDate_dd_mm_yyyy_With_DayName(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy, EEEE");
        Calendar now = Calendar.getInstance();
        return sdf.format(now.getTime());
    }


    public static String  get_yyyy_mm_dd(int year, int month, int day){
        String m = ( month < 9) ? "0" + month: month + "";
        String d = (day  < 9) ? "0" + day: day +"";
        return year + "-" + m + "-" + d;
    }
}
