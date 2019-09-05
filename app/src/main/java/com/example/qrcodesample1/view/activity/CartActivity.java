package com.example.qrcodesample1.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.CartBean;
import com.example.qrcodesample1.utils.RetrofiManager;
import com.example.qrcodesample1.view.adapter.CartAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.el_cart)
    ExpandableListView mElCart;
    @BindView(R.id.cb_cart_all_select)
    CheckBox mCbCartAllSelect;
    @BindView(R.id.tv_cart_total_price)
    TextView mTvCartTotalPrice;
    @BindView(R.id.btn_cart_pay)
    Button mBtnCartPay;

    //所有商家的集合
    List<CartBean.ResultBean> mSellerList = new ArrayList<>();
    private CartAdapter mCartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        mCartAdapter = new CartAdapter(mSellerList);
        mElCart.setAdapter(mCartAdapter);


        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("userId", "8112");
        headersMap.put("sessionId", "15676676814698112");
        RetrofiManager.getInstance()
                .create()
                .getCartInfo(headersMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CartBean>() {
                    @Override
                    public void accept(CartBean cartBean) throws Exception {
                        if (cartBean != null && "0000".equals(cartBean.getStatus())) {
                            mSellerList = cartBean.getResult();
                            //刷新适配器
                            mCartAdapter.setData(mSellerList);

                            // TODO: 2019/9/5 展开二级列表
                            for (int i = 0; i <mSellerList.size() ; i++) {
                                //展开对应的组
                                mElCart.expandGroup(i);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @OnClick({R.id.cb_cart_all_select, R.id.btn_cart_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_cart_all_select:
                // TODO: 2019/9/5 点击了全选按钮
                // TODO: 2019/9/5  改变所有商品的状态
                // TODO: 2019/9/5  重新计算总价
                // TODO: 2019/9/5 重新计算总数量

                break;
            case R.id.btn_cart_pay:
                Toast.makeText(CartActivity.this, "跳转支付页面", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
