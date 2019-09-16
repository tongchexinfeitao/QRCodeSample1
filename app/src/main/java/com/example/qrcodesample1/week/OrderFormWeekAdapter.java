package com.example.qrcodesample1.week;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.OrderFormBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFormWeekAdapter extends RecyclerView.Adapter<OrderFormWeekAdapter.OrderFormWeekViewHolder> {

    List<OrderFormBean.OrderListBean> orderListBeans = new ArrayList<>();

    public OrderFormWeekAdapter(List<OrderFormBean.OrderListBean> orderListBeans) {
        this.orderListBeans = orderListBeans;
    }

    @NonNull
    @Override
    public OrderFormWeekViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_form_week, viewGroup, false);
        return new OrderFormWeekViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderFormWeekViewHolder orderFormWeekViewHolder, int i) {
        OrderFormBean.OrderListBean orderListBean = orderListBeans.get(i);
        orderFormWeekViewHolder.mTvOrderSellerName.setText(orderListBean.getOrderId());
        switch (orderListBean.getOrderStatus()) {
            case 1:
                orderFormWeekViewHolder.mTvOrderStatus.setText("待付款");
                break;
            case 2:
                orderFormWeekViewHolder.mTvOrderStatus.setText("待收货");
                break;
            case 3:
                orderFormWeekViewHolder.mTvOrderStatus.setText("待评价");
                break;
            case 9:
                orderFormWeekViewHolder.mTvOrderStatus.setText("已完成");
                break;
        }
        orderFormWeekViewHolder.mBtnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(orderFormWeekViewHolder.itemView.getContext(), "取消订单", Toast.LENGTH_SHORT).show();
            }
        });
        orderFormWeekViewHolder.mBtnGoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(orderFormWeekViewHolder.itemView.getContext(), "去支付", Toast.LENGTH_SHORT).show();
            }
        });

        orderFormWeekViewHolder.orderFormWeekChildAdapter = new OrderFormWeekChildAdapter(orderListBean.getDetailList());
        orderFormWeekViewHolder.mRvOrder.setLayoutManager(new LinearLayoutManager(orderFormWeekViewHolder.itemView.getContext()));
        orderFormWeekViewHolder.mRvOrder.setAdapter(orderFormWeekViewHolder.orderFormWeekChildAdapter);
    }

    @Override
    public int getItemCount() {
        return orderListBeans == null ? 0 : orderListBeans.size();
    }

    public void setData(List<OrderFormBean.OrderListBean> orderList) {
        orderListBeans.clear();
        orderListBeans.addAll(orderList);
        notifyDataSetChanged();
    }


    class OrderFormWeekViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order_seller_name)
        TextView mTvOrderSellerName;
        @BindView(R.id.tv_order_status)
        TextView mTvOrderStatus;

        //商品的recyclerview
        @BindView(R.id.rv_order)
        RecyclerView mRvOrder;

        //商品的适配器
        OrderFormWeekChildAdapter orderFormWeekChildAdapter;

        @BindView(R.id.btn_cancel_order)
        Button mBtnCancelOrder;
        @BindView(R.id.btn_go_pay)
        Button mBtnGoPay;

        public OrderFormWeekViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
