package com.oscar.writtenexamination.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.oscar.writtenexamination.R;

import static com.oscar.writtenexamination.Base.Configurations.setText;

public class CheckVersionDialog extends Dialog implements View.OnClickListener {

    Context mContext;
    int themeId;

    private TextView tvVersion,btnUpdate;

    private String version;
//    private OnCancelListtener onCancelListtener;
    private OnConfirmListtener onConfirmListtener;

    public CheckVersionDialog(Context context) {
        super(context);
    }

    public CheckVersionDialog(Context context, int themeId){
        super(context,themeId);
        this.mContext = context;
        this.themeId = themeId;
    }

    public CheckVersionDialog setVersion(String version) {
        this.version = "v " + version;
        return this;
    }

//    public CheckVersionDialog setCanel(OnCancelListtener onCancelListtener) {
//        this.onCancelListtener = onCancelListtener;
//        return this;
//    }
//
    public CheckVersionDialog setConfirm(OnConfirmListtener onConfirmListtener) {
        this.onConfirmListtener = onConfirmListtener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_version);

        //如果对话框宽度异常，可以通过下方代码根据设备的宽度来设置弹窗宽度
        Window window = getWindow();
        if (window != null){
            WindowManager windowManager = window.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams params = getWindow().getAttributes();
            Point point=new Point();
            display.getSize(point);
            params.width= (int) (point.x * 0.8);
            window.setAttributes(params);

            tvVersion = findViewById(R.id.dvVersionTv);
            btnUpdate = findViewById(R.id.dvUpdateBtn);

            btnUpdate.setOnClickListener(this);

            setText(tvVersion,version);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dvUpdateBtn:
                if (onConfirmListtener != null){
                    onConfirmListtener.onConfirm(this);
                }
                dismiss();
                break;
//            case R.id.dvCancleBtn:
//                if (onCancelListtener != null){
//                    onCancelListtener.onCancel(this);
//                }
//                dismiss();
//                break;
        }
    }

    //自定义接口形式提供回调方法
    public interface OnCancelListtener{
        void onCancel(CheckVersionDialog myDialog);
    }
    public interface OnConfirmListtener{
        void onConfirm(CheckVersionDialog myDialog);
    }
}
