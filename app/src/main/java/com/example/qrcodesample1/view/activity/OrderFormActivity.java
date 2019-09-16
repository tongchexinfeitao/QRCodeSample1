package com.example.qrcodesample1.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.view.fragment.OrderFormFragment;
import com.example.qrcodesample1.week.MyTitleView;
import com.example.qrcodesample1.week.OrderFormWeekFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFormActivity extends AppCompatActivity {

    @BindView(R.id.fl_order)
    FrameLayout mFlOrder;

    @BindView(R.id.my_title_view)
    MyTitleView myTitleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form_ativity);
        ButterKnife.bind(this);

        myTitleView.setmOnMyTitleViewClickListener(new MyTitleView.onMyTitleViewClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });



        //构造5个fragment
        OrderFormFragment allOrderFragment = OrderFormFragment.creat(0);
        OrderFormFragment waitPayOrderFragment = OrderFormFragment.creat(1);
        OrderFormFragment waitReiceiveOrderFragment = OrderFormFragment.creat(2);
        OrderFormFragment evaluateOrderFragment = OrderFormFragment.creat(3);
        OrderFormFragment completeOrderFragment = OrderFormFragment.creat(9);

        OrderFormWeekFragment orderFormWeekFragment = new OrderFormWeekFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_order,orderFormWeekFragment)
                .commit();
    }
}
