package com.sabekur2017.assignmentleveloneortwo.data;




import com.sabekur2017.assignmentleveloneortwo.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public APIClient() {
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
    }
}
