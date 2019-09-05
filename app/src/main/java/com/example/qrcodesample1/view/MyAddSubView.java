package com.example.qrcodesample1.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.qrcodesample1.R;

public class MyAddSubView extends LinearLayout {


    public MyAddSubView(Context context) {
        super(context, null);
    }

    public MyAddSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View inflate = inflate(context, R.layout.add_remove_view_layout, this);
    }
}
