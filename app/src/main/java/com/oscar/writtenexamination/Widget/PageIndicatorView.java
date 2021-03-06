package com.oscar.writtenexamination.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.DimensionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 页码指示器类
 * 此类可以作为一个工具类，在ViewPager做的轮播图上也可以使用
 */
public class PageIndicatorView extends LinearLayout {

    private Context mContext = null;
    private int dotSize = 5; // 指示器的大小（dp）
    private int margins = 4; // 指示器间距（dp）
    private List<View> indicatorViews = null; // 存放指示器

    public PageIndicatorView(Context context) {
        this(context, null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        dotSize = DimensionUtils.dip2px(context, dotSize);
        margins = DimensionUtils.dip2px(context, margins);
    }

    /**
     * 初始化指示器，默认选中第一页
     *
     * @param count 指示器数量，即页数
     */
    public void initIndicator(int count) {

        if (indicatorViews == null) {
            indicatorViews = new ArrayList<>();
        } else {
            indicatorViews.clear();
            removeAllViews();
        }
        View view;
        LayoutParams params = new LayoutParams(dotSize, dotSize);
        params.setMargins(margins, margins, margins, margins);
        for (int i = 0; i < count; i++) {
            view = new View(mContext);
            view.setBackgroundResource(android.R.drawable.presence_invisible);
            addView(view, params);
            indicatorViews.add(view);
        }
        if (indicatorViews.size() > 0) {
            indicatorViews.get(0).setBackgroundResource(android.R.drawable.presence_online);
        }
    }

    /**
     * 设置选中页
     *
     * @param selected 页下标，从0开始
     */
    public void setSelectedPage(int selected) {
        LayoutParams params1 = new LayoutParams(dotSize*3, dotSize);
        params1.setMargins(margins, margins, margins, margins);
        LayoutParams params2 = new LayoutParams(dotSize, dotSize);
        params2.setMargins(margins, margins, margins, margins);
        for (int i = 0; i < indicatorViews.size(); i++) {
            if (i == selected) {
                indicatorViews.get(i).setLayoutParams(params1);
                indicatorViews.get(i).setBackgroundResource(R.drawable.bg_indicator_selected);
            } else {
                indicatorViews.get(i).setLayoutParams(params2);
                indicatorViews.get(i).setBackgroundResource(R.drawable.bg_indicator_normal);
            }
        }
    }
}
