package x_ware.com.edl.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by buneavros on 3/6/18.
 */

public final class PreferenceHelper {
    public static void setString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        //editor.apply();
        editor.commit();
    }

    public static <T extends Serializable> void setSerializeObject(Context context, String key, T value) {
        String json = new Gson().toJson(value);
        setString(context, key, json);
    }


    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public static <T extends Serializable> T getSerializeObject(Context context, String key, Type type) {
        return new Gson().fromJson(getString(context, key), type);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
}
