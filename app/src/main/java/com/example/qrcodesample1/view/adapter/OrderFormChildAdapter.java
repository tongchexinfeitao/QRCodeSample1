package com.example.qrcodesample1.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.OrderFormBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFormChildAdapter extends RecyclerView.Adapter<OrderFormChildAdapter.FormChildViewHolder> {


    List<OrderFormBean.OrderListBean.DetailListBean> mDetailListBeans = new ArrayList<>();

    public OrderFormChildAdapter(List<OrderFormBean.OrderListBean.DetailListBean> detailListBeans) {
        this.mDetailListBeans = detailListBeans;
    }

    @NonNull
    @Override
    public FormChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order_child_complete_layout, viewGroup, false);
        FormChildViewHolder viewHolder = new FormChildViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FormChildViewHolder formChildViewHolder, int i) {
        OrderFormBean.OrderListBean.DetailListBean detailListBean = mDetailListBeans.get(i);
        formChildViewHolder.mIvOrderChildIcon.setImageResource(R.mipmap.ic_launcher_round);
        formChildViewHolder.mTvOrderChildPrice.setText("" +detailListBean.getCommodityPrice());
        formChildViewHolder.mTvOrderChildTitle.setText("" +detailListBean.getCommodityName());
    }

    @Override
    public int getItemCount() {
        return mDetailListBeans == null ? 0 : mDetailListBeans.size();
    }

    class FormChildViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_order_child_icon)
        ImageView mIvOrderChildIcon;
        @BindView(R.id.tv_order_child_title)
        TextView mTvOrderChildTitle;
        @BindView(R.id.tv_order_child_price)
        TextView mTvOrderChildPrice;

        public FormChildViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
