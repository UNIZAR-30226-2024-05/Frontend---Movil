package com.example.narratives._backend;

import static com.example.narratives._backend.RetrofitInterface.URL_BASE;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String userCookie;

    public static Retrofit getLoginRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(URL_BASE)
                .build();
    }

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL_BASE)
                .build();
    }

    public static RetrofitInterface getRetrofitInterface() {
        return getRetrofit().create(RetrofitInterface.class);
    }

    public static String getUserCookie() {
        return (userCookie == null) ? "" : userCookie;
    }

    public static void setUserCookie(String cookie) {
        userCookie = cookie;
    }
}


