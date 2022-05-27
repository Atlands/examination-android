package com.oscar.writtenexamination.Network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.Utils.NetUtils;

public class FragNetBroadcaseReceiver extends BroadcastReceiver {

    public FragNetChangeListener listener = Fragment.listener;
    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtils.getNetWorkState(context);
            // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态
            if (listener != null) {
                listener.onChangeListener(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface FragNetChangeListener {
        void onChangeListener(int status);
    }
}
