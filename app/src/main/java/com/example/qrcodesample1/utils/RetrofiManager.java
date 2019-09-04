package com.example.qrcodesample1.utils;

import com.example.qrcodesample1.IApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofiManager {
    private static RetrofiManager mRetrofiManager = new RetrofiManager();

    public static RetrofiManager getInstance() {
        return mRetrofiManager;
    }

    private static final String BASE_URL = "http://172.17.8.100/";
    private IApi iApi;

    public RetrofiManager() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor) //日志拦截器
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)   //base_url必须带斜杠
                .client(okhttpClient)   //设置okhttpClient
                .addConverterFactory(GsonConverterFactory.create()) //设置Gson转换器，让Retofit支持gson转换
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//设置支持Rxjava2
                .build();

        //构造一个IApi接口对象
        iApi = retrofit.create(IApi.class);
    }

    public IApi create() {
        return iApi;
    }
}
