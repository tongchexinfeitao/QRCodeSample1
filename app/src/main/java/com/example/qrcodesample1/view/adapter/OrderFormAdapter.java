package com.example.qrcodesample1.view.adapter;

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
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFormAdapter extends XRecyclerView.Adapter<XRecyclerView.ViewHolder> {

    //待支付
    private static final int WAIT_PARY_TYPE = 1;
    //待收货
    private static final int WAIT_RECEIVE_TYPE = 2;
    //待评价
    private static final int WAIT_EVALUATE_TYPE = 3;
    //已完成
    private static final int COMPLETE_TYPE = 9;
    //所有的订单
    List<OrderFormBean.OrderListBean> mOrderListBeans = new ArrayList<>();

    String pat1 = "yyyy-MM-dd HH:mm";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pat1);

    public OrderFormAdapter(List<OrderFormBean.OrderListBean> orderListBeans) {
        this.mOrderListBeans = orderListBeans;
    }

    @NonNull
    @Override
    public XRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = null;
        RecyclerView.ViewHolder viewHolder = null;
        if (i == COMPLETE_TYPE) {
            inflate = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_order_form_complete_layout, viewGroup, false);
            viewHolder = new CompleteViewHolder(inflate);
        } else if (i == WAIT_PARY_TYPE) {
            //用已完成的布局去代替去支付
            inflate = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_order_form_complete_layout, viewGroup, false);
            viewHolder = new CompleteViewHolder(inflate);

        } else if (i == WAIT_RECEIVE_TYPE) {
            inflate = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_order_form_receive_layout, viewGroup, false);
            viewHolder = new WaitReceiveViewHolder(inflate);

        } else if (i == WAIT_EVALUATE_TYPE) {
            //用已完成的布局去代替去评价
            inflate = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_order_form_complete_layout, viewGroup, false);
            viewHolder = new CompleteViewHolder(inflate);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull XRecyclerView.ViewHolder viewHolder, int i) {
        //拿到当前的订单
        OrderFormBean.OrderListBean orderListBean = mOrderListBeans.get(i);
        if (viewHolder instanceof CompleteViewHolder) {
            CompleteViewHolder completeViewHolder = (CompleteViewHolder) viewHolder;
            completeViewHolder.mTvOrderId.setText("订单号  " + orderListBean.getOrderId());

            String time = simpleDateFormat.format(orderListBean.getOrderTime());
            completeViewHolder.mTvOriderTime.setText(time);
            completeViewHolder.mRvOrderProduct.setLayoutManager(new LinearLayoutManager(completeViewHolder.itemView.getContext()));

            //设置商品的recyclerview
            completeViewHolder.orderFormChildAdapter = new OrderFormChildAdapter(orderListBean.getDetailList());
            completeViewHolder.mRvOrderProduct.setAdapter(completeViewHolder.orderFormChildAdapter);

        } else if (viewHolder instanceof WaitReceiveViewHolder) {
            final WaitReceiveViewHolder waitReceiveViewHolder = (WaitReceiveViewHolder) viewHolder;
            waitReceiveViewHolder.mTvOrderId.setText(orderListBean.getOrderId());
            String time = simpleDateFormat.format(orderListBean.getOrderTime());
            waitReceiveViewHolder.mTvOriderTime.setText(time);
            waitReceiveViewHolder.mTvExpressCompany.setText(orderListBean.getExpressCompName());
            waitReceiveViewHolder.mTvExpressId.setText(orderListBean.getExpressSn());
            waitReceiveViewHolder.mBtnReceive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(waitReceiveViewHolder.itemView.getContext(), "去收货", Toast.LENGTH_SHORT).show();
                }
            });
            //设置商品的recyclerview
            waitReceiveViewHolder.orderFormChildAdapter = new OrderFormChildAdapter(orderListBean.getDetailList());
            waitReceiveViewHolder.mRvOrderProduct.setLayoutManager(new LinearLayoutManager(waitReceiveViewHolder.itemView.getContext()));
            waitReceiveViewHolder.mRvOrderProduct.setAdapter(waitReceiveViewHolder.orderFormChildAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return mOrderListBeans == null ? 0 : mOrderListBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        OrderFormBean.OrderListBean orderListBean = mOrderListBeans.get(position);
        int orderStatus = orderListBean.getOrderStatus();
        //考试可以直接
//        return orderStatus;
        int itemViewType = 1;
        switch (orderStatus) {
            case 1:
                itemViewType = WAIT_PARY_TYPE;
                break;
            case 2:
                itemViewType = WAIT_RECEIVE_TYPE;
                break;
            case 3:
                itemViewType = WAIT_EVALUATE_TYPE;
                break;
            case 9:
                itemViewType = COMPLETE_TYPE;
                break;
        }
        return itemViewType;
    }

    public void setData(List<OrderFormBean.OrderListBean> orderList) {
        mOrderListBeans.clear();
        mOrderListBeans.addAll(orderList);
        notifyDataSetChanged();
    }

    static class CompleteViewHolder extends XRecyclerView.ViewHolder {
        @BindView(R.id.tv_order_id)
        TextView mTvOrderId;
        @BindView(R.id.rv_order_product)
        RecyclerView mRvOrderProduct;
        @BindView(R.id.tv_orider_time)
        TextView mTvOriderTime;
        OrderFormChildAdapter orderFormChildAdapter;

        CompleteViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class WaitReceiveViewHolder extends XRecyclerView.ViewHolder {
        @BindView(R.id.tv_order_id)
        TextView mTvOrderId;
        @BindView(R.id.tv_orider_time)
        TextView mTvOriderTime;
        @BindView(R.id.rv_order_product)
        RecyclerView mRvOrderProduct;
        @BindView(R.id.tv_express_company)
        TextView mTvExpressCompany;
        @BindView(R.id.tv_express_id)
        TextView mTvExpressId;
        @BindView(R.id.btn_receive)
        Button mBtnReceive;
        OrderFormChildAdapter orderFormChildAdapter;

        WaitReceiveViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
