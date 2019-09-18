package com.example.qrcodesample1.takephoto;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.qrcodesample1.bean.OrderFormBean;
import com.example.qrcodesample1.utils.RetrofiManager;

import org.devio.takephoto.app.TakePhotoFragment;
import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MyTakePhotoFragment extends TakePhotoFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button button = new Button(getContext());
        button.setText("选择相册");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTakePhoto().onPickFromGallery();
            }
        });
        return button;
    }


    List<File> list = new ArrayList<>();
    List<MultipartBody.Part> parts = new ArrayList<>();

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        Log.e("TAG", "选择成功" + result.getImages().get(0).getOriginalPath());
        File file = new File(result.getImages().get(0).getOriginalPath());
        list.add(file);

        //请求头
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("userId", "8112");
        headersMap.put("sessionId", "15687725630878112");

        //get的查询参数
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("commodityId", 5);
        paramsMap.put("content", "给你推荐一个");

        for (int i = 0; i < list.size(); i++) {
            File file1 = list.get(i);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file1);
            MultipartBody.Part part = MultipartBody.Part.createFormData("image", file1.getName(), requestBody);
            parts.add(part);
        }
        
        RetrofiManager.getInstance().create()
                .pushCircle(headersMap, paramsMap, parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderFormBean>() {
                    @Override
                    public void accept(OrderFormBean orderFormBean) throws Exception {
                        Log.e("TAG", "发布成功" + orderFormBean);
                        Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "发布失败" + throwable.toString());
                        Toast.makeText(getContext(), "发布失败", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }
}
