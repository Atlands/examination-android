package com.oscar.writtenexamination.Base;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oscar.writtenexamination.Network.FragNetBroadcaseReceiver;
import com.oscar.writtenexamination.Utils.NetUtils;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.netType;

public abstract class Fragment extends android.support.v4.app.Fragment implements FragNetBroadcaseReceiver.FragNetChangeListener {

    public Context mContext;
    public Activity mActivity;
    protected View mRoot;
    protected Unbinder mRootUnBinder;
    // 标示是否第一次初始化数据
    protected boolean mIsFirstInitData = true;

    public static FragNetBroadcaseReceiver.FragNetChangeListener listener;
    private FragNetBroadcaseReceiver fragNetBroadcaseReceiver;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            setNetConnect(isNetConnect);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 初始化参数
        initArgs(getArguments());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initFragNetBroadcaseReceiver();
        netType = NetUtils.getNetWorkState(Objects.requireNonNull(getContext()));
        isNetConnect = checkNet(getContext());
        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前的根布局，但是不再创建时就添加到container里边
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                // 把当前Root从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData) {
            // 触发一次以后就不会触发
            mIsFirstInitData = false;
            // 触发
            onFirstInit();
        }

        // 当View创建完成后初始化数据
        initData();
        setListener();
    }

    private void initFragNetBroadcaseReceiver() {
        listener = this;
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            fragNetBroadcaseReceiver = new FragNetBroadcaseReceiver();
            //注册广播接收
            Objects.requireNonNull(getActivity()).registerReceiver(fragNetBroadcaseReceiver, filter);
        }
    }
    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle) {
        mContext = getContext();
        mActivity = getActivity();
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root) {
        mRootUnBinder = ButterKnife.bind(this, root);
        setNetConnect(isNetConnect);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 设置监听器
     */
    protected void setListener(){

    }

    /**
     * 当首次初始化数据的时候会调用的方法
     */
    protected void onFirstInit() {

    }

    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onChangeListener(){
        return true;
    }

    @Override
    public void onChangeListener(int status) {
        if (netType == status){
            return;
        }else {
            if (netType == -1){
                netType = status;
                isNetConnect = isNetConnect();
                setNetConnect(isNetConnect);
                initData();
            }else {
                //netType = 0 或 1,原网络是移动或无线
                netType = status;
                isNetConnect = isNetConnect();
                if (status == -1){
                    //有网变无网
                    setNetConnect(isNetConnect);
                } else {
                    //有网，无线和移动切换
                    return;
                }
            }
        }
    }

    protected abstract void setNetConnect(boolean isNetConnect);


    /**
     * 初始化时判断有没有网络
     */
    public boolean checkNet(Context context) {
        netType = NetUtils.getNetWorkState(context);
        return isNetConnect();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(fragNetBroadcaseReceiver);
    }
}
