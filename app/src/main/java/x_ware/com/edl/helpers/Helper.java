package x_ware.com.edl.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by buneavros on 2/21/18.
 */

public class Helper {

    public static void createToast(Context context, String msg){
        Toast.makeText(context, msg , Toast.LENGTH_SHORT).show();
    }



    public static void toastMsg(Context con, String msg){
        Toast.makeText(con, msg , Toast.LENGTH_SHORT).show();
    }

    public static void reuqestError(Context con, String TAG, Throwable t){
        Log.d(TAG, "reuqestError: " + t.getMessage());
        Log.d(TAG, "reuqestError: " + t.getStackTrace());
        Log.d(TAG, "reuqestError: " + t.getLocalizedMessage());
        Toast.makeText(con, "Error occurred while processing your request!" , Toast.LENGTH_SHORT).show();
    }


    public static void notFound404(Context con){
        Toast.makeText(con, "Not found!" , Toast.LENGTH_SHORT).show();
    }

    public static void error500(Context context){
        Toast.makeText(context, "Error occurred while processing your request!" , Toast.LENGTH_SHORT).show();
    }



    public static void noRecordToast(Context con) {
        Toast.makeText(con, "No record" , Toast.LENGTH_SHORT).show();
    }




    public static String YYYYMMDD(int year, int month, int day){
        String m = ( month < 9) ? "0" + month: month + "";
        String d = (day  < 9) ? "0" + day: day +"";
        return year + "-" + m + "-" + d;
    }


    public static String DDMMYYYY(int year, int month, int day){
        String m = ( month < 10) ? "0" + month: month + "";
        String d = (day  < 10) ? "0" + day: day +"";
        return d + "-" + m + "-" + year;
    }

    public static String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar now = Calendar.getInstance();
        String nowDate= sdf.format(now.getTime());
        return nowDate;
    }



    public static String getCurrentDateOnly(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar now = Calendar.getInstance();
        String nowDate= sdf.format(now.getTime());
        return nowDate;
    }


}
