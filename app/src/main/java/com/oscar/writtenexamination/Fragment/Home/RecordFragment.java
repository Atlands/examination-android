package com.oscar.writtenexamination.Fragment.Home;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.Fragment.Record.PurchasedFragment;
import com.oscar.writtenexamination.Fragment.Record.SubjectFragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.oscar.writtenexamination.Widget.ChildViewPager;

import butterknife.BindView;

import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;

public class RecordFragment extends Fragment implements View.OnClickListener {

    //Toolbar
    @BindView(R.id.frToolbar)
    Toolbar mToolbar;

    //主要
    @BindView(R.id.frTablayout)
    TabLayout tabLayout;
    @SuppressLint("StaticFieldLeak")
    static ChildViewPager viewPager;

    //参数
    private String[] tabs = {"已購買","账单"};
    private Fragment[] fragments = new Fragment[2];
    //适配器
    private FragmentPagerAdapter mAdapter;
    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_record;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        StatusBarUtil.setGradientColor(mActivity, mToolbar);
        UiUtils.setStatusBarDarkTheme(mActivity, true);
        viewPager = root.findViewById(R.id.frViewPager);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        if (!isNetConnect) {
            ToastUtils.showShortToast(mContext, R.string.tipsNetworkConnectFailedPlzCheckIt);
            return;
        }
        if (isLogin()){
            //初始化tab
            initTab();
        }else {
            //未登入
            noLoginDialog(mContext, this);
        }
    }

    /**
     * 初始化标签
     */
    private void initTab() {
        fragments[0] = SubjectFragment.newInstance();
        fragments[1] = PurchasedFragment.newInstance();
        viewPager.setOffscreenPageLimit(0);
        //适配器（容器都需要适配器）
        if (mAdapter == null){
            mAdapter = new FragmentPagerAdapter(getFragmentManager()) {
                //选中的item
                @Override
                public Fragment getItem(int position) {
                    return fragments[position];
                }

                @Override
                public int getCount() {
                    return fragments.length;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return tabs[position];
                }
            };
            viewPager.setAdapter(mAdapter);
        }
        tabLayout.setupWithViewPager(viewPager,true);
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) {

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dnliConfirmBtn:
                closeListenerDialog();
                LoginSignActivity.show(mContext,null);
                break;
        }
    }
}
