package com.example.qrcodesample1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCAN = 100;
    private static final int REQUEST_CODE_OENG_IMAGE = 200;
    @BindView(R.id.tv_qr_content)
    TextView mTvQrContent;
    @BindView(R.id.btn_qr_scan)
    Button mBtnQrScan;
    @BindView(R.id.iv_qr_picture)
    ImageView mIvQrPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //butterknife必须绑定
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_qr_content, R.id.btn_qr_scan})
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
        }
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
        }

    }


}
