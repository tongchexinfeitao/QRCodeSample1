package com.example.qrcodesample1.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.qrcodesample1.R;
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
        OrderFormFragment orderFragment = new OrderFormFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_order,orderFragment)
                .commit();
    }
}
