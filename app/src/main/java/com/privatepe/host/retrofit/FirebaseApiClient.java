package com.privatepe.host.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseApiClient {

    public static final String BASE_URL = "https://fcm.googleapis.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient getRequestHeader() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(80, TimeUnit.SECONDS)
                .connectTimeout(80, TimeUnit.SECONDS)
                .writeTimeout(80, TimeUnit.SECONDS)

                .build();

        return httpClient;
    }
}

/*   .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request.Builder newRequest = request.newBuilder().header("Accept", "application/json");
                    return chain.proceed(newRequest.build());
                })*/