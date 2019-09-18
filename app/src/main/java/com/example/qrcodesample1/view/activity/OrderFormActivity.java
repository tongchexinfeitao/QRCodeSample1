package com.example.qrcodesample1.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.mycircle.MyCircleFragment;
import com.example.qrcodesample1.uploadPhoto.UpLoadPhotoForPushCircleFragment;
import com.example.qrcodesample1.view.fragment.OrderFormFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFormActivity extends AppCompatActivity {

    @BindView(R.id.fl_order)
    FrameLayout mFlOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form_ativity);
        ButterKnife.bind(this);
        //构造5个fragment
        OrderFormFragment allOrderFragment = OrderFormFragment.creat(0);
        OrderFormFragment waitPayOrderFragment = OrderFormFragment.creat(1);
        OrderFormFragment waitReiceiveOrderFragment = OrderFormFragment.creat(2);
        OrderFormFragment evaluateOrderFragment = OrderFormFragment.creat(3);
        OrderFormFragment completeOrderFragment = OrderFormFragment.creat(9);

        UpLoadPhotoForPushCircleFragment upLoadPhotoForPushCircleFragment = new UpLoadPhotoForPushCircleFragment();

        MyCircleFragment myCircleFragment = new MyCircleFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_order,myCircleFragment)
                .commit();
    }
}
