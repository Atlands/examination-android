package com.oscar.writtenexamination.Activity.Home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Adapter.BottomNavigationAdapter;
import com.oscar.writtenexamination.Adapter.HomeScreenPagerAdapter;
import com.oscar.writtenexamination.Base.Activity;
import com.oscar.writtenexamination.R;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import butterknife.BindView;

import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.getNewVersion;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;

public class HomeActivity extends Activity implements BottomNavigationAdapter.BottomNavigationListener, View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;

    @BindView(R.id.homeBottomNagationRy)
    RecyclerView ry;

    //适配器
    HomeScreenPagerAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    public static BottomNavigationAdapter bottomNavigationAdapter;

    private static final int INITIAL_SELECTED_TAB_POSITION = 0;


    /**
     * 跳转入口
     */
    public static void show(Context context, Bundle bundle) {
        Intent intent = new Intent(context, HomeActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        super.initData();
        currentUser = ParseUser.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检查版本
        getNewVersion(this);
        if (isLogin()) {
            final ParseInstallation inst = ParseInstallation.getCurrentInstallation();

            // IMPORTANT: REPLACE "191162618244" WITH YOUR OWN GCM SENDER ID
//            inst.put("GCMSenderId", "639533504053");

            inst.put("userID", ParseUser.getCurrentUser().getObjectId());
            inst.put("username", ParseUser.getCurrentUser().getUsername());
            inst.saveInBackground(e -> {

                // Now save the deviceToken
//                String deviceToken = FirebaseInstanceId.getInstance().getToken();
//                HashMap<String, Object> params = new HashMap<>(2);
//                params.put("installationId", inst.getObjectId());
//                params.put("deviceToken", deviceToken);
//                ParseCloud.callFunctionInBackground("setDeviceToken", params, (FunctionCallback<Boolean>) (success, e1) -> {
//                    if (e1 == null) {
//                        Log.i("log-", "REGISTERED FOR PUSH NOTIFICATIONS!");
//                    } else {
//                        Log.i(TAG, e1.getMessage());
//                    }
//                });
            });
        }
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_home;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        setUpContent();
    }


    private void setUpContent() {
        setUpViewPager();
        setUpTopNavigation();
    }

    private void setUpViewPager() {
        viewPager = findViewById(R.id.homeNSVP);
        adapter = new HomeScreenPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (bottomNavigationAdapter != null) bottomNavigationAdapter.setSelectedItemPosition(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setUpTopNavigation() {
        ViewTreeObserver vto = ry.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpTopNavigationRV();
                ry.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setUpTopNavigationRV() {
        bottomNavigationAdapter = new BottomNavigationAdapter(this,this, INITIAL_SELECTED_TAB_POSITION);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        ry.setAdapter(bottomNavigationAdapter);
        ry.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onTabSelected(int pos) {
        if (pos != 0){
            if (!isLogin()){
                //未登入
                noLoginDialog(this,this);
            }
        }
        viewPager.setCurrentItem(pos);
        return true;
    }

    @Override
    public void onSpecialTabSelected() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dnliConfirmBtn:
                closeListenerDialog();
                LoginSignActivity.show(this,null);
                break;
        }
    }
}
