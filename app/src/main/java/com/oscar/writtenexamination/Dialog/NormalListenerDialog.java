package com.oscar.writtenexamination.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.oscar.writtenexamination.R;


public class NormalListenerDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private int themeId;

    private OnCancelListtener onCancelListtener;
    private OnConfirmListtener onConfirmListtener;

    private String title,confirm;

    private TextView tvTitle,tvModify;
    private EditText etEmail;

    public NormalListenerDialog(@NonNull Context context) {
        super(context);
    }

    public NormalListenerDialog(Context context, int themeId){
        super(context,themeId);
        this.mContext = context;
        this.themeId = themeId;
    }

    public NormalListenerDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public NormalListenerDialog setCanel(OnCancelListtener onCancelListtener) {
        this.onCancelListtener = onCancelListtener;
        return this;
    }

    public NormalListenerDialog setConfirm(String confirm, OnConfirmListtener onConfirmListtener) {
        this.confirm = confirm;
        this.onConfirmListtener = onConfirmListtener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_normal_listener);

        //如果对话框宽度异常，可以通过下方代码根据设备的宽度来设置弹窗宽度
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Point point=new Point();
        display.getSize(point);
        params.width= (int) (point.x * 0.8);
        getWindow().setAttributes(params);

        tvTitle = findViewById(R.id.dfpTitleTv);
        tvModify = findViewById(R.id.dfpConfirmBtn);
        etEmail = findViewById(R.id.dfpEmailEt);

        tvModify.setOnClickListener(this);

        tvTitle.setText(title);
        tvModify.setText(confirm);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dfpConfirmBtn:
                if (onConfirmListtener != null){
                    onConfirmListtener.onConfirm(this,etEmail);
                }
                break;
        }
    }

    //自定义接口形式提供回调方法
    public interface OnCancelListtener{
        void onCancel(NormalListenerDialog myDialog);
    }
    public interface OnConfirmListtener{
        void onConfirm(NormalListenerDialog myDialog, EditText editText);
    }
}
