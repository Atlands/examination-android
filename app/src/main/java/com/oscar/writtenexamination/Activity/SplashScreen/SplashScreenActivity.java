package com.oscar.writtenexamination.Activity.SplashScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.Home.HomeActivity;
import com.oscar.writtenexamination.Base.Activity;
import com.oscar.writtenexamination.Fragment.PermissionsFragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import butterknife.BindView;

import static com.oscar.writtenexamination.Base.Configurations.CONFIG_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.configOoject;
import static com.oscar.writtenexamination.Base.Configurations.getCurrentVersion;

public class SplashScreenActivity extends Activity {

    @BindView(R.id.spsToolbar)
    Toolbar mToolbar;


    @BindView(R.id.spsVersion)
    TextView tvVersion;

    @BindView(R.id.spsLoadingTv)
    TextView tvLoad;
    @BindView(R.id.spsProgressBar)
    ProgressBar pb;

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    //数据
    String appName = "";
    String version = "";
    //延迟时间
    private int splashInterval = 2000;

    /**
     * 跳转入口
     */
    public static void show(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SplashScreenActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        context = this;
        activity = this;
        return super.initArgs(bundle);
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash_screen;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        //设置状态栏
        StatusBarUtil.setGradientColor(this, mToolbar);
        UiUtils.setStatusBarDarkTheme(this, true);
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        super.initData();
        //设置版本号
        version = getCurrentVersion(this);
        appName = AppUtils.getAppName();
        tvVersion.setText(appName + "  v " + version);
        getConfigurations();
    }

//    /**
//     * 真实的跳转
//     */
//    private void reallySkip() {
//        // 权限检测，跳转
////        if (PermissionsFragment.haveAll(mContext, getSupportFragmentManager())) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    HomeActivity.show(mContext,null);
//                    this.finish();
//                }
//                private void finish() {
//                }}, splashInterval);
////        }
//    }

    /**
     * 获取参数表
     */
    @SuppressLint("SetTextI18n")
    void getConfigurations(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CONFIG_CLASS_NAME);
        query.findInBackground((objects, e) -> {
            if (e == null){
                if (objects.size() == 0){
                    configOoject = null;
                    tvLoad.setText(getString(R.string.tipsGetConfigFailed));
                    getConfigurations();
                }else {
                    configOoject = objects.get(0);
                    tvLoad.setText(getString(R.string.tipGetConfigSuccess));
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            LoginSignActivity.show(mContext,null);
//                            this.finish();
//                        }
//                        private void finish() {
//                        }}, splashInterval);
                    // 权限检测，跳转
                    if (PermissionsFragment.haveAll(mContext, getSupportFragmentManager())) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HomeActivity.show(mContext, null);
                                this.finish();
                            }

                            private void finish() {
                            }
                        }, splashInterval);
                    }
                }
            }else {
                configOoject = null;
                tvLoad.setText(getString(R.string.tipsGetConfigFailed));
                ToastUtils.showLongToast(this,
                        getString(R.string.tipsGetConfigFailed) + "：" + e.getMessage());
                getConfigurations();
            }
        });
    }
    /**
     * 设置监听器
     */
    @Override
    protected void setListener() {
        super.setListener();
    }
}
