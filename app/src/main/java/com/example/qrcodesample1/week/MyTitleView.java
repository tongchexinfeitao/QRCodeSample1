package com.example.qrcodesample1.week;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.qrcodesample1.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTitleView extends LinearLayout {
    @BindView(R.id.tbn_finish)
    Button mTbnFinish;
    @BindView(R.id.rv_title)
    RelativeLayout mRvTitle;

    public MyTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = inflate(context, R.layout.mytitle_view_layout, this);
        ButterKnife.bind(inflate);
        //取所有的自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTitleView);
        //去背景自定义属性
        int color = typedArray.getColor(R.styleable.MyTitleView_titleViewBackground, Color.BLACK);
        mRvTitle.setBackgroundColor(color);
    }

    @OnClick({R.id.tbn_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tbn_finish:
                if (mOnMyTitleViewClickListener != null) {
                    mOnMyTitleViewClickListener.onClick();
                }
                break;
        }
    }

    onMyTitleViewClickListener mOnMyTitleViewClickListener;

    public void setmOnMyTitleViewClickListener(onMyTitleViewClickListener mOnMyTitleViewClickListener) {
        this.mOnMyTitleViewClickListener = mOnMyTitleViewClickListener;
    }

    public interface onMyTitleViewClickListener {
        void onClick();
    }

}
