package com.oscar.writtenexamination.Widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.oscar.writtenexamination.R;

import java.util.List;

public class TagTextView extends AppCompatTextView {

    private StringBuffer content_buffer;

    private TextView tvTag;

    private View view;//标签布局的最外层布局

    private Context mContext;

    public TagTextView(Context context) {
        super(context);
        mContext = context;
    }

    public TagTextView(Context context, AttributeSet attrs) {

        super(context, attrs);

        mContext = context;

    }


    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        mContext = context;

    }


    public void setContentAndTag(String content, List<String> tags) {
        content_buffer = new StringBuffer();
        for (String item : tags) {//将每个tag的内容添加到content后边，之后将用drawable替代这些tag所占的位置
            content_buffer.append(item);
        }
        content_buffer.append(content);
        SpannableString spannableString = new SpannableString(content_buffer);
        for (int i = 0; i < tags.size(); i++) {
            String item = tags.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.text_with_tag, null);//R.layout.tag是每个标签的布局
            tvTag = view.findViewById(R.id.tagText);
            tvTag.setText(item);
            Bitmap bitmap = convertViewToBitmap(view);
            Drawable d = new BitmapDrawable(bitmap);
            d.setBounds(0, 0, tvTag.getWidth(), tvTag.getHeight());//缺少这句的话，不会报错，但是图片不回显示
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);//图片将对齐底部边线
            int startIndex;
            int endIndex;
            startIndex = getLastLength(tags, i );
            endIndex = startIndex + item.length();
            spannableString.setSpan(span, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        setText(spannableString);
        setGravity(Gravity.CENTER_VERTICAL);
    }


    private static Bitmap convertViewToBitmap(View view) {

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();


        return bitmap;

    }


    private int getLastLength(List<String> list, int maxLength) {
        int length = 0;
        for (int i = 0; i < maxLength; i++) {
            length += list.get(i).length();
        }
        return length;
    }
}
