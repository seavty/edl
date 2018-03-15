package x_ware.com.edl.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import x_ware.com.edl.adapters.appointment.AppointmentAdapter;
import x_ware.com.edl.interfaces.IRecyclerViewClickListener;
import x_ware.com.edl.modules.appointment.AppointmentDetailActivity;
import x_ware.com.edl.modules.auth.LoginActivity;
import x_ware.com.edl.networking.models.appointment.AppointmentViewModel;

/**
 * Created by buneavros on 3/7/18.
 */

public class ApiHelper {

    public static void unableConnectToServer(Activity activity, String TAG, Throwable t){
        Log.d(TAG,  t.toString());
        Toast.makeText(activity, "Unable connect to server." , Toast.LENGTH_SHORT).show();
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

            default:
                Toast.makeText(activity, "Error occurred while processing your request!" , Toast.LENGTH_SHORT).show();
                return false;
        }
    }
}
