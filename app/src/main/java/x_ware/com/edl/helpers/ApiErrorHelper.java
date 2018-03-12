package x_ware.com.edl.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by buneavros on 3/7/18.
 */

public class ApiErrorHelper {

    public static void unableConnectToServer(Context context, String TAG, Throwable t){
        Log.d(TAG,  t.toString());
        Toast.makeText(context, "Unable connect to server." , Toast.LENGTH_SHORT).show();
    }

    public static void error500(Context context){
        Toast.makeText(context, "Error occurred while processing your request!" , Toast.LENGTH_SHORT).show();
    }
}
