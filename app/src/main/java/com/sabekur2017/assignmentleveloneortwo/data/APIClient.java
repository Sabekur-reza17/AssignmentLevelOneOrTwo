package com.sabekur2017.assignmentleveloneortwo.data;




import com.google.gson.GsonBuilder;
import com.sabekur2017.assignmentleveloneortwo.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BuildConfig.FIELD_BUZZ_BASE_URL)
                    .addConverterFactory(GsonConverterFactory
                            .create(new GsonBuilder()
                                    .setLenient()
                                    .serializeNulls()
                                    .create()));


    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG && !httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
        }
        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
    /*public APIClient() {
    }

    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.FIELD_BUZZ_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiInterface getApiService() {
        return getRetrofit()
                .create(ApiInterface.class);
    }

    private Retrofit getRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.FIELD_BUZZ_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiInterface getApiService(OkHttpClient client) {
        return getRetrofit(client)
                .create(ApiInterface.class);
    }*/
}
