package com.example.qrcodesample1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkActivity extends AppCompatActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok);
        ButterKnife.bind(this);
        mHandler = new Handler();
    }

    @OnClick({R.id.btn_get_sync, R.id.btn_get_Async, R.id.btn_post_sync, R.id.btn_post_Async})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_sync:     //日志拦截器  用来打印http请求的状态
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                //给日志拦截器设置打印级别
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                // TODO: 2019/8/31  1 构造 OkHttpClient
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .addInterceptor(httpLoggingInterceptor)
                        .build();
                // TODO: 2019/8/31  1 构造 Request
                Request request = new Request.Builder()
                        .get()
                        .url("http://172.17.8.100/small/commodity/v1/commodityList")
                        //.header("key","value")   添加请求头
                        .build();
                // TODO: 2019/8/31  3 将Request转成成Call
                final Call call = okHttpClient.newCall(request);

                // TODO: 2019/8/31  4执行get的   同步请求     execute
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //响应结果
                        Response response = null;
                        try {
                            response = call.execute();
                            // TODO: 2019/8/31 通过这行代码来判断是否请求成功
                            if (response != null && response.isSuccessful()) {

                                // TODO: 2019/8/31 拿到封装json的响应体
                                ResponseBody responseBody = response.body();

                                // TODO: 2019/8/31 通过string（） 方法，拿到json
                                String json = responseBody.string();
                                final ProductBean productBean = new Gson().fromJson(json, ProductBean.class);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(OkActivity.this, "请求成功" + productBean.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();

                        }

                    }
                }).start();


                break;
            case R.id.btn_get_Async:
                // TODO: 2019/8/31  1 构造 OkHttpClient
                OkHttpClient okHttpClient1 = new OkHttpClient.Builder()
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();
                // TODO: 2019/8/31  1 构造 Request
                Request request1 = new Request.Builder()
                        .get()
                        .url("http://172.17.8.100/small/commodity/v1/commodityList")
                        //.header("key","value")   添加请求头
                        .build();
                // TODO: 2019/8/31  3 将Request转成成Call
                final Call call1 = okHttpClient1.newCall(request1);

                // TODO: 2019/8/31 enqueue  异步请求   会自动帮我们切到子线程，但是结果回调还是子线程
                call1.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response != null && response.isSuccessful()) {
                            String json = response.body().string();
                            final ProductBean productBean = new Gson().fromJson(json, ProductBean.class);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OkActivity.this, "请求成功" + productBean.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });

                break;
            case R.id.btn_post_sync:
                // TODO: 2019/8/31  1 构造 OkHttpClient
                OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build();
                // TODO: 2019/8/31  1 构造 Request
                FormBody formBody = new FormBody.Builder()
                        .add("phone", "15501186623")
                        .add("pwd", "123456")
                        .build();

                Request request2 = new Request.Builder()
                        .post(formBody)
                        .url("http://172.17.8.100/small/user/v1/login")
                        //.header("key","value")   添加请求头
                        .build();
                // TODO: 2019/8/31  3 将Request转成成Call
                final Call call2 = okHttpClient2.newCall(request2);

                call2.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(OkActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response != null && response.isSuccessful()) {
                            String json = response.body().string();
                            final LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                            if ("0000".equals(loginBean.getStatus())) {

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(OkActivity.this, "登录成功" + loginBean, Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } else {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(OkActivity.this, "登录失败 " + loginBean.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OkActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                break;
            case R.id.btn_post_Async:

//                OkhttpUtil.getInstance().doGet("http://172.17.8.100/small/commodity/v1/commodityList", new OkhttpUtil.OkhttpCallBack() {
//                    @Override
//                    public void onSuccess(String json) {
//                        Toast.makeText(OkActivity.this, "请求成功" +json, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//
//                    }
//                });


                Map<String, String> map = new HashMap<>();
                map.put("phone", "15501186623");
                map.put("pwd", "123456");
                OkhttpUtil.getInstance().doPost("http://172.17.8.100/small/user/v1/login", map, new OkhttpUtil.OkhttpCallBack() {
                    @Override
                    public void onSuccess(String json) {

                        LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                        //发送
                        EventBus.getDefault().postSticky(loginBean);
                        Toast.makeText(OkActivity.this, "post成功" + json, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OkActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(OkActivity.this, "post失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
