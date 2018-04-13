package x_ware.com.edl.helpers;

import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by buneavros on 4/13/18.
 */

public class StringHelper {
    public static String nullToEmptyString(String str) {
        if (str == null)
            return "";
        else
            return str;
    }

    public static Object tmp (Object obj) {

        if (obj == null)
            return obj;

        //obj.getClass()
        //obj.getClass().getDeclaredFields();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            Log.d("test", "tmp: " + field.getGenericType());
            Log.d("test", "tmp: " + field.getType());



        }
        return obj;


    }
}





