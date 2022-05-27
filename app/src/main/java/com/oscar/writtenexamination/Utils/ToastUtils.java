package com.oscar.writtenexamination.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oscar.writtenexamination.R;

public class ToastUtils {

    /**
     * 显示短Toast
     */
    public static void showShortToast(Context context, int string) {
        Toast.makeText(context, context.getString(string), Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示短Toast
     */
    public static void showShortToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长Toast
     */
    public static void showLongToast(Context context, int string) {
        Toast.makeText(context, context.getString(string), Toast.LENGTH_LONG).show();
    }

    /**
     * 显示长Toast
     */
    public static void showLongToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示黑色背景中间文本toast
     * @param context 上下文
     * @param text 显示文本
     */
    public static void showBlackCenterToast(Context context,String text){
        Toast toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        //填充物来自的xml文件,在这个改成一个view
        //实现xml到view的转变哦
        @SuppressLint("InflateParams")
        View view =inflater.inflate(R.layout.toast_bgblack_center,null);

        //不一定需要，找到xml里面的组件，设置组件里面的具体内容
        TextView textView1=view.findViewById(R.id.tbcTv);
        if (!text.equals("")){
            textView1.setText(text);
        }

        //把填充物放进toast
        toast.setView(view);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        //展示toast
        toast.show();
    }

    /**
     * 显示图片Toast
     */
    public static void showImageToast(Context context, String text, int imgRes, int time){
        Toast toast=new Toast(context);

        //创建一个填充物,用于填充Toast
        LayoutInflater inflater = LayoutInflater.from(context);

        //填充物来自的xml文件,在这个改成一个view
        //实现xml到view的转变哦
        @SuppressLint("InflateParams")
        View view =inflater.inflate(R.layout.toast_image,null);

        //不一定需要，找到xml里面的组件，设置组件里面的具体内容
        ImageView imageView1=view.findViewById(R.id.timgImg);
        TextView textView1=view.findViewById(R.id.timgTv);
        if (!text.equals("")){
            textView1.setText(text);
        }
        imageView1.setImageResource(imgRes);

        //把填充物放进toast
        toast.setView(view);
        toast.setDuration(time);
        toast.setGravity(Gravity.CENTER,0,0);

        //展示toast
        toast.show();
    }
}
