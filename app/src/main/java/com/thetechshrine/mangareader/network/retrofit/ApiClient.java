package com.thetechshrine.mangareader.network.retrofit;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static int timeOut = 60;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {

        if (okHttpClient == null) {
            initOkHttp(context);
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiInterface.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        return retrofit;
    }

    private static void initOkHttp(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();

                builder.addHeader("Accept", "application/json;charset=utf-8");
                builder.addHeader("Content-type", "application/json;charset=utf-8");

                Log.i("REQUEST URL", original.url().toString());

                Request request = builder.build();

                return chain.proceed(request);
            }
        });

        okHttpClient = httpClient.build();
    }
}