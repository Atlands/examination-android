package com.oscar.writtenexamination.Activity.Edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Base.Activity;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.PhoneOrEmailFormatCheckUtils;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import butterknife.BindView;
import butterknife.OnClick;
import static com.oscar.writtenexamination.Base.Configurations.EDIT_PWD_TITLE;
import static com.oscar.writtenexamination.Base.Configurations.USER_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.USER_PASSCODE;
import static com.oscar.writtenexamination.Base.Configurations.USER_USERNAME;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.hideLoading;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;
import static com.oscar.writtenexamination.Base.Configurations.resEmail;
import static com.oscar.writtenexamination.Base.Configurations.resPwd;
import static com.oscar.writtenexamination.Base.Configurations.setText;
import static com.oscar.writtenexamination.Base.Configurations.showLoading;

public class EditPwdActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.epwToolbar)
    Toolbar mToolbar;
    @BindView(R.id.epwBackBtn)
    ImageView btnBack;
    @BindView(R.id.epwTitleTv)
    TextView tvTitle;

    //邮箱
    @BindView(R.id.epwEmailImg)
    ImageView imgEmail;
    @BindView(R.id.epwEmailEt)
    EditText etEmail;

    //新密码
    @BindView(R.id.epwPasswordImg)
    ImageView imgPwd;
    @BindView(R.id.epwPasswordEt)
    EditText etPwd;

    //确认密码
    @BindView(R.id.epwConfirmImg)
    ImageView imgConfirm;
    @BindView(R.id.epwConfirmEt)
    EditText etConfirm;

    //按钮
    @BindView(R.id.epwConfirmBtn)
    Button btnConfirm;

    //参数
    private int num = 0;
    private final int okNum = 3;
    //数据
    private String title;

    /**
     * 跳转入口
     */
    public static void show(Context context, Bundle bundle) {
        Intent intent = new Intent(context, EditPwdActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回True，错误返回False
     */
    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle == null){
            title = getString(R.string.titleModifyPwd);
        }else {
            title = bundle.getString(EDIT_PWD_TITLE);
        }
        return super.initArgs(bundle);
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_edit_pwd;
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
        btnConfirm.setActivated(false);
        btnConfirm.setBackgroundResource(R.drawable.bg_button_normal_mainone);
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        super.initData();
        setText(tvTitle,title);
        if (!isLogin()){
            //未登入
            noLoginDialog(this,this);
            num = 0;
            etEmail.setText("");
            imgEmail.setImageResource(resEmail[0]);
        }else {
            num++;
            etEmail.setText(ParseUser.getCurrentUser().getUsername());
            imgEmail.setImageResource(resEmail[1]);
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        setEdittext(etEmail,imgEmail,resEmail);
        setEdittext(etPwd,imgPwd,resPwd);
        setEdittext(etConfirm,imgConfirm,resPwd);
    }

    @OnClick(R.id.epwConfirmBtn)
    void OnConfirm(){
        showLoading(this);
        if (btnConfirm.isActivated()){
            dismissKeyboard();
            if (judgeInfor()){
                String username = etEmail.getText().toString();
                String pwd = etPwd.getText().toString();
                ParseQuery<ParseUser> query = ParseQuery.getQuery(USER_CLASS_NAME);
                query.whereEqualTo(USER_USERNAME,username);
                query.findInBackground((users, e) -> {
                    if (e == null){
                        if (users == null || users.size() == 0){
                            ToastUtils.showShortToast(mContext,R.string.tipsUserNotExit);
                        }else {
                            ParseUser user = users.get(0);
                            user.setPassword(pwd);
                            user.put(USER_PASSCODE,pwd);
                            user.saveInBackground(e1 -> {
                                if (e1 == null){
                                    noLoginDialog(this,this);
                                }else {
                                    ToastUtils.showLongToast(this,getString(R.string.tipsModifyFailed) +
                                            "：" + e1.getMessage());
                                }
                            });
                        }
                    }else {
                        ToastUtils.showLongToast(this,getString(R.string.tipsModifyFailed) +
                                "：" + e.getMessage());
                    }
                });
            }else {
                hideLoading();
                ToastUtils.showShortToast(this,R.string.tipsModifyFailed);
            }
        }else {
            hideLoading();
            ToastUtils.showShortToast(this,R.string.tipsPlzInputInfor);
        }
    }

    /**
     * 判断格式
     * @return true正确，false错误并提示错误
     */
    boolean judgeInfor(){
        String name = etEmail.getText().toString();
        String pwd = etPwd.getText().toString();
        String confirm = etConfirm.getText().toString();
        if (TextUtils.isEmpty(name)){
            etEmail.setError(getString(R.string.tipsPlzInputEmail));
            return false;
        }
        if (TextUtils.isEmpty(pwd)){
            etPwd.setError(getString(R.string.tipsPlzInputPwd));
            return false;
        }
        if (TextUtils.isEmpty(confirm)){
            etConfirm.setError(getString(R.string.tipsPlzInputConfirmPwd));
            return false;
        }
        if (!PhoneOrEmailFormatCheckUtils.isEmail(name)){
            etEmail.setError(getString(R.string.tipsPlzInputLegalEmail));
            return false;
        }
        if (pwd.length() < 6){
            etPwd.setError(getString(R.string.tipsPlzInputLegalPwd));
            return false;
        }
        if (confirm.length() < 6){
            etConfirm.setError(getString(R.string.tipsPlzInputLegalConfirmPwd));
            return false;
        }
        if (!pwd.equals(confirm)){
            etPwd.setError(getString(R.string.tipsPlzInputEqualPwd));
            etConfirm.setError(getString(R.string.tipsPlzInputEqualPwd));
            return false;
        }
        return true;
    }

    @OnClick(R.id.epwBackBtn)
    void onBack(){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dnliConfirmBtn:
                closeListenerDialog();
                break;
        }
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
        btnConfirm.setBackgroundResource(isOk ? R.drawable.bg_button_normal_activited_selector : R.drawable.bg_button_normal_mainone);
        btnConfirm.setActivated(isOk);
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
        imm.hideSoftInputFromWindow(etConfirm.getWindowToken(), 0);
    }
}
