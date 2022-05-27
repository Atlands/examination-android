package com.oscar.writtenexamination.Fragment.LoginSign;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.PhoneOrEmailFormatCheckUtils;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.oscar.writtenexamination.Base.Configurations.USER_PASSCODE;
import static com.oscar.writtenexamination.Base.Configurations.USER_PURCHASED_CATEGORY;
import static com.oscar.writtenexamination.Base.Configurations.USER_PURCHASED_SUBJECT;
import static com.oscar.writtenexamination.Base.Configurations.hideTextLoading;
import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.resEmail;
import static com.oscar.writtenexamination.Base.Configurations.resPwd;
import static com.oscar.writtenexamination.Base.Configurations.showTextLoading;

public class SignFragment extends Fragment {

    //APP标题
    @BindView(R.id.fsAppNameTv)
    TextView tvAppName;
    @BindView(R.id.fsAppIntroTv)
    TextView tvAppIntro;

    //电邮地址
    @BindView(R.id.fsEmailImg)
    ImageView imgEmail;
    @BindView(R.id.fsEmailEt)
    EditText etEmail;

    //密码
    @BindView(R.id.fsPasswordImg)
    ImageView imgPwd;
    @BindView(R.id.fsPasswordEt)
    EditText etPwd;
    //确认密码
    @BindView(R.id.fsPwdConfirmImg)
    ImageView imgPwdConfirm;
    @BindView(R.id.fsPwdConfirmEt)
    EditText etPwdConfirm;

    //按钮
    @BindView(R.id.fsSignupBtn)
    Button btnSign;

    //参数
    private int num = 0;
    private final int okNum = 3;
    //数据
    private ParseUser user;

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_signup;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        btnSign.setActivated(false);
        btnSign.setBackgroundResource(R.drawable.bg_button_normal_mainone);
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
        setEdittext(etEmail,imgEmail,resEmail);
        setEdittext(etPwd,imgPwd,resPwd);
        setEdittext(etPwdConfirm,imgPwdConfirm,resPwd);
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) {

    }

    /**
     * 设置APP信息
     */
    void setAppInfor(){

    }

    @OnClick(R.id.fsSignupBtn)
    void onSign(){
        showTextLoading(mContext,getString(R.string.tipsLoadingSignup),false);
        dismissKeyboard();
        if (btnSign.isActivated()){
            if (judgeInfor()){
                setParseUser();
                user.signUpInBackground(e -> {
                    hideTextLoading();
                    user = null;
                    if (e == null){
                        ToastUtils.showLongToast(mContext,
                                getString(R.string.tipsSignupSuccess) + "，欢迎使用" + getString(R.string.app_name));
                        LoginSignActivity.viewPager.setCurrentItem(0);
                        LoginSignActivity.topNavigationAdapter.setSelectedItemPosition(0);
                    }else {
                        int code = e.getCode();
                        if (code == 202){
                            etEmail.setError(getString(R.string.tipsPlzInputOterEmail));
                        }else {
                            ToastUtils.showLongToast(mContext,
                                    getString(R.string.tipsSignupFailed) + "：" + e.getMessage());
                        }
                    }
                });
            }else {
                hideTextLoading();
                ToastUtils.showShortToast(mContext,R.string.tipsSignupFailed);
            }
        }else {
            hideTextLoading();
            ToastUtils.showShortToast(mContext,R.string.tipsPlzInputInfor);
        }
    }

    /**
     * 插入用户数据
     */
    private void setParseUser() {
        user = new ParseUser();
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();
        user.setUsername(email);
        user.setPassword(pwd);
        user.setEmail(email);
        user.put(USER_PASSCODE, pwd);
        user.put(USER_PURCHASED_CATEGORY,new ArrayList<>());
        user.put(USER_PURCHASED_SUBJECT,new ArrayList<>());
    }

    /**
     * 判断格式
     * @return true正确，false错误并提示错误
     */
    boolean judgeInfor(){
        String email = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();
        String confirm = etPwdConfirm.getText().toString();
        if (TextUtils.isEmpty(email)){
            etEmail.setError(getString(R.string.tipsSendEmailFailed));
            return false;
        }
        if (TextUtils.isEmpty(pwd)){
            etPwd.setError(getString(R.string.tipsPlzInputPwd));
            return false;
        }
        if (TextUtils.isEmpty(confirm)){
            etPwdConfirm.setError(getString(R.string.tipsPlzInputConfirmPwd));
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
        if (confirm.length() < 6){
            etPwdConfirm.setError(getString(R.string.tipsPlzInputLegalConfirmPwd));
            return false;
        }
        if (!pwd.equals(confirm)){
            etPwdConfirm.setError(getString(R.string.tipsPlzInputEqualPwd));
            return false;
        }
        return true;
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
        btnSign.setBackgroundResource(isOk ? R.drawable.bg_button_normal_activited_selector : R.drawable.bg_button_normal_mainone);
        btnSign.setActivated(isOk);
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
        imm.hideSoftInputFromWindow(etPwdConfirm.getWindowToken(), 0);
    }
}
