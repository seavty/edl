package x_ware.com.edl.networking;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import x_ware.com.edl.helpers.PreferenceHelper;
import x_ware.com.edl.helpers.PreferenceKeyHelper;
import x_ware.com.edl.networking.dto.user.UserModel;

/**
 * Created by buneavros on 2/20/18.
 */
public class RetrofitProvider {

    //public static final String baseURL = "http://192.168.0.3/edl/api/v1/"; //local
    public static final String baseURL = "http://203.125.35.189/edl/api/v1/"; // SG server
    //public static final String baseURL = "http://47.74.183.123/edl/api/v1/"; //alibabar server

    public static Retrofit get() {
        //This interceptor log the http request and response
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //Set interceptor level to show log http header or http body
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    //overload function which consist of Context that we use it to user token
    public static Retrofit get(Context context) {
        UserModel user = PreferenceHelper.getSerializeObject(context, PreferenceKeyHelper.User, UserModel.class);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder().header("token", user.token);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);

        OkHttpClient client = httpClient.build();

        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }
}
