package x_ware.com.edl.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import x_ware.com.edl.modules.auth.LoginActivity;

/**
 * Created by buneavros on 3/7/18.
 */

public class ApiErrorHelper {

    public static void unableConnectToServer(Context context, String TAG, Throwable t){
        Log.d(TAG,  t.toString());
        Toast.makeText(context, "Unable connect to server." , Toast.LENGTH_SHORT).show();
    }

    public static void statusCode500(Context context){
        Toast.makeText(context, "Error occurred while processing your request!" , Toast.LENGTH_SHORT).show();
    }

    public static void statusCode404(Context context){
        Toast.makeText(context, "Not found!" , Toast.LENGTH_SHORT).show();
    }

    public static void statusCode401(Activity activity){
        Toast.makeText(activity, "Session expired. Please login again!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
