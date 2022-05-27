package com.oscar.writtenexamination.Fragment.Home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.Edit.EditInforActivity;
import com.oscar.writtenexamination.Activity.Edit.EditPwdActivity;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.parse.ParseUser;
import butterknife.BindView;
import butterknife.OnClick;

import static com.oscar.writtenexamination.Base.Configurations.EDIT_PWD_TITLE;
import static com.oscar.writtenexamination.Base.Configurations.USER_AVATAR;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.getParseAvator;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;
import static com.oscar.writtenexamination.Base.Configurations.setText;
import static com.oscar.writtenexamination.Base.Configurations.switchHomeVp;

public class MineFragment extends Fragment implements View.OnClickListener {

    //Toolbar
    @BindView(R.id.fmToolbar)
    Toolbar mToolbar;
    @BindView(R.id.fmTitleTv)
    TextView tvTitle;
    //登入登出
    @BindView(R.id.fmLoginBtn)
    TextView btnLogin;
    @BindView(R.id.fmLogoutBtn)
    Button btnLogout;

    @BindView(R.id.fmModifyItem)
    LinearLayout llModify;
    @BindView(R.id.fmAvator)
    ImageView imgAvator;
    @BindView(R.id.fmUsernameTv)
    TextView tvName;
    @BindView(R.id.fmModifyBtn)
    ImageView btnModify;

    @BindView(R.id.fmModifyInforItem)
    LinearLayout llModifyInfor;
    @BindView(R.id.fmMITv)
    TextView tvMI;

    @BindView(R.id.fmModifyPwdItem)
    LinearLayout llModifyPwd;
    @BindView(R.id.fmMPTv)
    TextView tvMP;

    //数据
    ParseUser currentUser;

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_mine;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        StatusBarUtil.setGradientColor(mActivity, mToolbar);
        UiUtils.setStatusBarDarkTheme(mActivity, true);
        //初始化标题
        setText(tvTitle, getString(R.string.titleMine));
    }

    @Override
    protected void initData() {
        if (!isNetConnect) {
            ToastUtils.showBlackCenterToast(mContext, getString(R.string.tipsNetworkConnectFailedPlzCheckIt));
            return;
        }
        llModify.setVisibility(isLogin() ? View.VISIBLE : View.GONE);
        btnLogin.setVisibility(isLogin() ? View.GONE : View.VISIBLE);
        btnLogout.setVisibility(isLogin() ? View.VISIBLE : View.GONE);
        if (isLogin()) {
            currentUser = ParseUser.getCurrentUser();
            String name = currentUser.getUsername();
            getParseAvator(imgAvator, currentUser, USER_AVATAR); //头像
            setText(tvName,name);
            setText(tvMI,getString(R.string.titleModifyInfor));
            setText(tvMP,getString(R.string.titleModifyPwd));
        } else {
            //未登入
            noLoginDialog(mContext, this);
            resetInfor();
        }
    }

    @OnClick(R.id.fmLogoutBtn)
    void onLogout(){
        if (isLogin()) {
            ParseUser.logOutInBackground(e -> {
                if (e == null) {
                    ToastUtils.showShortToast(mContext, R.string.tipsLogoutSuccess);
                    initData();
                    switchHomeVp(0);
                } else {
                    ToastUtils.showLongToast(mContext, getString(R.string.tipsLogoutFailedPlzRetry)
                            + "，失敗原因：" + e.getMessage());
                }
            });
        } else {
            //未登入
            noLoginDialog(mContext,this);
        }
    }

    @OnClick(R.id.fmLoginBtn)
    void onLogin(){
        LoginSignActivity.show(mContext,null);
    }

    @OnClick(R.id.fmModifyPwdItem)
    void onModifyPwd(){
        if (isLogin()) {
            Bundle bundle = new Bundle();
            bundle.putString(EDIT_PWD_TITLE, getString(R.string.titleModifyPwd));
            EditPwdActivity.show(mContext, bundle);
        } else {
            //未登入
            noLoginDialog(mContext,this);
        }
    }

    @OnClick({R.id.fmModifyBtn,R.id.fmModifyInforItem,R.id.fmModifyItem})
    void onModify(){
        if (isLogin()){
            EditInforActivity.show(mContext,null);
        }else {
            //未登入
            noLoginDialog(mContext,this);
        }
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) {
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 重置信息
     */
    void resetInfor() {
        imgAvator.setImageResource(R.drawable.default_avatar);
        tvName.setText("");
        tvName.setBackgroundResource(R.drawable.bg_nodata_normal);
        tvMI.setText("");
        tvMP.setText("");
        btnLogout.setVisibility(View.GONE);
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
