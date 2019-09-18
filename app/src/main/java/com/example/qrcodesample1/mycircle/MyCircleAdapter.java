package com.example.qrcodesample1.mycircle;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.qrcodesample1.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

class MyCircleAdapter extends XRecyclerView.Adapter<MyCircleAdapter.MyCircleViewHolder> {
    private List<MyCircleBean.ResultBean> list;

    public MyCircleAdapter(List<MyCircleBean.ResultBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyCircleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_circle, viewGroup, false);
        return new MyCircleViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCircleViewHolder myCircleViewHolder, int i) {
        MyCircleBean.ResultBean resultBean = list.get(i);
        myCircleViewHolder.mTvCircleItemCotent.setText(resultBean.getContent());
        //先把线性布局中imageView清除掉
        myCircleViewHolder.mLlCircleItemImagesContainer.removeAllViews();
        //所有图片url用逗号拼接起来的字符串
        String images = resultBean.getImage();
        //用逗号分隔上面的字符串，得到所有图片url的数组
        String[] imageUrls = images.split(",");
        for (int j = 0; j < imageUrls.length; j++) {
            String imageUrl = imageUrls[j];
            ImageView imageView = new ImageView(myCircleViewHolder.itemView.getContext());
            Glide.with(myCircleViewHolder.itemView.getContext())
                    .load(imageUrl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(30, 0)))
                    .error(R.mipmap.ic_launcher_round)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(imageView);
            //把imageView添加到线性布局中
            myCircleViewHolder.mLlCircleItemImagesContainer.addView(imageView, 120, 120);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * 刷新和加载更多
     *
     * @param result
     * @param isLoadMore
     */
    public void setData(List<MyCircleBean.ResultBean> result, boolean isLoadMore) {
        if (isLoadMore) {
            list.addAll(result);
        } else {
            list.clear();
            list.addAll(result);
        }
        notifyDataSetChanged();
    }

    public class MyCircleViewHolder extends XRecyclerView.ViewHolder {
        @BindView(R.id.tv_circle_item_cotent)
        TextView mTvCircleItemCotent;
        @BindView(R.id.ll_circle_item_images_container)
        LinearLayout mLlCircleItemImagesContainer;

        public MyCircleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
