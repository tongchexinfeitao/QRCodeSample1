package com.example.qrcodesample1;

import android.os.Handler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkhttpUtil {
    private static OkhttpUtil mOkhttpUtil = new OkhttpUtil();
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;


    public static OkhttpUtil getInstance() {
        return mOkhttpUtil;
    }

    public OkhttpUtil() {
        //日志拦截器  用来打印http请求的状态
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //给日志拦截器设置打印级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // TODO: 2019/8/31  1 构造 OkHttpClient
        mOkHttpClient = new OkHttpClient.Builder()
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();

        mHandler = new Handler();
    }

    public void doGet(String url, final OkhttpCallBack okhttpCallBack) {
        // TODO: 2019/8/31  1 构造 Request
        Request request1 = new Request.Builder()
                .get()
                .url(url)
                //.header("key","value")   添加请求头
                .build();
        // TODO: 2019/8/31  3 将Request转成成Call
        final Call call = mOkHttpClient.newCall(request1);

        // TODO: 2019/8/31 enqueue  异步请求   会自动帮我们切到子线程，但是结果回调还是子线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    final String json = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (okhttpCallBack != null) {
                                okhttpCallBack.onSuccess(json);
                            }
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (okhttpCallBack != null) {
                                okhttpCallBack.onFailure(new Exception("请求失败"));
                            }
                        }
                    });
                }

            }
        });
    }

    public interface OkhttpCallBack {
        void onSuccess(String json);

        void onFailure(Throwable throwable);
    }

}
