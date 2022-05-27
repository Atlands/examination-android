package com.oscar.writtenexamination.Activity.LoginSign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Adapter.LoginSignPagerAdapter;
import com.oscar.writtenexamination.Adapter.TopNavigationAdapter;
import com.oscar.writtenexamination.Base.Activity;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginSignActivity extends Activity implements TopNavigationAdapter.TopNavigationListener{

    @BindView(R.id.lsToolbar)
    Toolbar mToolbar;
    @BindView(R.id.lsBackBtn)
    ImageView btnBack;
    @BindView(R.id.lsNavigationRy)
    RecyclerView topRy;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;

    //适配器
    LoginSignPagerAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    public static TopNavigationAdapter topNavigationAdapter;


    private static final int INITIAL_SELECTED_TAB_POSITION = 0;

    /**
     * 跳转入口
     */
    public static void show(Context context, Bundle bundle) {
        Intent intent = new Intent(context, LoginSignActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login_sign;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        //设置状态栏
        StatusBarUtil.setGradientColor(mActivity, mToolbar);
        UiUtils.setStatusBarDarkTheme(this, true);
        setUpContent();
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        super.initData();
    }

    private void setUpContent() {
        viewPager = findViewById(R.id.lsVP);
        setUpViewPager();
        setUpTopNavigation();
    }

    private void setUpViewPager() {
        adapter = new LoginSignPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                topNavigationAdapter.setSelectedItemPosition(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setUpTopNavigation() {
        ViewTreeObserver vto = topRy.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpTopNavigationRV();
                topRy.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @OnClick(R.id.lsBackBtn)
    void onBack(){
        finish();
    }

    private void setUpTopNavigationRV() {
        topNavigationAdapter = new TopNavigationAdapter(this,this, INITIAL_SELECTED_TAB_POSITION);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        topRy.setAdapter(topNavigationAdapter);
        topRy.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onTabSelected(int pos) {
        viewPager.setCurrentItem(pos);
        return true;
    }

    @Override
    public void onSpecialTabSelected() {

    }
}
