package com.example.qrcodesample1.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrcodesample1.R;
import com.example.qrcodesample1.bean.CartBean;
import com.example.qrcodesample1.view.MyAddSubView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends BaseExpandableListAdapter {
    //所有商家的集合
    List<CartBean.ResultBean> mSellerList;

    public CartAdapter(List<CartBean.ResultBean> list) {
        this.mSellerList = list;
    }

    /**
     * 设置新的数据刷新适配器
     *
     * @param list
     */
    public void setData(List<CartBean.ResultBean> list) {
        mSellerList.clear();
        mSellerList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mSellerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSellerList.get(groupPosition).getShoppingCartList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //商家的布局
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_parent, parent, false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        // TODO: 2019/9/5  获取当前商家的选中状态
        //拿到当前商家
        CartBean.ResultBean groupBean = mSellerList.get(groupPosition);
        //拿到商家名字
        String categoryName = groupBean.getCategoryName();
        //设置商家的名字
        groupViewHolder.mSellerNameTv.setText(categoryName);

        //拿到当前商家的商品列表
        final List<CartBean.ResultBean.ShoppingCartListBean> shoppingCartList = groupBean.getShoppingCartList();
        boolean groupIsChecked=true;
        //遍历当前商家下的所有的商品，只要有一个没选中的话，那么当前商家就应该是未选中
        for (int i = 0; i < shoppingCartList.size(); i++) {
            //获取到单个的商品
            CartBean.ResultBean.ShoppingCartListBean shoppingCartListBean = shoppingCartList.get(i);
            if (!shoppingCartListBean.isChecked()) {
                groupIsChecked=false;
                break;
            }
        }
        //设置商家的checkbox的状态为true
        groupViewHolder.mSellerCb.setChecked(groupIsChecked);

        //设置商家的checkbox的点击事件
        groupViewHolder.mSellerCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/9/5  拿到商家的checkbox当前的状态
                boolean oldGroupIsChecked = true;
                for (int i = 0; i < shoppingCartList.size(); i++) {
                    //获取到单个的商品
                CartBean.ResultBean.ShoppingCartListBean shoppingCartListBean = shoppingCartList.get(i);
                if (!shoppingCartListBean.isChecked()) {
                    oldGroupIsChecked = false;
                    break;
                }
            }

                // TODO: 2019/9/5 将这个状态置反
                //新状态
                boolean newGroupIsChecked = !oldGroupIsChecked;
                // TODO: 2019/9/5 将置反后的状态设置给当前商家下的所有的商品
                for (int i = 0; i < shoppingCartList.size(); i++) {
                    //获取到单个的商品
                    CartBean.ResultBean.ShoppingCartListBean shoppingCartListBean = shoppingCartList.get(i);
                    shoppingCartListBean.setChecked(newGroupIsChecked);
                }
                // TODO: 2019/9/5 数据修改完毕之后刷新适配器
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    //商品的布局
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //拿到第groupPosition个商家的第childPosition的商品
        final CartBean.ResultBean.ShoppingCartListBean shoppingCartListBean = mSellerList.get(groupPosition).getShoppingCartList().get(childPosition);

        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_child, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        //设置当前商品的选中状态
        childViewHolder.mChildCb.setChecked(shoppingCartListBean.isChecked());
        //设置当前商品的标题
        childViewHolder.mProductTitleNameTv.setText(shoppingCartListBean.getCommodityName());
        //设置当前商品的单价
        childViewHolder.mProductPriceTv.setText(""+shoppingCartListBean.getPrice());
        // TODO: 2019/9/5 设置商品的数量

        // TODO: 2019/9/5 操作商品的点击事件
        childViewHolder.mChildCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/9/5  修改当前商品的状态  ps:得到旧状态，置反之后就是新状态，然后修改数据为新状态
                shoppingCartListBean.setChecked(!shoppingCartListBean.isChecked());
                // TODO: 2019/9/5 重新计算商家的状态   ps：刷新适配器，会重新执行getGroupView方法，这个方法里面会计算，所以直接刷新就行
                notifyDataSetChanged();
                // TODO: 2019/9/5 重新计算全选的状态、总价、总数量
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupViewHolder {
        @BindView(R.id.seller_cb)
        CheckBox mSellerCb;
        @BindView(R.id.seller_name_tv)
        TextView mSellerNameTv;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.child_cb)
        CheckBox mChildCb;
        @BindView(R.id.product_icon_iv)
        ImageView mProductIconIv;
        @BindView(R.id.product_title_name_tv)
        TextView mProductTitleNameTv;
        @BindView(R.id.product_price_tv)
        TextView mProductPriceTv;
        @BindView(R.id.add_remove_view)
        MyAddSubView mAddRemoveView;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
