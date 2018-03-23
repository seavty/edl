package x_ware.com.edl.helpers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by buneavros on 3/23/18.
 */

public class CameraHelper {

    public static int REQUEST_TAKE_PHOTO = 701;

    public static Bitmap getCameraBitmap(Intent data, Activity activity) {
        Uri imageUri;
        String picturePath;
        imageUri = data.getData();

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(imageUri,
                                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }
}
