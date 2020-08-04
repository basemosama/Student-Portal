package com.basemosama.studentportal.network;


import com.basemosama.studentportal.BuildConfig;
import com.basemosama.studentportal.network.adapter.LiveDataCallAdapterFactory;
import com.basemosama.studentportal.utility.Constants;
import com.basemosama.studentportal.utility.DateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson));
    private static Retrofit.Builder predictionBuilder = new Retrofit.Builder()
            .baseUrl(Constants.PREDICTION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));
    private static OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
    private static OkHttpClient.Builder okHttpPredictionBuilder = new OkHttpClient.Builder();

    public static <T> T createService(Class<T> serviceClass) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            if (!okHttpBuilder.interceptors().contains(loggingInterceptor)) {
                okHttpBuilder.addInterceptor(loggingInterceptor);
            }
        }
        builder = builder.client(okHttpBuilder.build());
        Retrofit retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    public static <T> T createPredictionService(Class<T> serviceClass) {
        okHttpPredictionBuilder.connectTimeout(1, TimeUnit.MINUTES);
        okHttpPredictionBuilder.writeTimeout(1, TimeUnit.MINUTES);
        okHttpPredictionBuilder.readTimeout(1, TimeUnit.MINUTES);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            if (!okHttpPredictionBuilder.interceptors().contains(loggingInterceptor)) {
                okHttpPredictionBuilder.addInterceptor(loggingInterceptor);
            }
        }
        predictionBuilder = predictionBuilder.client(okHttpPredictionBuilder.build());
        Retrofit predictionRetrofit = predictionBuilder.build();
        return predictionRetrofit.create(serviceClass);
    }
}
