package com.example.qrcodesample1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {

    @BindView(R.id.btn_get_sync)
    Button mBtnGetSync;
    @BindView(R.id.btn_get_Async)
    Button mBtnGetAsync;
    @BindView(R.id.btn_post_sync)
    Button mBtnPostSync;
    @BindView(R.id.btn_post_Async)
    Button mBtnPostAsync;
    private IApi iApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor) //日志拦截器
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.17.8.100/")   //base_url必须带斜杠
                .client(okhttpClient)   //设置okhttpClient
                .addConverterFactory(GsonConverterFactory.create()) //设置Gson转换器，让Retofit支持gson转换
                .build();

        //构造一个IApi接口对象
        iApi = retrofit.create(IApi.class);

    }

    @OnClick({R.id.btn_get_sync, R.id.btn_get_Async, R.id.btn_post_sync, R.id.btn_post_Async})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_sync:

                Call<ProductBean> productBeanCall = iApi.getProductList();


                productBeanCall.enqueue(new Callback<ProductBean>() {  //异步请求
                    @Override
                    public void onResponse(Call<ProductBean> call, Response<ProductBean> response) {
                        if (response != null && response.isSuccessful()) {
                            ProductBean productBean = response.body();
                            Toast.makeText(RetrofitActivity.this, "请求成功" + productBean.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            // TODO: 2019/9/4 失败
                            Toast.makeText(RetrofitActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductBean> call, Throwable t) {
                        Toast.makeText(RetrofitActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.btn_get_Async:
                Call<LoginBean> loginBeanCall = iApi.login("15501186623", "123456");
                loginBeanCall.enqueue(new Callback<LoginBean>() {
                    @Override
                    public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                        if (response != null && response.isSuccessful()) {
                            LoginBean loginBean = response.body();
                            if ("0000".equals(loginBean.getStatus())) {
                                Toast.makeText(RetrofitActivity.this, "请求成功" + loginBean.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RetrofitActivity.this, "请求失败" + loginBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        Toast.makeText(RetrofitActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<LoginBean> call, Throwable t) {
                        Toast.makeText(RetrofitActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_post_sync:
                IApi iApi = RetrofiManager.getInstance().create();
                Call<LoginBean> loginBeanCall1 = iApi.login("15501186623", "123456");
                loginBeanCall1.enqueue(new Callback<LoginBean>() {
                    @Override
                    public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                        if (response != null && response.isSuccessful()) {
                            LoginBean loginBean = response.body();
                            if ("0000".equals(loginBean.getStatus())) {
                                Toast.makeText(RetrofitActivity.this, "请求成功" + loginBean.toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RetrofitActivity.this, "请求失败" + loginBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        Toast.makeText(RetrofitActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<LoginBean> call, Throwable t) {
                        Toast.makeText(RetrofitActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.btn_post_Async:
                break;
        }
    }
}
