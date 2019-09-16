package com.example.qrcodesample1.week;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.OrderFormBean;
import com.example.qrcodesample1.utils.RetrofiManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderFormWeekFragment extends Fragment {


    @BindView(R.id.rv_order)
    RecyclerView mRvOrder;
    private List<OrderFormBean.OrderListBean> orderListBeans = new ArrayList<>();
    private OrderFormWeekAdapter mOrderFormWeekAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.frgment_order_form_week_layout, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mOrderFormWeekAdapter = new OrderFormWeekAdapter(orderListBeans);
        mRvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvOrder.setAdapter(mOrderFormWeekAdapter);

        //请求头
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("userId", "8112");
        headersMap.put("sessionId", "15686223489518112");

        //get的查询参数
        Map<String, Integer> paramsMap = new HashMap<>();
        paramsMap.put("status", 0);
        paramsMap.put("page", 1);
        paramsMap.put("count", 10);

        RetrofiManager.getInstance().create()
                .getOrderFormInfo(headersMap, paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderFormBean>() {
                    @Override
                    public void accept(OrderFormBean orderFormBean) throws Exception {
                        if ("0000".equals(orderFormBean.getStatus())) {
                            mOrderFormWeekAdapter.setData(orderFormBean.getOrderList());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
