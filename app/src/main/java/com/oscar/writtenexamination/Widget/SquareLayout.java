package com.oscar.writtenexamination.Widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SquareLayout extends FrameLayout {
    public SquareLayout(@NonNull Context context) {
        super(context);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 复写测量方法
     * @param widthMeasureSpec 传递宽度
     * @param heightMeasureSpec 传递高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //给父类传递的测量值都为宽度，则为基于宽度的正方形控件
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
