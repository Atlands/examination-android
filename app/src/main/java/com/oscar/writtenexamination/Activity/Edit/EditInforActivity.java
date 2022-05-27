package com.oscar.writtenexamination.Activity.Edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Base.Activity;
import com.oscar.writtenexamination.Fragment.GalleryFragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.FileUtils;
import com.oscar.writtenexamination.Utils.PhoneOrEmailFormatCheckUtils;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.oscar.writtenexamination.Utils.UriUtils;
import com.parse.ParseUser;
import java.io.File;
import java.io.IOException;
import butterknife.BindView;
import butterknife.OnClick;

import static com.oscar.writtenexamination.Base.Configurations.KEY_REQUEST_CODE_CAMERA;
import static com.oscar.writtenexamination.Base.Configurations.PERMISSIONS;
import static com.oscar.writtenexamination.Base.Configurations.USER_AVATAR;
import static com.oscar.writtenexamination.Base.Configurations.bottomDialogPhoto;
import static com.oscar.writtenexamination.Base.Configurations.closeBottomDialogPhoto;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.getParseAvator;
import static com.oscar.writtenexamination.Base.Configurations.hideLoading;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;
import static com.oscar.writtenexamination.Base.Configurations.resEmail;
import static com.oscar.writtenexamination.Base.Configurations.saveParseImage;
import static com.oscar.writtenexamination.Base.Configurations.showBottomDialogPhoto;
import static com.oscar.writtenexamination.Base.Configurations.showLoading;
import static com.oscar.writtenexamination.Base.Configurations.verifyPermissions;
import static com.oscar.writtenexamination.Utils.FileUtils.createOriImageFile;

public class EditInforActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.eiToolbar)
    Toolbar mToolbar;
    @BindView(R.id.eiBackBtn)
    ImageView btnBack;

    @BindView(R.id.eiAvator)
    ImageView imgAvator;

    @BindView(R.id.eiEmailImg)
    ImageView imgEmail;
    @BindView(R.id.eiEmailEt)
    EditText etEmail;

    @BindView(R.id.eiConfirmBtn)
    Button btnConfirm;

    //参数
    private int num = 0;
    private final int okNum = 1;
    private boolean isAvator = false;
    //数据
    ParseUser user;
    String currentEmail;

    /**
     * 跳转入口
     */
    public static void show(Context context, Bundle bundle) {
        Intent intent = new Intent(context, EditInforActivity.class);
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
        return R.layout.activity_edit_infor;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        //设置状态栏
        StatusBarUtil.setGradientColor(this, mToolbar);
        UiUtils.setStatusBarDarkTheme(this, true);
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        num = 0;
        btnConfirm.setActivated(false);
        btnConfirm.setBackgroundResource(R.drawable.bg_button_normal_mainone);
        if (isLogin()) {
            user = ParseUser.getCurrentUser();
            currentEmail = user.getEmail();
            if (!TextUtils.isEmpty(currentEmail)) {
                imgEmail.setImageResource(resEmail[1]);
                etEmail.setText(currentEmail);
                num++;
            }
            getParseAvator(imgAvator, user, USER_AVATAR);
            if (num > 0) setButton();
        } else {
            //未登入
            noLoginDialog(this,this);
        }
    }


    @Override
    protected void setListener() {
        super.setListener();
        setEdittext(etEmail, imgEmail, resEmail);
    }

    @OnClick(R.id.eiAvator)
    void onAvator() {
        if (isLogin()) {
            if (bottomDialogPhoto == null || !bottomDialogPhoto.isShowing()){
                showBottomDialogPhoto(mContext,this);
            }
        } else {
            //未登入
            noLoginDialog(this,this);
        }
    }

    @OnClick(R.id.eiConfirmBtn)
    void onConfirm() {
        if (isLogin()) {
            if (btnConfirm.isActivated()) {
                showLoading(this);
                if (judgeInfor()) {
                    String email = etEmail.getText().toString();
                    user.setUsername(email);
                    user.setEmail(email);
                    if (isAvator){
                        Bitmap bitmap = ((BitmapDrawable) imgAvator.getDrawable()).getBitmap();
                        saveParseImage(bitmap, user, USER_AVATAR,50);
                    }
                    user.saveInBackground(e -> {
                        hideLoading();
                        if (e == null){
                            ToastUtils.showShortToast(this,R.string.tipsModifySuccess);
                            finish();
                        }else {
                            ToastUtils.showShortToast(this,
                                    getString(R.string.tipsModifyFailedPlzRetry) + "：" + e.getMessage());
                        }
                    });
                } else {
                    hideLoading();
                    ToastUtils.showShortToast(this, R.string.tipsModifyFailed);
                }
            } else {
                ToastUtils.showShortToast(this, R.string.tipsPlzInputInfor);
            }
        } else {
            //未登入
            noLoginDialog(this,this);
        }
    }

    @OnClick(R.id.eiBackBtn)
    void onBack() {
        if (isAvator) {
            showEditAvatorAlert();
        }
        finish();
    }

    boolean judgeInfor() {
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.tipsPlzInputEmail));
            return false;
        }
        if (!PhoneOrEmailFormatCheckUtils.isEmail(email)) {
            etEmail.setError(getString(R.string.tipsPlzInputLegalEmail));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isAvator){
            showEditAvatorAlert();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * 打开头像修改提示框
     */
    private void showEditAvatorAlert() {
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(mContext);
        alert.setMessage(R.string.tipsAskAvatorAlreadyChange)
                .setTitle(R.string.tipsWarmReminder);

        alert.setPositiveButton(R.string.buttonContinueModify, (dialog, which) -> dialog.dismiss());

        alert.setCancelable(false)
                .setNegativeButton(R.string.buttonCancleModify, (dialog, which) ->{
                    dialog.dismiss();
                    finish();
                })
                .setIcon(R.drawable.ic_logo_exmination);
        alert.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picGallary:
                closeBottomDialogPhoto();
                //设置图片数据
                new GalleryFragment()
                        .setListener(this::setImagData).show(getSupportFragmentManager(),GalleryFragment.class.getName());
//                openGallery();
                break;
            case R.id.picCamera:
                if (verifyPermissions(mActivity,PERMISSIONS[2])){
                    closeBottomDialogPhoto();
                    openCamera();
                }
                break;
            case R.id.dnliConfirmBtn:
                closeListenerDialog();
                LoginSignActivity.show(this,null);
                break;
        }
    }

    File file;
    Uri mUri;
    String imgPath;
    /**
     * 打开相机
     */
    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            file = createOriImageFile(this);
            imgPath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                mUri = Uri.fromFile(file);
            } else {
                mUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(cameraIntent, KEY_REQUEST_CODE_CAMERA);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // 相机获取图片
            if (requestCode == KEY_REQUEST_CODE_CAMERA) {
                //根据Uri得到
                Glide.with(imgAvator.getContext())
                        .load(mUri)
                        .thumbnail(0.1f)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .dontAnimate()
                        .centerCrop()
                        .apply(new RequestOptions().centerCrop())
                        .error(R.drawable.default_avatar)
                        .into(imgAvator);
                isAvator = true;
            }
        }else {
            isAvator = false;
            ToastUtils.showShortToast(this,getString(R.string.buttonCancle));
        }
        setButton();
    }

    /**
     * 设置头像数据
     * @param path 路径
     */
    private void setImagData(String path) {
        double size = FileUtils.getFileOrFilesSize(path,3);
        if (size > 3){
            isAvator = false;
            ToastUtils.showShortToast(mContext,"圖片超過3MB上限");
        }else {
            Uri uri = UriUtils.PathToUri(path);
            Glide.with(imgAvator.getContext())
                    .load(uri)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .centerCrop()
                    .apply(new RequestOptions().centerCrop())
                    .error(R.drawable.default_avatar)
                    .into(imgAvator);
            isAvator = true;
        }
        setButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public class TextWatcher implements android.text.TextWatcher {

        private CharSequence temp;
        private EditText editText;
        private ImageView img;
        private int[] resId;
        int editStart;
        int editEnd;
        private boolean isEmpty;

        TextWatcher(int[] resId, ImageView img, EditText editText) {
            this.resId = resId;
            this.img = img;
            this.editText = editText;
            this.isEmpty = editText.getText().toString().isEmpty();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
            temp = s;
            isEmpty = editText.getText().toString().isEmpty();
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
        boolean isOk = ((num == okNum) || isAvator);
        btnConfirm.setBackgroundResource(isOk ? R.drawable.bg_button_normal_activited_selector : R.drawable.bg_button_normal_mainone);
        btnConfirm.setActivated(isOk);
    }


    /**
     * 设置输入框监听器
     *
     * @param editText 输入框控件
     * @param img      图片控件
     * @param resId    图片资源
     */
    private void setEdittext(EditText editText, ImageView img, int[] resId) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (editText.getText().toString().length() == 0) {
                img.setImageResource(resId[hasFocus ? 1 : 0]);
            }
        });
        editText.addTextChangedListener(new TextWatcher(resId, img, editText));
    }
}
