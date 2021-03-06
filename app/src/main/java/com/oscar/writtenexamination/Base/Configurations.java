package com.oscar.writtenexamination.Base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.blankj.utilcode.util.ColorUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.oscar.writtenexamination.Activity.CategorySubject.CategoryActivity;
import com.oscar.writtenexamination.Activity.CategorySubject.SubjectActivity;
import com.oscar.writtenexamination.Activity.Examination.ExaminationActivity;
import com.oscar.writtenexamination.Activity.Home.HomeActivity;
import com.oscar.writtenexamination.Adapter.BottomNavigationAdapter;
import com.oscar.writtenexamination.Bean.CategoryBean;
import com.oscar.writtenexamination.Bean.SubjectBean;
import com.oscar.writtenexamination.Dialog.CheckVersionDialog;
import com.oscar.writtenexamination.Dialog.ForgetPwdDialog;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.PhoneOrEmailFormatCheckUtils;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

public  class Configurations extends Application {

    /**
     * Parse????????????
     */
    public static final String PARSE_APP_ID = "lnf6YAsqgMG1CA7PfnceXpzE8DYhh1y8SOx3kFPb";
    public static final String PARSE_CLIENT_KEY = "yHIXC1euZfN47chYfzxmW4C1TZNGLFnLm7xhfkJx";
    boolean isParseInitialized = false;

    /**
     * ????????????
     */
    public static String OBJECT_ID = "objectId";
    public static String UPDATED_AT = "updatedAt";
    public static String CREATED_AT = "createdAt";
    public static String USER_POINTER = "userPointer";
    public static String CATEGORY_POINTER = "categoryPointer";
    public static String SUBJECT_POINTER = "subjectPointer";
    public static String QUESTION_POINTER = "questionPointer";

    /**
     * ??????bean
     */
    public static String ANSWER_BEAN = "AnswerBean";
    public static String CATEGORY_BEAN = "CategoryBean";
    public static String QUESTION_BEAN = "QuestionBean";
    public static String SUBJECT_BEAN = "SubjectBean";
    /**
     * ?????????
     */
    public static String USER_CLASS_NAME = "_User";
    //string
    public static String USER_USERNAME = "username";
    public static String USER_EMAIL = "email";
    public static String USER_PASSWORD = "password";
    public static String USER_PASSCODE = "passcode";
    //array
    public static String USER_PURCHASED_CATEGORY = "purchasedCategory";
    public static String USER_PURCHASED_SUBJECT = "purchasedSubject";
    //boolean
    public static String USER_EMAIL_VERIFIED = "emailVerified";
    public static String USER_IS_DISABLE = "isDisable";
    //File
    public static String USER_AVATAR = "avatar";

    /**
     * ?????????
     */
    public static String ANSWERBANK_CLASS_NAME = "AnswerBank";
    //string
    public static String ANSWERBANK_OPTION_TEXT = "optionText";
    //boolean
    public static String ANSWER_OPTION_SHOW_IMAGE = "optionShowImage";
    public static String ANSWER_OPTIONS_CORRECT = "optionIsCorrect";
    //File
    public static String ANSWERBANK_OPTION_IMAGE = "optionImage";

    /**
     * ?????????
     */
    public static String CATEGORY_CLASS_NAME = "Category";
    //string
    public static String CATEGORY_NAME = "categoryName";
    public static String CATEGORY_INFO = "categoryInfo";
    public static String CATEGORY_PRODUCT_INFO = "categoryProductInfo";
    public static String CATEGORY_GOOGLE_PRODUCT_ID = "googleProductID";
    //boolean
    public static String CATEGORY_IS_HIDE = "isHide";
    //number
    public static String CATEGORY_GOOGLE_ORIGINAL_PRICE = "googleOriginalPrice";
    public static String CATEGORY_GOOGLE_SPECIAL_PRICE = "googleSpecialPrice";
    //File
    public static String CATEGORY_IMG = "categoryImg";

    /**
     * ?????????
     */
    public static String SUBJECT_CLASS_NAME = "Subject";
    //string
    public static String SUBJECT_ID = "subjectId";
    public static String SUBJECT_NAME = "subjectName";
    public static String SUBJECT_INFO = "subjectInfo";
    public static String SUBJECT_GOOGLE_PRODUCT_ID = "subjectProductID";
    public static String SUBJECT_PRODUCT_INFO = "subjectProductInfo";
    //boolean
    public static String SUBJECT_HIDE_FOR_NEW_USER = "subjectHideForNewUser";
    public static String SUBJECT_SHOW_PRODUCT_IMG = "subjectShowProductImg";
    //number
    public static String SUBJECT_SINGLE_COUNT = "singleCount";
    public static String SUBJECT_PASS_COUNT = "passCount";
    public static String SUBJECT_GOOGLE_ORIGINAL_PRICE = "subjectGoogleOriginalPrice";
    public static String SUBJECT_GOOGLE_SPECIAL_PRICE = "subjectGoogleSpecialPrice";
    public static String SUBJECT_SHOW_AD_COUNT = "showAdCount";
    //File
    public static String SUBJECT_PRODUCT_IMG = "subjectProductImg";

    /**
     * ?????????
     */
    public static String CONFIG_CLASS_NAME = "Config";
    //string
    public static String CONFIG_SUPPORT_WHATS_APP = "supportWhatsApp";
    public static String CONFIG_SUPPORT_EMAIL = "supportEmail";
    public static String CONFIG_ANDROID_NEW_VERSION = "androidNewVersion";
    public static String CONFIG_HOME_NAME = "homeName";
    public static String CONFIG_GOOGLE_UPDATE_LINK = "googleUpdateLink";

    /**
     * ???????????????
     */
    public static String NEWS_CLASS_NAME = "News";
    //string
    public static String NEWS_TOPIC = "newsTopic";
    public static String NEWS_DETAILS = "newsDetails";
    //boolean
    public static String NEWS_IS_HIDE = "newsIsHide";

    /**
     * ???????????????
     */
    public static String PURCHASED_HISTORY_CLASS_NAME = "PurchasedHistory";
    //number
    public static String PURCHASED_HISTORY_PRICE = "price";

    /**
     * ?????????
     */
    public static String QUESTION_CLASS_NAME = "QuestionBank";
    //string
    public static String QUESTION_QUESTION = "question";
    public static String QUESTION_CORRECT_INFO = "questionCorrectInfo";
    //boolean
    public static String QUESTION_SHOW_IMG = "questionShowImg";
    public static String QUESTION_SHOW_CORRECT_INFO = "questionShowCorrectInfo";
    public static String QUESTION_SHOW_CORRECT_IMG = "questionShowCorrectIm";
    //File
    public static String QUESTION_IMAGE = "questionImage";
    public static String QUESTION_CORRECT_IMG = "questionCorrectImg";

    /**
     * ?????????
     */
    public static String WRONG_BANK_CLASS_NAME = "WrongBank";
    //array
    public static String WRONG_BANK_QUESTIONS = "wrongQuestions";
    /**
     * ????????????
     */
    public static String GET_PRODUCTS = "getProducts";
    public static String GET_RANDOM_QUESTIONS = "getRandomQuestions";
    public static String GET_PURCHASED_HISTORY = "getPurchasedHistory";

    /**
     * ?????????
     */
    public static String SCROLL_AD_CLASS_NAME = "ScrollAd";
    //string
    public static String SCROLL_AD_TITLE = "scrollTitle";
    public static String SCROLL_AD_LINK = "scrollLink";
    //File
    public static String SCROLL_AD_IMAGE = "scrollImage";

    //?????????
    public static String EDIT_PWD_TITLE = "editPwdTitle";
    public static String EXAMINATION_STATUS = "ExaminationStaus";
    //????????????
    public static int EXAMINATION_STATUS_FOR_FREE = 1;
    public static int EXAMINATION_STATUS_FOR_PURCHASED = 2;
    //??????????????????
    public static SharedPreferences prefs;

    //??????????????????
    public static int netType = -1;
    public static boolean isNetConnect = false;
    public static int layoutNotNetConnect = R.layout.layout_net_not_connect;

    // ?????????????????????
    public static final int KEY_REQUEST_CODE_CAMERA = 0;
    public static final int KEY_REQUEST_CODE_GALLARY = 1;
    public static final int KEY_REQUEST_CODE_COPY = 2;
    //???????????????0-???????????????1-???????????????2-????????????
    public static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
    //????????????
    public static int[] resUser = {R.drawable.ic_user_bfgray, R.drawable.ic_user_normal};
    public static int[] resPwd = {R.drawable.ic_password_bfgray, R.drawable.ic_password_normal};
    public static int[] resEmail = {R.drawable.ic_email_bfgray, R.drawable.ic_email_normal};

    public static String[] upperCharacters ={"A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    //????????????List
    public static List<ParseObject> newsList = new ArrayList<>();

    private static Configurations instance;
    public static ParseObject configOoject;

    public static Configurations getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (!isParseInitialized) {
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId(PARSE_APP_ID)
                    .clientKey(PARSE_CLIENT_KEY)
                    .server("https://parseapi.back4app.com")
                    .build()
            );
            Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
            ParseUser.enableAutomaticUser();
            isParseInitialized = true;
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
        }
    }



    public static AlertDialog normalLoadDialog,textLoadDialog;
    public static AlertDialog.Builder db;
    public static LayoutInflater inflater;

    public static void initDialog(Context context){
        db = new AlertDialog.Builder(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * ?????????????????????
     * @param context ?????????
     */
    public static void showLoading(Context context) {
        initDialog(context);
        assert inflater != null;
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_normal_loading, null);
        db.setView(view);
        db.setCancelable(false);
        normalLoadDialog = db.create();
        normalLoadDialog.show();
    }

    /**
     * ?????????????????????
     */
    public static void hideLoading() {
        if (normalLoadDialog == null) return;
        normalLoadDialog.dismiss();
    }

    /**
     * ???????????????????????????
     * @param context ?????????
     * @param content ??????
     * @param isCancle ??????????????????
     */
    public static void showTextLoading(Context context,String content,boolean isCancle){
        initDialog(context);
        assert inflater != null;
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_content_loading, null);
        TextView tvContent = view.findViewById(R.id.dclContentTv);
        boolean isContent = TextUtils.isEmpty(content);
        tvContent.setVisibility(isContent ? View.GONE : View.VISIBLE);
        if (!isContent) setText(tvContent,content);
        db.setView(view);
        db.setCancelable(isCancle);
        textLoadDialog = db.create();
        textLoadDialog.show();
    }

    /**
     * ???????????????????????????
     */
    public static void hideTextLoading(){
        if (textLoadDialog == null) return;
        textLoadDialog.dismiss();
    }

    /**
     * ????????????????????????
     *
     * @param context ?????????
     */
    public static void showResetPwdDialog(final Context context) {
        ForgetPwdDialog forgetPwdDialog = new ForgetPwdDialog(context, R.style.alert_dialog);
        forgetPwdDialog
                .setTitle(context.getString(R.string.buttonForgotPwd))
                .setConfirm(context.getString(R.string.buttonConfirm), (myDialog, editText) -> {
                    showLoading(context);
                    String email = editText.getText().toString();
                    if (!TextUtils.isEmpty(email)) {
                        if (!PhoneOrEmailFormatCheckUtils.isEmail(email)) {
                            editText.setError(context.getString(R.string.tipsPlzInputLegalEmail));
                            return;
                        }
                        ParseQuery<ParseUser> query = ParseQuery.getQuery(USER_CLASS_NAME);
                        query.whereEqualTo(USER_USERNAME,email);
                        query.findInBackground((users, e) -> {
                            if (e == null){
                                if (users == null || users.size() == 0){
                                    editText.setError(context.getString(R.string.tipsUserNotExit));
                                    ToastUtils.showShortToast(context,context.getString(R.string.tipsSendEmailFailed));
                                }else {
                                    ParseUser.requestPasswordResetInBackground(email, e1 -> {
                                        if (e1 == null){
                                            forgetPwdDialog.dismiss();
                                            ToastUtils.showLongToast(context,context.getString(R.string.tipsSendResetEmailSuccess));
                                        }else {
                                            ToastUtils.showLongToast(context,context.getString(R.string.tipsSendEmailFailed) +
                                                    "???" + e1.getMessage());
                                        }
                                    });
                                }
                            }else {
                                ToastUtils.showLongToast(context,context.getString(R.string.tipsSendEmailFailed) +
                                        "???" + e.getMessage());
                            }
                        });
                    } else {
                        editText.setError(context.getString(R.string.tipsPlzInputEmail));
                    }
                });
        forgetPwdDialog.setCanceledOnTouchOutside(true);
        forgetPwdDialog.show();
    }

    public static Dialog bottomDialogPhoto;

    /**
     * ????????????????????????dialog
     * @param context ?????????
     */
    public static void showBottomDialogPhoto(Context context, View.OnClickListener listener) {
        bottomDialogPhoto = new Dialog(context, R.style.BottomDialog);//???????????????
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_photo_bottom, null);
        bottomDialogPhoto.setContentView(view);

        TextView btnGallery = view.findViewById(R.id.picGallary);
        TextView btnCamera = view.findViewById(R.id.picCamera);
        TextView btnCancle = view.findViewById(R.id.picCancle);
        btnGallery.setOnClickListener(listener);
        btnCamera.setOnClickListener(listener);
        btnCancle.setOnClickListener(view1 -> closeBottomDialogPhoto());

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.bottomMargin = dp2px(8f);
        view.setLayoutParams(params);

        Window window = bottomDialogPhoto.getWindow();
        bottomDialogPhoto.setCanceledOnTouchOutside(true);
        if (window != null){
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.BottomDialog_Animation);
        }
        bottomDialogPhoto.show();
    }

    /**
     * ????????????????????????dialog
     */
    public static void closeBottomDialogPhoto(){
        if (bottomDialogPhoto == null || !bottomDialogPhoto.isShowing())  return;
        bottomDialogPhoto.dismiss();
    }

    public static Dialog listenerDialog;

    /**
     * ????????????????????????dialog
     * @param context ?????????
     * @param listener ?????????
     */
    @SuppressLint("SetTextI18n")
    public static void showListenerDialog(Context context, int icon, String title, String content,
                                          String confirm, String cancle, boolean isCancle, View.OnClickListener listener){
        listenerDialog = new Dialog(context,R.style.alert_dialog);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_normal_listener, null);
        listenerDialog.setContentView(view);

        boolean isImg = icon == 0;
        ImageView imgIcon = view.findViewById(R.id.dnliImg);
        imgIcon.setVisibility(isImg ? View.GONE : View.GONE);
        if (!isImg){
            imgIcon.setBackground(null);
            imgIcon.setBackgroundResource(icon);
        }
        boolean isTitle = TextUtils.isEmpty(title);
        TextView tvTitle = view.findViewById(R.id.dnliTitleTv);
        tvTitle.setVisibility(isTitle ? View.GONE : View.VISIBLE);
        if (!isTitle){
            setText(tvTitle,title);
        }
        boolean isContent = TextUtils.isEmpty(content);
        TextView tvContent = view.findViewById(R.id.dnliContentTv);
        tvContent.setVisibility(isContent ? View.GONE : View.VISIBLE);
        if (!isContent){
            setText(tvContent,content);
        }
        boolean isCan = cancle == null;
        TextView btnCancle = view.findViewById(R.id.dnliCancleBtn);
        btnCancle.setVisibility(isCan ? View.GONE : View.VISIBLE);
        if (!isCan){
            if (cancle.equals("")){
                btnCancle.setText(context.getString(R.string.buttonCancle));
            }else {
                btnCancle.setText(cancle);
            }
            btnCancle.setOnClickListener(listener);
        }
        boolean isCon = confirm == null;
        TextView btnConfirm = view.findViewById(R.id.dnliConfirmBtn);
        btnConfirm.setVisibility(isCon ? View.GONE : View.VISIBLE);
        if (!isCon){
            if (confirm.equals("")){
                btnConfirm.setText("OK");
            }else {
                btnConfirm.setText(confirm);
            }
            btnConfirm.setOnClickListener(listener);
        }

        Window window = listenerDialog.getWindow();
        if (window != null){
            WindowManager windowManager = window.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams params = window.getAttributes();
            Point point=new Point();
            display.getSize(point);
            params.width= point.x;
            window.setAttributes(params);
            window.setGravity(Gravity.CENTER);
            listenerDialog.setCanceledOnTouchOutside(isCancle);
            listenerDialog.show();
        }
    }


    /**
     * ????????????????????????dialog
     */
    public static void closeListenerDialog(){
        if (listenerDialog == null)  return;
        listenerDialog.cancel();
    }

    public static void noLoginDialog(Context context, View.OnClickListener listener){
        showListenerDialog(context,R.drawable.ic_logo_exmination,context.getString(R.string.tipsNoLogin),
                context.getString(R.string.tipsPlzLogin),"",null,false,listener);
    }

    public static Dialog purCateDialog,purSubjectDialog,advertisementDialog;

    /**
     * ????????????category?????????
     * @param context ?????????
     * @param bean categoryBean??????
     * @param listener ?????????
     */
    @SuppressLint("SetTextI18n")
    public static void showPurCateDialog(Context context, CategoryBean bean, View.OnClickListener listener){
        purCateDialog = new Dialog(context, R.style.BottomDialog);//???????????????
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_purchased_category, null);
        purCateDialog.setContentView(view);

        RelativeLayout llPB = view.findViewById(R.id.dpcPBLayout);
        ProgressBar progressBar = view.findViewById(R.id.dpcProgressBar);
        ImageView imageView = view.findViewById(R.id.dpcCateImg);
        TextView tvName = view.findViewById(R.id.dpcCateNameTv);
        TextView btnPurchased = view.findViewById(R.id.dpcPurchasedBtn);
        TextView btnCancle = view.findViewById(R.id.dpcCancleBtn);

        //????????????
        String name = bean.getCategoryName();
        tvName.setText(name);
        String urlImg = bean.getCategoryImgUrl();
        boolean isImg = TextUtils.isEmpty(urlImg);
        llPB.setBackgroundColor(ColorUtils.getColor(isImg ? R.color.colorF8 : R.color.colorWhite));
        if (!isImg){
            getParseImage(imageView,urlImg);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        float oPrice = bean.getGoogleOriginalPrice();
        float sPrice = bean.getGoogleSpecialPrice();
        boolean isSpecial = sPrice > 0;
        if (isSpecial){
            //????????????
            btnPurchased.setText(context.getString(R.string.titleLimitedSpecial) + "???$" + sPrice + "???" + context.getString(R.string.buttonPurchasedNow));
        }else {
            //????????????
            btnPurchased.setText("?????????" + "$" + oPrice + "???" + context.getString(R.string.buttonAllPurchased));
        }
        btnPurchased.setOnClickListener(listener);
        btnCancle.setOnClickListener(view12 -> closePurCateDialog());

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.bottomMargin = dp2px(0f);
        view.setLayoutParams(params);

        Window window = purCateDialog.getWindow();
        purCateDialog.setCanceledOnTouchOutside(true);
        if (window != null){
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.BottomDialog_Animation);
        }
        purCateDialog.show();
    }

    /**
     * ????????????category?????????
     * @param context ?????????
     * @param cObj category??????
     * @param listener ?????????
     */
    @SuppressLint("SetTextI18n")
    public static void showPurCateDialog(Context context, ParseObject cObj, View.OnClickListener listener){
        purCateDialog = new Dialog(context, R.style.BottomDialog);//???????????????
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_purchased_category, null);
        purCateDialog.setContentView(view);

        RelativeLayout llPB = view.findViewById(R.id.dpcPBLayout);
        ProgressBar progressBar = view.findViewById(R.id.dpcProgressBar);
        ImageView imageView = view.findViewById(R.id.dpcCateImg);
        TextView tvName = view.findViewById(R.id.dpcCateNameTv);
        TextView btnPurchased = view.findViewById(R.id.dpcPurchasedBtn);
        TextView btnCancle = view.findViewById(R.id.dpcCancleBtn);

        //????????????
        String name = cObj.getString(CATEGORY_NAME);
        tvName.setText(name);
        boolean isShowImg = cObj.getParseFile(CATEGORY_IMG) != null;
        llPB.setBackgroundColor(ColorUtils.getColor(isShowImg ? R.color.colorWhite : R.color.colorF8));
        progressBar.setVisibility(isShowImg ? View.GONE : View.VISIBLE);
        imageView.setVisibility(isShowImg ? View.VISIBLE : View.GONE);
        if (isShowImg){
            getParseImage(imageView,cObj,CATEGORY_IMG);
        }
        double oPrice = cObj.getDouble(CATEGORY_GOOGLE_ORIGINAL_PRICE);
        double sPrice = cObj.getDouble(CATEGORY_GOOGLE_SPECIAL_PRICE);
        boolean isSpecial = sPrice > 0;
        if (isSpecial){
            //????????????
            btnPurchased.setText(context.getString(R.string.titleLimitedSpecial) + "???$" + sPrice + "???" + context.getString(R.string.buttonPurchasedNow));
        }else {
            //????????????
            btnPurchased.setText("?????????" + "$" + oPrice + "???" + context.getString(R.string.buttonAllPurchased));
        }
        btnPurchased.setOnClickListener(listener);
        btnCancle.setOnClickListener(view12 -> closePurCateDialog());

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.bottomMargin = dp2px(0f);
        view.setLayoutParams(params);

        Window window = purCateDialog.getWindow();
        purCateDialog.setCanceledOnTouchOutside(true);
        if (window != null){
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.BottomDialog_Animation);
        }
        purCateDialog.show();
    }

    /**
     * ????????????cate?????????
     */
    public static void closePurCateDialog(){
        if (purCateDialog == null || !purCateDialog.isShowing()) return;
        purCateDialog.dismiss();
    }


    /**
     * ????????????category?????????
     * @param context ?????????
     * @param bean subjectBean??????
     * @param listener ?????????
     */
    @SuppressLint("SetTextI18n")
    public static void showPurSubjectDialog(Context context, SubjectBean bean, View.OnClickListener listener){
        purSubjectDialog = new Dialog(context, R.style.BottomDialog);//???????????????
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_purchased_subject, null);
        purSubjectDialog.setContentView(view);

        RelativeLayout llPB = view.findViewById(R.id.dpsPBLayout);
        ProgressBar progressBar = view.findViewById(R.id.dpsProgressBar);
        ImageView imageView = view.findViewById(R.id.dpsProductImg);
        TextView tvInfor = view.findViewById(R.id.dpsProductInforTv);
        TextView btnPurchased = view.findViewById(R.id.dpsPurchasedBtn);
        TextView btnCancle = view.findViewById(R.id.dpsCancleBtn);

        //??????Product???infor
        String infor = "?????????????????????";
        setText(tvInfor,infor);
        tvInfor.setVisibility(TextUtils.isEmpty(infor) ? View.GONE : View.VISIBLE);
        //????????????
        boolean isShowPro = bean.isSubjectShowProductImg();
        String urlPro = bean.getSubjectProductImgUrl();
        boolean isEmptyPro = TextUtils.isEmpty(urlPro);
        llPB.setVisibility(isShowPro ? (isEmptyPro ? View.GONE : View.VISIBLE) : View.GONE);
        llPB.setBackgroundColor(ColorUtils.getColor(isShowPro ? (isEmptyPro ? R.color.colorF8 : R.color.colorWhite) : R.color.colorF8));
        if (isShowPro && !isEmptyPro){
            getParseImage(imageView,urlPro);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        //??????
        float oPrice = bean.getSubjectGoogleOriginalPrice();
        float sPrice = bean.getSubjectGoogleSpecialPrice();
        boolean isSpecial = sPrice > 0;
        if (isSpecial){
            //????????????
            btnPurchased.setText(context.getString(R.string.titleLimitedSpecial) + "???$" + sPrice + "???" + context.getString(R.string.buttonSinglePurchased));
        }else {
            //????????????
            btnPurchased.setText("?????????" + "$" + oPrice + "???" + context.getString(R.string.buttonSinglePurchased));
        }
        btnPurchased.setOnClickListener(listener);
        btnCancle.setOnClickListener(view12 -> closePurSubDialog());

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.bottomMargin = dp2px(0f);
        view.setLayoutParams(params);

        Window window = purSubjectDialog.getWindow();
        purSubjectDialog.setCanceledOnTouchOutside(true);
        if (window != null){
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.BottomDialog_Animation);
        }
        purSubjectDialog.show();
    }

    /**
     * ????????????category?????????
     * @param context ?????????
     * @param sObj subject??????
     * @param listener ?????????
     */
    @SuppressLint("SetTextI18n")
    public static void showPurSubjectDialog(Context context, ParseObject sObj, View.OnClickListener listener){
        purSubjectDialog = new Dialog(context, R.style.BottomDialog);//???????????????
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_purchased_subject, null);
        purSubjectDialog.setContentView(view);

        RelativeLayout llPB = view.findViewById(R.id.dpsPBLayout);
        ProgressBar progressBar = view.findViewById(R.id.dpsProgressBar);
        ImageView imageView = view.findViewById(R.id.dpsProductImg);
        TextView tvInfor = view.findViewById(R.id.dpsProductInforTv);
        TextView btnPurchased = view.findViewById(R.id.dpsPurchasedBtn);
        TextView btnCancle = view.findViewById(R.id.dpsCancleBtn);

        //??????Product???infor
        String infor = "?????????????????????";
        setText(tvInfor,infor);
        tvInfor.setVisibility(TextUtils.isEmpty(infor) ? View.GONE : View.VISIBLE);
        //????????????
        boolean isShowPro = sObj.getBoolean(SUBJECT_SHOW_PRODUCT_IMG);
        boolean isEmptyPro = sObj.getParseFile(SUBJECT_PRODUCT_IMG) == null;
        int visibility = isShowPro ? (isEmptyPro ? View.GONE : View.VISIBLE) : View.GONE;
        llPB.setVisibility(visibility);
        progressBar.setVisibility(visibility);
        imageView.setVisibility(visibility);
        llPB.setBackgroundColor(ColorUtils.getColor(isShowPro ? (isEmptyPro ? R.color.colorF8 : R.color.colorWhite) : R.color.colorF8));
        if (isShowPro && !isEmptyPro){
            getParseImage(imageView,sObj,SUBJECT_PRODUCT_IMG);
        }
        //??????
        double oPrice = sObj.getDouble(SUBJECT_GOOGLE_ORIGINAL_PRICE);
        double sPrice = sObj.getDouble(SUBJECT_GOOGLE_SPECIAL_PRICE);
        boolean isSpecial = sPrice > 0;
        if (isSpecial){
            //????????????
            btnPurchased.setText(context.getString(R.string.titleLimitedSpecial) + "???$" + sPrice + "???" + context.getString(R.string.buttonSinglePurchased));
        }else {
            //????????????
            btnPurchased.setText("?????????" + "$" + oPrice + "???" + context.getString(R.string.buttonSinglePurchased));
        }
        btnPurchased.setOnClickListener(listener);
        btnCancle.setOnClickListener(view12 -> closePurSubDialog());

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.bottomMargin = dp2px(0f);
        view.setLayoutParams(params);

        Window window = purSubjectDialog.getWindow();
        purSubjectDialog.setCanceledOnTouchOutside(true);
        if (window != null){
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.BottomDialog_Animation);
        }
        purSubjectDialog.show();
    }

    /**
     * ????????????subject
     */
    public static void closePurSubDialog(){
        if (purSubjectDialog == null || !purSubjectDialog.isShowing()) return;
        purSubjectDialog.dismiss();
    }

    /**
     * ????????????????????????
     */
    public static void showAdvertisementDialog(Context context, SubjectBean bean, View.OnClickListener listener){
        advertisementDialog = new Dialog(context, R.style.BottomDialog);//???????????????
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_advertisement, null);
        advertisementDialog.setContentView(view);

        TextView btnPurchased = view.findViewById(R.id.dadPurchasedBtn);
        TextView btnCancle = view.findViewById(R.id.dadCancleBtn);

        btnPurchased.setOnClickListener(view1 -> {
            closeAdvertisementDialog();
            showPurSubjectDialog(context,bean,listener);
        });
        btnCancle.setOnClickListener(view12 -> closeAdvertisementDialog());

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.bottomMargin = dp2px(0f);
        view.setLayoutParams(params);

        Window window = advertisementDialog.getWindow();
        advertisementDialog.setCanceledOnTouchOutside(true);
        if (window != null){
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.BottomDialog_Animation);
        }
        advertisementDialog.show();
    }

    /**
     * ????????????subject
     */
    public static void closeAdvertisementDialog(){
        if (advertisementDialog == null || !advertisementDialog.isShowing()) return;
        advertisementDialog.dismiss();
    }

    /**
     * ??????????????????
     */
    public static boolean setText(TextView tv,String content){
        if (content == null || TextUtils.isEmpty(content)){
            return false;
        }
        tv.setText(content);
        tv.setBackground(null);
        return true;
    }

    /**
     * ???????????????????????????
     */
    public static boolean setTextWithBg(Context context,TextView tv,String content,int resId){
        if (content == null || TextUtils.isEmpty(content)){
            return false;
        }
        tv.setText(content);
        Drawable drawable = context.getResources().getDrawable(resId);
        tv.setBackground(drawable);
        return true;
    }

    public static void getParseAvator(final ImageView imgView, ParseObject parseObj, String className) {
        imgView.setBackgroundResource(R.color.colorTranslate);
        ParseFile fileObject = parseObj.getParseFile(className);
        if (fileObject != null) {
            Glide.with(imgView.getContext())
                    .load(fileObject.getUrl())
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .centerCrop()
                    .apply(new RequestOptions().centerCrop())
                    .error(R.drawable.default_avatar)
                    .into(imgView);
        } else {
            Glide.with(imgView.getContext())
                    .load(R.drawable.default_avatar)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .centerCrop()
                    .apply(new RequestOptions().centerCrop())
                    .error(R.drawable.default_avatar)
                    .into(imgView);
        }
    }

    public static void getParseImage(final ImageView imgView, ParseObject parseObj, String className) {
        imgView.setBackgroundResource(R.color.colorTranslate);
        ParseFile fileObject = parseObj.getParseFile(className);
        if (fileObject != null) {
            Glide.with(imgView.getContext())
                    .load(fileObject.getUrl())
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .centerCrop()
                    .apply(new RequestOptions().centerCrop())
                    .error(R.drawable.ic_default_pic)
                    .into(imgView);
        } else {
            Glide.with(imgView.getContext())
                    .load(R.drawable.ic_default_pic)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .centerCrop()
                    .apply(new RequestOptions().centerCrop())
                    .error(R.drawable.ic_default_pic)
                    .into(imgView);
        }
    }

    public static void getParseImage(ImageView imageView,String imgUrl){
        if (TextUtils.isEmpty(imgUrl)){
            Glide.with(imageView.getContext())
                    .load(R.drawable.ic_default_pic)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .centerCrop()
                    .apply(new RequestOptions().centerCrop())
                    .error(R.drawable.ic_default_pic)
                    .into(imageView);
        }else {
            Glide.with(imageView.getContext())
                    .load(imgUrl)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate()
                    .centerCrop()
                    .apply(new RequestOptions().centerCrop())
                    .error(R.drawable.ic_default_pic)
                    .into(imageView);
        }
    }

    /**
     * ??????Parse??????
     */
    public static void saveParseImage(Bitmap bitmap, ParseObject parseObj, String className,String fileName, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile imageFile = new ParseFile(fileName + ".jpg", byteArray);
        parseObj.put(className, imageFile);
    }

    /**
     * ??????Parse??????
     */
    public static void saveParseImage(Bitmap bitmap, ParseObject parseObj, String className,int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile imageFile = new ParseFile("image.jpg", byteArray);
        parseObj.put(className, imageFile);
    }

    /**
     * ????????????
     * @param list ??????
     * @return ture?????????false?????????
     */
    public static boolean judgeEmptyList(List list){
        return list == null || list.size() == 0;
    }

    /**
     * ????????????
     * @param object ??????
     * @return true?????????false?????????
     */
    public static boolean judgeObject(ParseObject object){
        return object == null || TextUtils.isEmpty(object.getObjectId());
    }

    /**
     * ???????????????????????????
     * @param v ??????
     * @param event ??????
     */
    public static boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            // ??????EditText????????????????????????
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // ??????????????????EditText?????????????????????????????????????????????????????????????????????EditText????????????????????????????????????????????????
        return false;
    }

    /**
     * ???????????????
     * @param context ?????????
     * @param token token
     */
    public static void hideKeyboard(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * ??????????????????
     */
    public static void getTopNewsContent(final Context context, final TextView tv, final ViewFlipper vf) {
        vf.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        tv.setText("???????????????...");
        boolean isNews = judgeEmptyList(newsList);
        if (isNews) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(NEWS_CLASS_NAME);
            query.findInBackground((objects, e) -> {
                if (e == null){
                    boolean isEmpty = judgeEmptyList(objects);
                    tv.setVisibility(View.GONE);
                    vf.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                    if (!isEmpty){
                        newsList.addAll(objects);
                        setViewFlipper(context, vf);
                    }
                }else {
                    newsList.clear();
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("??????????????????,??????????????????");
                    tv.setOnClickListener(view -> getTopNewsContent(context, tv, vf));
                    setViewFlipper(context, vf);
                }
            });
        } else {
            tv.setVisibility(View.GONE);
            vf.setVisibility(View.VISIBLE);
            setViewFlipper(context, vf);
        }
    }

    /**
     * ????????????
     */
    @SuppressLint("SetTextI18n")
    public static void setViewFlipper(final Context context, ViewFlipper vf) {
        if (newsList == null || newsList.size() == 0) {
            vf.setVisibility(View.GONE);
        } else {
            vf.setVisibility(View.VISIBLE);
            vf.setFlipInterval(10000);//????????????
            for (int i = 0; i < newsList.size(); i++) {
                ParseObject nObj = newsList.get(i);
                if (nObj != null){
                    TextView tv = new TextView(context);
                    tv.setText(nObj.getString(NEWS_TOPIC) + "???"+ nObj.getString(NEWS_DETAILS));
                    tv.setTextColor(ColorUtils.getColor(R.color.colorWhite));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(12);
                    vf.addView(tv);
                }
            }
            vf.startFlipping();
            vf.setInAnimation(context, R.anim.news_banner_in); //??????????????????
            vf.setOutAnimation(context, R.anim.news_banner_out);
        }
    }

    /**
     * ???????????????
     */
    @SuppressLint("SetTextI18n")
    public static int getVersioCode(Context context){
        PackageManager packageManager = context.getPackageManager();
        // ???????????????
        // ???1 ?????? ???2 ?????????????????????flag ??????????????? ???0
        try {
            return (packageManager.getPackageInfo(
                    context.getPackageName(), 0)).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * ???????????????
     */
    @SuppressLint("SetTextI18n")
    public static String getVersioName(Context context){
        PackageManager packageManager = context.getPackageManager();
        // ???????????????
        // ???1 ?????? ???2 ?????????????????????flag ??????????????? ???0
        try {
            return (packageManager.getPackageInfo(
                    context.getPackageName(), 0)).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * ???????????????
     * @return string ??????name???code
     */
    public static String getCurrentVersion(Context context){
        String name = getVersioName(context);
        int code = getVersioCode(context);
        if (name.equals("0") || code == 0){
            return "????????????";
        }else {
            return (name + "." + code);
        }
    }

    /**
     * ????????????
     * @param bean ????????????
     * @param questionId ??????ID
     */
    public static void addWrongBank(SubjectBean bean,String questionId){
        if (isLogin()){
            //??????
            if (bean != null && !TextUtils.isEmpty(questionId)){

                //?????????
                String subjectId = bean.getSubjectId();
                ParseObject sObj = ParseObject.createWithoutData(SUBJECT_CLASS_NAME,subjectId);
                ParseUser user = ParseUser.getCurrentUser();
                ParseQuery<ParseObject> query = ParseQuery.getQuery(WRONG_BANK_CLASS_NAME);
                query.whereEqualTo(USER_POINTER,user);
                query.whereEqualTo(SUBJECT_POINTER,sObj);
                query.findInBackground((objects, e) -> {
                    if (e == null){
                        if (judgeEmptyList(objects)){
                            //?????????????????????
                            ParseObject wObj = ParseObject.create(WRONG_BANK_CLASS_NAME);
                            List<String> list = new ArrayList<>();
                            list.add(questionId);
                            wObj.put(USER_POINTER,user);
                            wObj.put(SUBJECT_POINTER,sObj);
                            wObj.put(WRONG_BANK_QUESTIONS,list);
                            wObj.saveInBackground();
                        }else {
                            //??????????????????
                            ParseObject wObj = objects.get(0);
                            List<String> list = wObj.getList(WRONG_BANK_QUESTIONS);
                            if (judgeEmptyList(list)){
                                list = new ArrayList<>();
                                list.add(questionId);
                            }else {
                                if (!list.contains(questionId)) list.add(questionId);
                            }
                            wObj.put(WRONG_BANK_QUESTIONS,list);
                            wObj.saveInBackground();
                        }
                    }
                });
            }
        }
    }

    /**
     * ????????????
     * @param sObj subject??????
     * @param questionId ??????ID
     */
    public static void addWrongBank(ParseObject sObj,String questionId){
        if (isLogin()){
            //??????
            if (!judgeObject(sObj) && !TextUtils.isEmpty(questionId)){

                //?????????
                ParseUser user = ParseUser.getCurrentUser();
                ParseQuery<ParseObject> query = ParseQuery.getQuery(WRONG_BANK_CLASS_NAME);
                query.whereEqualTo(USER_POINTER,user);
                query.whereEqualTo(SUBJECT_POINTER,sObj);
                query.findInBackground((objects, e) -> {
                    if (e == null){
                        if (judgeEmptyList(objects)){
                            //?????????????????????
                            ParseObject wObj = ParseObject.create(WRONG_BANK_CLASS_NAME);
                            List<String> list = new ArrayList<>();
                            list.add(questionId);
                            wObj.put(USER_POINTER,user);
                            wObj.put(SUBJECT_POINTER,sObj);
                            wObj.put(WRONG_BANK_QUESTIONS,list);
                            wObj.saveInBackground();
                        }else {
                            //??????????????????
                            ParseObject wObj = objects.get(0);
                            List<String> list = wObj.getList(WRONG_BANK_QUESTIONS);
                            if (judgeEmptyList(list)){
                                list = new ArrayList<>();
                                list.add(questionId);
                            }else {
                                if (!list.contains(questionId)) list.add(questionId);
                            }
                            wObj.put(WRONG_BANK_QUESTIONS,list);
                            wObj.saveInBackground();
                        }
                    }
                });
            }
        }
    }

    /**
     * ????????????
     * @param bean ????????????
     * @param questionId ??????Id
     */
    public static void removeWrongBank(SubjectBean bean,String questionId){
        if (isLogin()){
            //??????
            if (bean != null && !TextUtils.isEmpty(questionId)){
                //?????????
                String subjectId = bean.getSubjectId();
                ParseObject sObj = ParseObject.createWithoutData(SUBJECT_CLASS_NAME,subjectId);
                ParseUser user = ParseUser.getCurrentUser();
                ParseQuery<ParseObject> query = ParseQuery.getQuery(WRONG_BANK_CLASS_NAME);
                query.whereEqualTo(USER_POINTER,user);
                query.whereEqualTo(SUBJECT_POINTER,sObj);
                query.findInBackground((objects, e) -> {
                    if (e == null && !judgeEmptyList(objects)){
                        ParseObject wObj = objects.get(0);
                        List<String> list = wObj.getList(WRONG_BANK_QUESTIONS);
                        if (!judgeEmptyList(list)){
                            if (list.contains(questionId)){
                                list.remove(questionId);
                                wObj.put(WRONG_BANK_QUESTIONS,list);
                                wObj.saveInBackground();
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * ????????????
     * @param sObj subject??????
     * @param questionId ??????Id
     */
    public static void removeWrongBank(ParseObject sObj,String questionId){
        if (isLogin()){
            //??????
            if (!judgeObject(sObj) && !TextUtils.isEmpty(questionId)){
                //?????????
                ParseUser user = ParseUser.getCurrentUser();
                ParseQuery<ParseObject> query = ParseQuery.getQuery(WRONG_BANK_CLASS_NAME);
                query.whereEqualTo(USER_POINTER,user);
                query.whereEqualTo(SUBJECT_POINTER,sObj);
                query.findInBackground((objects, e) -> {
                    if (e == null && !judgeEmptyList(objects)){
                        ParseObject wObj = objects.get(0);
                        List<String> list = wObj.getList(WRONG_BANK_QUESTIONS);
                        if (!judgeEmptyList(list)){
                            if (list.contains(questionId)){
                                list.remove(questionId);
                                wObj.put(WRONG_BANK_QUESTIONS,list);
                                wObj.saveInBackground();
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * ??????????????????
     */
    public static void getNewVersion(final Context context) {
        String version = getCurrentVersion(context);
        if (version.equals("????????????")){
            ToastUtils.showBlackCenterToast(context,"???????????????????????????");
        }else {
            if (judgeObject(configOoject)){
                ParseQuery<ParseObject> query = ParseQuery.getQuery(CONFIG_CLASS_NAME);
                query.getFirstInBackground((object, e) -> {
                    if (e == null){
                        configOoject = object;
                        checkVersion(context,version,object.getString(CONFIG_ANDROID_NEW_VERSION));
                    }else {
                        ToastUtils.showBlackCenterToast(context,"???????????????????????????" + e.getMessage());
                    }
                });
            }else {
                checkVersion(context,version,configOoject.getString(CONFIG_ANDROID_NEW_VERSION));
            }
        }
    }

    public static void checkVersion(Context context,String curVersion,String newVersion){
        if (curVersion.equals("????????????")) return;
        if (TextUtils.isEmpty(newVersion)) return;
        if (!newVersion.equals(curVersion)){
            showCheckVersionDialog(context,newVersion);
        }
    }

    static CheckVersionDialog checkVersionDialog;

    /**
     * ?????????????????????????????????
     *
     * @param context      ?????????
     */
    public static void showCheckVersionDialog(final Context context,String newVersion) {
        if (configOoject == null) return;
        if (checkVersionDialog == null){
            checkVersionDialog = new CheckVersionDialog(context, R.style.alert_dialog);
            checkVersionDialog
                    .setVersion(newVersion)
                    .setConfirm(myDialog -> {
                        //????????????
                        rateNow(context);
                    });
            checkVersionDialog.setCanceledOnTouchOutside(false);
        }
        checkVersionDialog.show();
    }

    /**
     * ????????????
     */
    public static void rateNow(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
            //intent.setPackage(GoogleMarket.GOOGLE_PLAY);//????????????????????????????????????????????????????????????????????????
            //if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            //}
//            else {
            //               ?????????????????????????????????????????????Google Play
//                Intent intent2 = new Intent(Intent.ACTION_VIEW);
//                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
//                if (intent2.resolveActivity(context.getPackageManager()) != null) {
//                    context.startActivity(intent2);
//                } else {
//                    //??????Google Play ??????????????????
//                }
//            }
        } catch (ActivityNotFoundException activityNotFoundException1) {
            // Log.e(AppRater.class.getSimpleName(), "GoogleMarket Intent not found");
        }
    }

    /**
     * ??????????????????
     */
    public static boolean isLogin(){
        ParseUser currentUser = ParseUser.getCurrentUser();

        return (currentUser != null && currentUser.getUsername() != null && !currentUser.getUsername().isEmpty());
    }

    /**
     * ??????category??????
     * @param context ?????????
     * @param bean categoryBean??????
     */
    public static void goCategory(Context context,CategoryBean bean){
        Bundle bundle = new Bundle();
        bundle.putParcelable(CATEGORY_BEAN,bean);
        CategoryActivity.show(context,bundle);
    }

    /**
     * ??????category??????
     * @param context ?????????
     * @param object category??????
     */
    public static void goCategory(Context context,ParseObject object){
        Bundle bundle = new Bundle();
        bundle.putParcelable(CATEGORY_POINTER,object);
        CategoryActivity.show(context,bundle);
    }

    /**
     * ??????subject??????
     * @param context ?????????
     * @param category ??????categoryBean??????
     * @param subject subjectBean??????
     */
    public static void goSubject(Context context,CategoryBean category,SubjectBean subject){
        Bundle bundle = new Bundle();
        bundle.putParcelable(CATEGORY_BEAN,category);
        bundle.putParcelable(SUBJECT_BEAN,subject);
        SubjectActivity.show(context,bundle);
    }

    /**
     * ??????subject??????
     * @param context ?????????
     * @param category ??????cate??????
     * @param subject sub??????
     */
    public static void goSubject(Context context,ParseObject category,ParseObject subject){
        Bundle bundle = new Bundle();
        bundle.putParcelable(CATEGORY_POINTER,category);
        bundle.putParcelable(SUBJECT_POINTER,subject);
        SubjectActivity.show(context,bundle);
    }

    /**
     *
     * @param context ?????????
     * @param type ???????????????
     * @param category ??????
     * @param subject ??????
     */
    public static void goExamination(Context context,int type,CategoryBean category,SubjectBean subject){
        Bundle bundle = new Bundle();
        if (CategoryActivity.activity != null) CategoryActivity.activity.finish();
        if (SubjectActivity.activity != null) SubjectActivity.activity.finish();
        bundle.putInt(EXAMINATION_STATUS, type);
        bundle.putParcelable(CATEGORY_BEAN, category);
        bundle.putParcelable(SUBJECT_BEAN, subject);
        ExaminationActivity.show(context, bundle);
    }

    /**
     *
     * @param context ?????????
     * @param type ???????????????
     * @param category ??????
     * @param subject ??????
     */
    public static void goExamination(Context context,int type,ParseObject category,ParseObject subject){
        Bundle bundle = new Bundle();
        if (CategoryActivity.activity != null) CategoryActivity.activity.finish();
        if (SubjectActivity.activity != null) SubjectActivity.activity.finish();
        bundle.putInt(EXAMINATION_STATUS, type);
        bundle.putParcelable(CATEGORY_POINTER, category);
        bundle.putParcelable(SUBJECT_POINTER, subject);
        ExaminationActivity.show(context, bundle);
    }

    /**
     * ??????????????????
     * @param page ?????????
     */
    public static void switchHomeVp(int page){
        ViewPager viewPager = HomeActivity.viewPager;
        BottomNavigationAdapter adapter = HomeActivity.bottomNavigationAdapter;
        if (viewPager != null &&  adapter != null){
            adapter.setSelectedItemPosition(page);
            viewPager.setCurrentItem(page);
        }
    }

    /**
     * ???????????????????????????
     *
     * @param activity ?????????
     * @param permission ??????????????????
     * @return  ????????????
     */
    public static boolean verifyPermissions(com.oscar.writtenexamination.Base.Activity activity, String permission) {
        int Permission = ActivityCompat.checkSelfPermission(activity,permission);
        return Permission == PackageManager.PERMISSION_GRANTED;
    }

}
