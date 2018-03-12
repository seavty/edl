package x_ware.com.edl.helpers;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by buneavros on 3/7/18.
 */

public class ProgressDialogHelper {

    public static ProgressDialog getInstance(Context context) {
        ProgressDialog progress;
        progress = new ProgressDialog(context);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        return progress;
    }
}
