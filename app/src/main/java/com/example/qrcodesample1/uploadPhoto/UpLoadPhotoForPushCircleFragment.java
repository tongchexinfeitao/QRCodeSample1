package com.example.qrcodesample1.uploadPhoto;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.OrderFormBean;
import com.example.qrcodesample1.utils.RetrofiManager;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoFragment;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 多图上传
 */
public class UpLoadPhotoForPushCircleFragment extends TakePhotoFragment {
    @BindView(R.id.btn_take_photo)
    Button mBtnTakePhoto;
    @BindView(R.id.btn_push_circle)
    Button mBtnPushCircle;
    private Unbinder unbinder;

    private List<MultipartBody.Part> parts = new ArrayList<>();
    private int count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_upload_photo_layout, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 接受图片结果
     *
     *  绝对路径 -》 文件 -》 RequestyBody -》 Part  -》 添加到List<Part> ->调用接口
     *
     * @param result
     */
    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        //拿到单个图片的绝对路径
        String originalPath = image.getOriginalPath();
        //把路径转换成文件
        File file = new File(originalPath);
        count++;
        Toast.makeText(getContext(), "已选择" + count + "图片", Toast.LENGTH_SHORT).show();

        //图片类型          通过  parse 方法
        MediaType mediaType = MediaType.parse("image/*");
        //根据类型和文件构造一个请求体       通过  create 方法
        RequestBody requestBody = RequestBody.create(mediaType, file);
        //根据 key、文件名字 、请求体 构造一个 Part对象    通过  createFormData 方法
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        //添加到Part集合中
        parts.add(part);
    }


    @OnClick({R.id.btn_take_photo, R.id.btn_push_circle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                //获取TakePhoto对象
                TakePhoto takePhoto = getTakePhoto();
                //从图片库选择图片
                takePhoto.onPickFromGallery();
                break;
            case R.id.btn_push_circle:
                //请求头
                Map<String, String> headersMap = new HashMap<>();
                headersMap.put("userId", "8112");
                headersMap.put("sessionId", "15687766184668112");

                //参数
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("commodityId", 5);
                paramsMap.put("content", "给大家推荐一款手机");
                RetrofiManager.getInstance()
                        .create()
                        .uploadPhotoForPushCircl(headersMap, paramsMap, parts)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<OrderFormBean>() {
                            @Override
                            public void accept(OrderFormBean orderFormBean) throws Exception {
                                if ("0000".equals(orderFormBean.getStatus())) {
                                    Log.e("TAG", "发布成功" + orderFormBean.toString());
                                    Toast.makeText(getContext(), "发布成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("TAG", "发布失败");
                                Toast.makeText(getContext(), "发布失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                break;
        }
    }
}
