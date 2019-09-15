package com.example.qrcodesample1.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.OrderFormBean;
import com.example.qrcodesample1.utils.RetrofiManager;
import com.example.qrcodesample1.view.adapter.OrderFormAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderFormFragment extends Fragment {

    /**
     * 根据订单类型创建一个OrderFormFragment对象
     */
    public static OrderFormFragment creat(int status) {
        OrderFormFragment orderFormFragment = new OrderFormFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        orderFormFragment.setArguments(bundle);
        return orderFormFragment;
    }


    @BindView(R.id.xrv_orderForm)
    XRecyclerView mXrvOrderForm;

    List<OrderFormBean.OrderListBean> orderListBeans = new ArrayList<>();
    private OrderFormAdapter mOrderFormAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.frgment_order_form_layout, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //从budle中取出状态值
        Bundle bundle = getArguments();
        int status = bundle.getInt("status");


        mOrderFormAdapter = new OrderFormAdapter(orderListBeans);
        //设计订单的一些点击监听
        mOrderFormAdapter.setOnOrderFormClickListener(new OrderFormAdapter.onOrderFormClickListener() {
            @Override
            public void onGoPay(String orderId) {
                // TODO: 2019/9/15 调用支付的接口
            }

            @Override
            public void onCancelOrder(String orderId) {
                // TODO: 2019/9/15 调用取消订单接口
            }
        });


        mXrvOrderForm.setLayoutManager(new LinearLayoutManager(getContext()));
        mXrvOrderForm.setAdapter(mOrderFormAdapter);

        //请求头
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("userId", "8112");
        headersMap.put("sessionId", "15685191578528112");

        //get的查询参数
        Map<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("status", status);
        paramsMap.put("page", 1);
        paramsMap.put("count", 10);
        RetrofiManager.getInstance().create()
                .getOrderFormInfo(headersMap, paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderFormBean>() {
                    @Override
                    public void accept(OrderFormBean orderFormBean) throws Exception {
                        if (orderFormBean != null && "0000".equals(orderFormBean.getStatus())) {
                            Toast.makeText(OrderFormFragment.this.getContext(), "订单获取成功", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "accept : " + orderFormBean.toString());
                            mOrderFormAdapter.setData(orderFormBean.getOrderList());
                        } else {
                            Toast.makeText(OrderFormFragment.this.getContext(), "订单获取失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "error : " + throwable.toString());

                    }
                });
    }
}
