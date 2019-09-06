package com.example.qrcodesample1.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodesample1.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAddSubView extends LinearLayout {
    @BindView(R.id.sub_tv)
    TextView mSubTv;
    @BindView(R.id.product_number_tv)
    TextView mProductNumberTv;
    @BindView(R.id.add_tv)
    TextView mAddTv;

    private int mNum = 1;

    public MyAddSubView(Context context) {
        super(context, null);
    }

    public MyAddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = inflate(context, R.layout.add_remove_view_layout, this);
        ButterKnife.bind(inflate);

    }

    @OnClick({R.id.sub_tv, R.id.add_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sub_tv:
                if (mNum > 1) {
                    mNum--;
                    mProductNumberTv.setText(mNum+"");
                    if (mOnNumberChangeListener != null) {
                        mOnNumberChangeListener.onNumberChange(mNum);
                    }
                } else {
                    Toast.makeText(getContext(), "不能再少了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_tv:
                mNum++;
                mProductNumberTv.setText(mNum+"");
                if (mOnNumberChangeListener != null) {
                    mOnNumberChangeListener.onNumberChange(mNum);
                }
                break;
        }
    }

    private onNumberChangeListener mOnNumberChangeListener;

    public void setmOnNumberChangeListener(onNumberChangeListener mOnNumberChangeListener) {
        this.mOnNumberChangeListener = mOnNumberChangeListener;
    }

    public void setNum(int count) {
        mNum=count;
        mProductNumberTv.setText(mNum+"");
    }

    public interface onNumberChangeListener {
        void onNumberChange(int num);
    }
}
