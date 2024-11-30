package com.example.datn_toystoryshop.Server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    //chỉ cần nhập IPv4 trong APIService
    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(APIService.BASE_URL) // Sử dụng BASE_URL từ APIService
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static APIService getAPIService() {
        return getInstance().create(APIService.class);
    }
}
