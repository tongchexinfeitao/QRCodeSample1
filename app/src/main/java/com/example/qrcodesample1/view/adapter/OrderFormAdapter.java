package com.example.qrcodesample1.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.OrderFormBean;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFormAdapter extends XRecyclerView.Adapter<OrderFormAdapter.OrderViewHolder> {

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
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_order_form_complete_layout, viewGroup, false);
        OrderViewHolder viewHolder = new OrderViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder completeViewHolder, int i) {
        //拿到当前订单的bean类
        final OrderFormBean.OrderListBean orderListBean = mOrderListBeans.get(i);
        //格式化时间
        String time = simpleDateFormat.format(orderListBean.getOrderTime());
        //拿到订单中所有的商品
        List<OrderFormBean.OrderListBean.DetailListBean> detailList = orderListBean.getDetailList();
        //计算总数量和总价格
        double totalPrice = 0;
        int totalCount = 0;
        for (int j = 0; j < detailList.size(); j++) {
            OrderFormBean.OrderListBean.DetailListBean detailListBean = detailList.get(j);
            totalCount += detailListBean.getCommodityCount();
            totalPrice += detailListBean.getCommodityPrice() * detailListBean.getCommodityCount();
        }
        String format = "共%s件商品,需付款%s元";
        String format1 = String.format(format, "" + totalCount, "" + totalPrice);

        //订单号
        completeViewHolder.mTvOrderId.setText("订单号  " + orderListBean.getOrderId());
        //上方的时间
        completeViewHolder.mTvOriderTimeTop.setText(time);
        //下方的时间
        completeViewHolder.mTvOriderTimeBottop.setText(time);
        //待支付中的数量和价格
        completeViewHolder.mTvTotalPrice.setText(format1);
        //带收货中的快递公司
        completeViewHolder.mTvExpressCompany.setText(orderListBean.getExpressCompName());
        //带收货中的快递号
        completeViewHolder.mTvExpressId.setText(orderListBean.getExpressSn());

        //去支付
        completeViewHolder.mBtnGoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOrderFormClickListener != null) {
                    onOrderFormClickListener.onGoPay(orderListBean.getOrderId());
                }
            }
        });

        //取消订单
        completeViewHolder.mBtnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOrderFormClickListener != null) {
                    onOrderFormClickListener.onCancelOrder(orderListBean.getOrderId());
                }
            }
        })

        //拿到当前的订单
        switch (orderListBean.getOrderStatus()) {
            case WAIT_PARY_TYPE:
                //待支付的控件需要显示
                completeViewHolder.mTvOrderId.setVisibility(View.VISIBLE);
                completeViewHolder.mTvOriderTimeTop.setVisibility(View.VISIBLE);
                completeViewHolder.mTvTotalPrice.setVisibility(View.VISIBLE);
                completeViewHolder.mBtnCancelOrder.setVisibility(View.VISIBLE);
                completeViewHolder.mBtnGoPay.setVisibility(View.VISIBLE);

                //待收货中的快递公司和快递号需要隐藏
                completeViewHolder.mTvExpressCompany.setVisibility(View.GONE);
                completeViewHolder.mTvExpressId.setVisibility(View.GONE);
                completeViewHolder.mTvConfirmReceive.setVisibility(View.GONE);

                //待评价的时间和删除按钮需要隐藏
                completeViewHolder.mTvOriderTimeBottop.setVisibility(View.GONE);
                completeViewHolder.mBtnDeleteOrder.setVisibility(View.GONE);
                break;
                //当订单状态是待收货的时候
            case WAIT_RECEIVE_TYPE:
                completeViewHolder.mTvOrderId.setVisibility(View.VISIBLE);
                completeViewHolder.mTvOriderTimeTop.setVisibility(View.VISIBLE);
                completeViewHolder.mTvExpressCompany.setVisibility(View.VISIBLE);
                completeViewHolder.mTvExpressId.setVisibility(View.VISIBLE);
                completeViewHolder.mTvConfirmReceive.setVisibility(View.VISIBLE);

                //关于待支付的控件需要隐藏
                completeViewHolder.mTvTotalPrice.setVisibility(View.GONE);
                completeViewHolder.mBtnCancelOrder.setVisibility(View.GONE);
                completeViewHolder.mBtnGoPay.setVisibility(View.GONE);

                //待评价的时间和删除按钮需要隐藏
                completeViewHolder.mBtnDeleteOrder.setVisibility(View.GONE);
                completeViewHolder.mTvOriderTimeBottop.setVisibility(View.GONE);
                break;
            case WAIT_EVALUATE_TYPE:
            case COMPLETE_TYPE:
                //关于待评价和已完成的删除按钮和下方时间需要显示
                completeViewHolder.mBtnDeleteOrder.setVisibility(View.VISIBLE);
                completeViewHolder.mTvOriderTimeBottop.setVisibility(View.VISIBLE);

                //关于待支付的控件需要隐藏
                completeViewHolder.mTvTotalPrice.setVisibility(View.GONE);
                completeViewHolder.mBtnCancelOrder.setVisibility(View.GONE);
                completeViewHolder.mBtnGoPay.setVisibility(View.GONE);

                //关于待收货的控件需要隐藏
                completeViewHolder.mTvExpressCompany.setVisibility(View.GONE);
                completeViewHolder.mTvExpressId.setVisibility(View.GONE);
                completeViewHolder.mTvConfirmReceive.setVisibility(View.GONE);
                break;
        }

        completeViewHolder.mRvOrderProduct.setLayoutManager(new LinearLayoutManager(completeViewHolder.itemView.getContext()));
        //设置商品的recyclerview
        completeViewHolder.orderFormChildAdapter = new OrderFormChildAdapter(orderListBean.getDetailList());
        completeViewHolder.mRvOrderProduct.setAdapter(completeViewHolder.orderFormChildAdapter);
    }

    @Override
    public int getItemCount() {
        return mOrderListBeans == null ? 0 : mOrderListBeans.size();
    }

    public void setData(List<OrderFormBean.OrderListBean> orderList) {
        mOrderListBeans.clear();
        mOrderListBeans.addAll(orderList);
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends XRecyclerView.ViewHolder {
        @BindView(R.id.tv_order_id)
        TextView mTvOrderId;
        @BindView(R.id.tv_orider_time_top)
        TextView mTvOriderTimeTop;
        @BindView(R.id.btn_delete_order)
        TextView mBtnDeleteOrder;
        @BindView(R.id.rv_order_product)
        RecyclerView mRvOrderProduct;
        @BindView(R.id.tv_total_price)
        TextView mTvTotalPrice;
        @BindView(R.id.tv_orider_time_bottop)
        TextView mTvOriderTimeBottop;
        @BindView(R.id.btn_cancel_order)
        Button mBtnCancelOrder;
        @BindView(R.id.btn_go_pay)
        Button mBtnGoPay;
        @BindView(R.id.tv_express_company)
        TextView mTvExpressCompany;
        @BindView(R.id.tv_express_id)
        TextView mTvExpressId;
        @BindView(R.id.tv_confirm_receive)
        Button mTvConfirmReceive;
        OrderFormChildAdapter orderFormChildAdapter;

        OrderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    onOrderFormClickListener onOrderFormClickListener;

    public void setOnOrderFormClickListener(OrderFormAdapter.onOrderFormClickListener onOrderFormClickListener) {
        this.onOrderFormClickListener = onOrderFormClickListener;
    }

  public   interface  onOrderFormClickListener{
      void   onGoPay(String orderId);
      void   onCancelOrder(String orderId);
    }
}
