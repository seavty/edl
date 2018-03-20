package x_ware.com.edl.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by buneavros on 3/20/18.
 */

public class ImageHelper {

    //compress format public static variables
    public static final Bitmap.CompressFormat JPEG = Bitmap.CompressFormat.JPEG;
    public static final Bitmap.CompressFormat PNG = Bitmap.CompressFormat.PNG;
    public static final Bitmap.CompressFormat WEBP = Bitmap.CompressFormat.WEBP;
    //================================================================================
    // Conversion Methods
    //================================================================================

    public static Bitmap resizeImageRunTime(byte[] byteArray, int width, int height, boolean isFilter){
        Bitmap b = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return Bitmap.createScaledBitmap(b, width, height, isFilter);
    }

    //region Conversion Methods
    public static byte[] bitmapToBytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap bytesToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public static Bitmap bytesToBitmap(byte[] byteArray, Bitmap.CompressFormat format) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, 100, stream);
        return bitmap;
    }

    public static String bytesToStringBase64(byte[] byteArray) {
        StringBuilder base64 = new StringBuilder(Base64.encodeToString(byteArray, Base64.DEFAULT));
        return base64.toString();
    }

    public static byte[] stringBase64ToBytes(String stringBase64) {
        byte[] byteArray = Base64.decode(stringBase64, Base64.DEFAULT);
        return byteArray;
    }
    //endregion
}
