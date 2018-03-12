package x_ware.com.edl.helpers;

/**
 * Created by buneavros on 3/7/18.
 */

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
}
