package com.example.qrcodesample1.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.LoginBean;
import com.example.qrcodesample1.bean.ProductBean;
import com.example.qrcodesample1.utils.RetrofiManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class RxActivity extends AppCompatActivity {

    @BindView(R.id.btn_get_sync)
    Button mBtnGetSync;
    @BindView(R.id.btn_get_Async)
    Button mBtnGetAsync;
    @BindView(R.id.btn_post_sync)
    Button mBtnPostSync;
    @BindView(R.id.btn_post_Async)
    Button mBtnPostAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_get_sync, R.id.btn_get_Async, R.id.btn_post_sync, R.id.btn_post_Async})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_sync:
                //使用map操作符转换数据类型， 使用filter操作符过滤有用数据
                Map<String, String> paramasMap = new HashMap<>();
                paramasMap.put("phone", "15501186623");
                paramasMap.put("pwd", "123456");
                RetrofiManager.getInstance()
                        .create()
                        .loginForRxJava(paramasMap)
                        .subscribeOn(Schedulers.io()) //在子线程订阅，耗时操作放到子线程
                        .observeOn(AndroidSchedulers.mainThread())//在主线程接受成功和失败
                        .filter(new Predicate<LoginBean>() {
                            @Override
                            public boolean test(LoginBean loginBean) throws Exception {
                                return "0000".equals(loginBean.getStatus());
                            }
                        })
                        .map(new Function<LoginBean, LoginBean.ResultBean>() {
                            @Override
                            public LoginBean.ResultBean apply(LoginBean loginBean) throws Exception {
                                return loginBean.getResult();
                            }
                        })
                        .map(new Function<LoginBean.ResultBean, String>() {
                            @Override
                            public String apply(LoginBean.ResultBean resultBean) throws Exception {
                                return resultBean.getPhone();
                            }
                        })
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.e("TAG", "onSubscribe");
                            }

                            @Override
                            public void onNext(String s) {
                                Log.e("TAG", "onNext");
                                Toast.makeText(RxActivity.this, "登录成功，手机号是"+s, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("TAG", "onError");
                            }

                            @Override
                            public void onComplete() {
                                Log.e("TAG", "onComplete");
                            }
                        });
                break;
            case R.id.btn_get_Async:
                //使用Timer操作符，做延时操作
                Observable.timer(3, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                Log.e("TAG", "onNext");
                                Toast.makeText(RxActivity.this, "accept", Toast.LENGTH_SHORT).show();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("TAG", "onError");
                            }
                        });
                break;
            case R.id.btn_post_sync:
                //使用flatmap  登录成功之后自动请求商品列表
                Map<String, String> paramasMap1 = new HashMap<>();
                paramasMap1.put("phone", "15501186623");
                paramasMap1.put("pwd", "123456");
                RetrofiManager.getInstance()
                        .create()
                        .loginForRxJava(paramasMap1)
                        .subscribeOn(Schedulers.io())
                        .filter(new Predicate<LoginBean>() {
                            @Override
                            public boolean test(LoginBean loginBean) throws Exception {
                                return "0000".equals(loginBean.getStatus());
                            }
                        })
                        .flatMap(new Function<LoginBean, ObservableSource<ProductBean>>() {
                            @Override
                            public ObservableSource<ProductBean> apply(LoginBean loginBean) throws Exception {
                                return RetrofiManager.getInstance().create().getProductListForRxJava();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ProductBean>() {
                            @Override
                            public void accept(ProductBean productBean) throws Exception {
                            Toast.makeText(RxActivity.this, "登录成功自动请求商品成功"+productBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("TAG", "错误" +throwable.toString());
                                Toast.makeText(RxActivity.this, "请求商品失败", Toast.LENGTH_SHORT).show();

                            }
                        });
                break;
            case R.id.btn_post_Async:
                RetrofiManager.getInstance().create()
                        .test1()
                        .subscribeOn(Schedulers.io()) //在子线程订阅，耗时操作放到子线程
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody responseBody) throws Exception {
                                Log.e("TAG", "成功" +responseBody.string());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("TAG", "throwable" +throwable.toString());

                            }
                        });
                break;
        }
    }
}
