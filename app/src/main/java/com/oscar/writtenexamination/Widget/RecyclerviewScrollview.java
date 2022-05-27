package com.oscar.writtenexamination.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

public class RecyclerviewScrollview extends ScrollView {

    private int slop;
    private int touch;

    public RecyclerviewScrollview(Context context) {
        super(context);
        setSlop(context);
    }

    public RecyclerviewScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSlop(context);
    }

    public RecyclerviewScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSlop(context);
    }


    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touch = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getRawY() - touch) > slop){
                    return true;
                }
                break;
        }
        return super.onInterceptHoverEvent(event);
    }

    private void setSlop(Context context){
        slop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
}
