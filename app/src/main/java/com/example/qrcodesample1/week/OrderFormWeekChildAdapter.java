package com.example.qrcodesample1.week;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.OrderFormBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFormWeekChildAdapter extends RecyclerView.Adapter<OrderFormWeekChildAdapter.OrderFormWeekChildViewHolder> {

    List<OrderFormBean.OrderListBean.DetailListBean> detailListBeans = new ArrayList<>();

    public OrderFormWeekChildAdapter(List<OrderFormBean.OrderListBean.DetailListBean> detailListBeans) {
        this.detailListBeans = detailListBeans;
    }

    @NonNull
    @Override
    public OrderFormWeekChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_form_week_child, viewGroup, false);
        return new OrderFormWeekChildViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderFormWeekChildViewHolder orderFormWeekChildViewHolder, int i) {
        //商品
        OrderFormBean.OrderListBean.DetailListBean detailListBean = detailListBeans.get(i);
        String urls = detailListBean.getCommodityPic();
        String[] split = urls.split(",");
        Glide.with(orderFormWeekChildViewHolder.itemView.getContext())
                .load(split[0])
                .error(R.mipmap.ic_launcher_round)
                .into(orderFormWeekChildViewHolder.mIvOrderChildIcon);
        orderFormWeekChildViewHolder.mTvOrderChildName.setText(detailListBean.getCommodityName());
        orderFormWeekChildViewHolder.mTvOrderChildPrice.setText("￥" + detailListBean.getCommodityPrice());
    }

    @Override
    public int getItemCount() {
        return detailListBeans == null ? 0 : detailListBeans.size();
    }

    public void setData(List<OrderFormBean.OrderListBean.DetailListBean> list) {
        detailListBeans.clear();
        detailListBeans.addAll(list);
        notifyDataSetChanged();
    }


    static class OrderFormWeekChildViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_order_child_icon)
        ImageView mIvOrderChildIcon;
        @BindView(R.id.tv_order_child_name)
        TextView mTvOrderChildName;
        @BindView(R.id.tv_order_child_price)
        TextView mTvOrderChildPrice;

        OrderFormWeekChildViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
