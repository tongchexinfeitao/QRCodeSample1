package com.example.qrcodesample1;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.camera.BitmapLuminanceSource;
import com.uuzuche.lib_zxing.decoding.DecodeFormatManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Hashtable;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCAN = 100;
    private static final int REQUEST_CODE_OENG_IMAGE = 200;
    @BindView(R.id.tv_qr_content)
    TextView mTvQrContent;
    @BindView(R.id.btn_qr_scan)
    Button mBtnQrScan;
    @BindView(R.id.iv_qr_picture)
    ImageView mIvQrPicture;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //butterknife必须绑定
        unbinder = ButterKnife.bind(this);
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}
                , 100);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //eventBus解注册
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void receiveLoginBean(LoginBean loginBean) {
        String nickName = loginBean.getResult().getNickName();
        Bitmap image = CodeUtils.createImage(nickName, 400, 400, null);
        mIvQrPicture.setImageBitmap(image);
    }


    @OnClick({R.id.tv_qr_content, R.id.btn_qr_scan, R.id.btn_qr_open_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_qr_content:
                String content = mTvQrContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    // TODO: 2019/8/30 生成不带头像的二维码
                    Bitmap qrBitmap = CodeUtils.createImage(content, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
                    mIvQrPicture.setImageBitmap(qrBitmap);
                }
                break;
            case R.id.btn_qr_scan:
                // TODO: 2019/8/30 打开相机扫一扫
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                break;
            case R.id.btn_qr_open_image:
                // TODO: 2019/8/30 打开相册
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("image/*");
                startActivityForResult(intent1, REQUEST_CODE_OENG_IMAGE);
                break;
        }
    }

    @OnLongClick(R.id.iv_qr_picture)
    public boolean onViewLongClicked(View view) {
        ImageView imageView = (ImageView) view;
        Drawable drawable = imageView.getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        MultiFormatReader multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        // 设置继续的字符编码格式为UTF8
        // hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        Result rawResult = null;
        try {
            rawResult = multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rawResult != null) {
            String result = rawResult.getText();
            Toast.makeText(MainActivity.this, "长按识别成功" + result, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this, "长按识别失败", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SCAN:
                if (data != null) {
                    //二维码的字符串存储在bundle中
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        // TODO: 2019/8/30 通过 RESULT_TYPE 去获取 结果码
                        int resultType = bundle.getInt(CodeUtils.RESULT_TYPE);
                        if (resultType == CodeUtils.RESULT_SUCCESS) {
                            // TODO: 2019/8/30 如果成功 ，通过 RESULT_STRING ，取出二维码中存储的信息
                            String resultString = bundle.getString(CodeUtils.RESULT_STRING);
                            Toast.makeText(MainActivity.this, "扫描成功" + resultString, Toast.LENGTH_SHORT).show();
                        } else if (resultType == CodeUtils.RESULT_FAILED) {
                            // TODO: 2019/8/30 失败提醒
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;

            //当请求码是200的时候，处理相册选择图片的结果
            case REQUEST_CODE_OENG_IMAGE:
                if (data != null) {
                    //图片的绝对路径封装在intent的Uri中
                    Uri uri = data.getData();
                    // TODO: 2019/8/31  利用ImageUtil将uri转成绝对路径，然后使用 CodeUtils.analyzeBitmap去解析图片
                    CodeUtils.analyzeBitmap(com.example.myapplication.ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(MainActivity.this, "解析图片成功" + result, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析图片失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //butterknife解绑
        unbinder.unbind();
        //presenter解绑
    }
}
