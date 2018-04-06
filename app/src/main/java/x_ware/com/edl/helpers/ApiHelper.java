package x_ware.com.edl.helpers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import x_ware.com.edl.modules.auth.LoginActivity;

/**
 * Created by buneavros on 3/7/18.
 */

public final class ApiHelper {

    public static void unableConnectToServer(Activity activity, String TAG, Throwable t){
        Log.d(TAG,  t.toString());
        Toast.makeText(activity, "Unable connect to server! Please try again!" , Toast.LENGTH_SHORT).show();
    }

    public static boolean isSuccessful(Activity activity, int statusCode){
        switch (statusCode) {
            case 200:
                return true;

            case 401:
                Toast.makeText(activity, "Session expired. Please login again!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
                return false;

            case 500:
                Toast.makeText(activity, "Error occurred while processing your request! Please try again!", Toast.LENGTH_SHORT).show();
                return false;

            default:
                Toast.makeText(activity, "Error occurred while processing your request! Please try again" , Toast.LENGTH_SHORT).show();
                return false;
        }
    }
}
