package com.oscar.writtenexamination.Base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.oscar.writtenexamination.Network.NetBroadcastReceiver;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.oscar.writtenexamination.Base.Configurations.hideKeyboard;
import static com.oscar.writtenexamination.Base.Configurations.isShouldHideKeyboard;
import static com.oscar.writtenexamination.Base.Configurations.netType;
import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.layoutNotNetConnect;


public abstract class Activity extends AppCompatActivity implements NetBroadcastReceiver.NetChangeListener{

    private NetBroadcastReceiver netBroadcastReceiver;
    public static NetBroadcastReceiver.NetChangeListener listener;
    private Unbinder unbinder;
    //双击退出
    private long mLastBackTime = 0;

    public Context mContext;
    public Activity mActivity;
    public ParseUser currentUser;
    public Bundle mBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在界面未初始化之前调用的初始化窗口
        initWidows();
        initNetBroadcaseReceiver();
        if (initArgs(getIntent().getExtras())) {
            // 得到界面Id并设置到Activity界面中
            int layId = getContentLayoutId();
            setContentView(layId);
            initBefore();
            initWidget();
            initData();
            setListener();
        } else {
            finish();
        }
    }


    private void init() {
        if (initArgs(getIntent().getExtras())) {
            // 得到界面Id并设置到Activity界面中
            int layId = getContentLayoutId();
            setContentView(layId);
            initBefore();
            initWidget();
            initData();
            setListener();
        } else {
            finish();
        }
    }

    private void initNetBroadcaseReceiver() {
        listener = this;
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            netBroadcastReceiver = new NetBroadcastReceiver();
            //注册广播接收
            getApplicationContext().registerReceiver(netBroadcastReceiver, filter);
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWidows() {
        if (getContentLayoutId() == R.layout.activity_splash_screen) return;
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        mContext = this;
        mActivity = this;
        mBundle = bundle;
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {
        //检查版本更新
        //checkVersion(this);
    }

    /**
     * 初始化控件
     */
    protected void initWidget() {
        if (unbinder == null){
            unbinder = ButterKnife.bind(this);
        }
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 设置监听器
     */
    protected void setListener() {
    }


    @Override
    public boolean onSupportNavigateUp() {
        // 当点击界面导航返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isTaskRoot()) {
                long now = new Date().getTime();
                long TIME_DIFF = 2 * 1000;
                if (now - mLastBackTime < TIME_DIFF) {
                    return super.onKeyDown(keyCode, event);
                } else {
                    mLastBackTime = now;
                    ToastUtils.showShortToast(this, R.string.tipsPressAgainOut);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        // 得到当前Activity下的所有Fragment
        @SuppressLint("RestrictedApi")
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                // 判断是否为我们能够处理的Fragment类型
                if (fragment instanceof com.oscar.writtenexamination.Base.Fragment) {
                    // 判断是否拦截了返回按鈕
                    if (((com.oscar.writtenexamination.Base.Fragment) fragment).onBackPressed()) {
                        // 如果有直接Return
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }


    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netType == 1) {
            return true;
        } else if (netType == 0) {
            return true;
        } else if (netType == -1) {
            return false;
        }
        return false;
    }


    /**
     * 网络变化之后的类型
     */
    @Override
    public void onChangeListener(int netMobile) {
        if (getContentLayoutId() == R.layout.activity_splash_screen) return;
        if (netType != netMobile){
            netType = netMobile;
            isNetConnect = isNetConnect();
            if (!isNetConnect){

//            }else {
                ToastUtils.showShortToast(this, R.string.tipsNetworkConnectFailedPlzCheckIt);
            }
        }
        @SuppressLint("RestrictedApi")
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments.size() > 1) {
            for (Fragment fragment : fragments) {
                // 判断是否为我们能够处理的Fragment类型
                if (fragment instanceof com.oscar.writtenexamination.Base.Fragment) {
                    // 判断是否拦截了返回按鈕
                    if (((com.oscar.writtenexamination.Base.Fragment) fragment).onChangeListener()) {
                        // 如果有直接Return
                        return;
                    }
                }
            }
        }
        // TODO Auto-generated method stub
        if (netType != netMobile) {

//        } else {
            Log.i("netType", "netType:" + netMobile);
            int layId = getContentLayoutId();
            if (judgeLayout(layId)){

//            }else {
                if (isNetConnect){
                    init();
                }else {
                    noNetcon();
                }
            }
        }
    }

    /**
     * 根据布局判断无网络操作
     */
    private boolean judgeLayout(int layId){
        return true;
    }

    /**
     * 没网络
     */
    public void noNetcon() {
        setContentView(layoutNotNetConnect);
        Button button = findViewById(R.id.nnc_set_btn);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        });
    }

    /**
     * 软键盘外部点击事件
     * @param ev 事件
     * @return true代表处理完
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(getBaseContext(),v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(netBroadcastReceiver!= null){
            getApplicationContext().unregisterReceiver(netBroadcastReceiver );
        }
    }
}
