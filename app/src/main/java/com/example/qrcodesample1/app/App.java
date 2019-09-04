package com.example.qrcodesample1.app;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //zxing初始化
        ZXingLibrary.initDisplayOpinion(this);
    }
}
