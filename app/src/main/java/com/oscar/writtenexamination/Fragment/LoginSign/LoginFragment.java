package com.oscar.writtenexamination.Fragment.LoginSign;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.oscar.writtenexamination.Activity.Edit.EditPwdActivity;
import com.oscar.writtenexamination.Activity.Home.HomeActivity;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.PhoneOrEmailFormatCheckUtils;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.parse.ParseACL;
import com.parse.ParseUser;
import butterknife.BindView;
import butterknife.OnClick;
import static com.oscar.writtenexamination.Base.Configurations.EDIT_PWD_TITLE;
import static com.oscar.writtenexamination.Base.Configurations.USER_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.hideLoading;
import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.resEmail;
import static com.oscar.writtenexamination.Base.Configurations.resPwd;
import static com.oscar.writtenexamination.Base.Configurations.resUser;
import static com.oscar.writtenexamination.Base.Configurations.showLoading;
import static com.oscar.writtenexamination.Base.Configurations.showResetPwdDialog;

public class LoginFragment extends Fragment {

    //APP标题
    @BindView(R.id.flAppNameTv)
    TextView tvAppName;
    @BindView(R.id.flAppIntroTv)
    TextView tvAppIntro;

    //用户名
    @BindView(R.id.flEmailImg)
    ImageView imgEmail;
    @BindView(R.id.flEmailEt)
    EditText etEmail;

    //密码
    @BindView(R.id.flPasswordImg)
    ImageView imgPwd;
    @BindView(R.id.flPasswordEt)
    EditText etPwd;

    //按钮
    @BindView(R.id.flLoginBtn)
    Button btnLogin;
    @BindView(R.id.flForgotPwdBtn)
    TextView btnForgot;

    //参数
    private int num = 0;
    private final int okNum = 2;

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }


    /**
     * 初始化控件
     */
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        btnLogin.setActivated(false);
        btnLogin.setBackgroundResource(R.drawable.bg_button_normal_mainone);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        if (!isNetConnect){
            ToastUtils.showShortToast(mContext,R.string.tipsNetworkConnectFailedPlzCheckIt);
            return;
        }
        //设置App信息
        setAppInfor();
    }

    @Override
    protected void setListener() {
        super.setListener();
        setEdittext(etEmail,imgEmail,resEmail);
        setEdittext(etPwd,imgPwd,resPwd);
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) {

    }

    /**
     * 设置APP信息
     */
    void setAppInfor(){

    }

    /**
     * 登录方法
     */
    @OnClick(R.id.flLoginBtn)
    void onLogin(){
        showLoading(mContext);
        dismissKeyboard();
        if (btnLogin.isActivated()){
            if (judgeInfor()){
                String email = etEmail.getText().toString();
                String pwd  = etPwd.getText().toString();
                ParseUser.logInInBackground(email, pwd,
                        (user, e) -> {
                            hideLoading();
                            if (e == null){
                                if (user == null){
                                    ToastUtils.showShortToast(mContext,getString(R.string.tipsLoginFailedPlzRetry));
                                    return;
                                }
                                mActivity.finish();
                                HomeActivity.show(mContext,null);
                                ToastUtils.showShortToast(mContext,getString(R.string.tipsLoginSuccess));
                                ParseACL parseACL = new ParseACL(ParseUser.getCurrentUser());
                                parseACL.setPublicReadAccess(true);
                                ParseUser.getCurrentUser().setACL(parseACL);
                            }else {
                                ToastUtils.showLongToast(mContext,getString(R.string.tipsLoginFailed) + "：" + e.getMessage());
                            }
                        });
            }else {
                hideLoading();
                ToastUtils.showShortToast(mContext,R.string.tipsLoginFailed);
            }
        }else {
            hideLoading();
            ToastUtils.showShortToast(mContext,R.string.tipsPlzInputInfor);
        }
    }

    /**
     * 判断格式
     * @return true正确，false错误并提示错误
     */
    boolean judgeInfor(){
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(email)){
            etEmail.setError(getString(R.string.tipsPlzInputEmail));
            return false;
        }
        if (TextUtils.isEmpty(pwd)){
            etPwd.setError(getString(R.string.tipsPlzInputPwd));
            return false;
        }
        if (!PhoneOrEmailFormatCheckUtils.isEmail(email)){
            etEmail.setError(getString(R.string.tipsPlzInputLegalEmail));
            return false;
        }
        if (pwd.length() < 6){
            etPwd.setError(getString(R.string.tipsPlzInputLegalPwd));
            return false;
        }
        return true;
    }

    /**
     * 跳转忘记密码
     */
    @OnClick(R.id.flForgotPwdBtn)
    void onForgot(){
        showResetPwdDialog(mContext);
    }

    public class TextWatcher implements android.text.TextWatcher {

        private CharSequence temp;
        private EditText editText;
        private ImageView img;
        private int[] resId;
        int editStart;
        int editEnd;
        private boolean isEmpty;

        TextWatcher(int[] resId,ImageView img,EditText editText){
            this.resId = resId;
            this.img = img;
            this.editText = editText;
            this.isEmpty = editText.getText().toString().isEmpty();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            temp = s;
            isEmpty = editText.getText().toString().isEmpty();
            setButton();
        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            editStart = editText.getSelectionStart();
            editEnd = editText.getSelectionEnd();
            if (temp.length() > 0) {
                img.setImageResource(resId[1]);
            }
            boolean empty = editText.getText().toString().isEmpty();
            //开始为空
            if (isEmpty){
                if (!empty && num < okNum){
                    num++;
                }
            }else {
                if (empty && num > 0){
                    num--;
                }
            }
            isEmpty = empty;
            //设置按鈕
            setButton();
        }
    }



    /**
     * 设置按鈕
     */
    private void setButton(){
        boolean isOk = (num == okNum);
        btnLogin.setBackgroundResource(isOk ? R.drawable.bg_button_normal_activited_selector : R.drawable.bg_button_normal_mainone);
        btnLogin.setActivated(isOk);
    }

    /**
     * 设置输入框监听器
     * @param editText 输入框控件
     * @param img 图片控件
     * @param resId 图片资源
     */
    private void setEdittext(EditText editText,ImageView img,int[] resId){
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (editText.getText().toString().length() == 0) {
                img.setImageResource(resId[hasFocus ? 1 : 0]);
            }
        });
        editText.addTextChangedListener(new TextWatcher(resId,img,editText));
    }

    /**
     * 软键盘消失
     */
    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)(mContext.getSystemService(Context.INPUT_METHOD_SERVICE));
        assert imm != null;
        imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etPwd.getWindowToken(), 0);
    }
}
