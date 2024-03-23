package com.example.narratives.regislogin;

import static com.example.narratives.regislogin.RetrofitInterface.URL_BASE;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String userCookie;

    public static Retrofit getLoginRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(URL_BASE)
                .build();
        return retrofit;
    }

    public static Retrofit getRetrofit(){

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL_BASE)
                .build();

        return retrofit;
    }

    public static RetrofitInterface getRetrofitInterface(){

        RetrofitInterface retrofitInterface = getRetrofit().create(RetrofitInterface.class);

        return retrofitInterface;
    }

    public static String getUserCookie() {
        return userCookie;
    }

    public static void setUserCookie(String cookie) {
        userCookie = cookie;
    }
}


