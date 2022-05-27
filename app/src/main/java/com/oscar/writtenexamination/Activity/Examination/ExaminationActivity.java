package com.oscar.writtenexamination.Activity.Examination;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Adapter.ExaminationPageAdapter;
import com.oscar.writtenexamination.Base.Activity;
import com.oscar.writtenexamination.Bean.CategoryBean;
import com.oscar.writtenexamination.Bean.QuestionBean;
import com.oscar.writtenexamination.Bean.SubjectBean;
import com.oscar.writtenexamination.Fragment.Examination.QuestionItemFragment;
import com.oscar.writtenexamination.Fragment.QuestionBottomFragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_BEAN;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.EXAMINATION_STATUS;
import static com.oscar.writtenexamination.Base.Configurations.EXAMINATION_STATUS_FOR_FREE;
import static com.oscar.writtenexamination.Base.Configurations.GET_RANDOM_QUESTIONS;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_BEAN;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_ID;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_PASS_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_SHOW_AD_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_SINGLE_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.hideLoading;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.listenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;
import static com.oscar.writtenexamination.Base.Configurations.setText;
import static com.oscar.writtenexamination.Base.Configurations.showAdvertisementDialog;
import static com.oscar.writtenexamination.Base.Configurations.showListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.showLoading;

public class ExaminationActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.exToolbar)
    Toolbar mToolbar;
    @BindView(R.id.exBackBtn)
    ImageView btnBack;
    @BindView(R.id.exTopView)
    View topView;
    @BindView(R.id.exCurrentIndexTv)
    TextView tvCurrent;

    @SuppressLint("StaticFieldLeak")
    public static TextView tvSName;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout btnBottom;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvCorrect;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvWrong;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;

    //参数
    public static int examinationType;
    int passCount = 0;
    public static int showAdCount = 0;
    //适配器
    public static ExaminationPageAdapter pagerAdapter;
    //数据
    public static ParseObject cObj,sObj;
    public static CategoryBean categoryBean;
    public static SubjectBean subjectBean;
    private int singleCount = 0;
    public static int wrong = 0;
    public static int correct = 0;
    //当前页面和全部页面下标
    public static int currentIndex;
    public static int newIndex;
    public static int passPage;
    public static int totalPage;
    //题库相关
    public static boolean isLoadQues = false;
    public static List<QuestionBean> questionBeanList = new ArrayList<>();
    public static List<Integer> wrongList = new ArrayList<>();
    public static List<Integer> correctList = new ArrayList<>();
    //监听器
    private View.OnClickListener listener;


    /**
     * 跳转入口
     */
    public static void show(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ExaminationActivity.class);
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
        activity = this;
        examinationType = bundle == null ? EXAMINATION_STATUS_FOR_FREE : bundle.getInt(EXAMINATION_STATUS);
        categoryBean = bundle == null ? null : bundle.getParcelable(CATEGORY_BEAN);
        subjectBean = bundle == null ? null : bundle.getParcelable(SUBJECT_BEAN);
        cObj = bundle == null ? null : bundle.getParcelable(CATEGORY_POINTER);
        sObj = bundle == null ? null : bundle.getParcelable(SUBJECT_POINTER);
        return super.initArgs(bundle);
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_examination;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        StatusBarUtil.setGradientColor(this, mToolbar);
        UiUtils.setStatusBarDarkTheme(this, true);

        tvWrong = findViewById(R.id.exWrongNumTv);
        tvCorrect = findViewById(R.id.exCorrectNumTv);
        viewPager = findViewById(R.id.exViewPager);
        tvSName = findViewById(R.id.exSNameTv);
        btnBottom = findViewById(R.id.exBottomLayout);
        listener = this;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        if (!isNetConnect) {
            ToastUtils.showBlackCenterToast(this, getString(R.string.tipsNetworkConnectFailedPlzCheckIt));
            return;
        }
        if (isLogin()){
            if (categoryBean == null || subjectBean == null){
                if (cObj == null || sObj == null){
                    showListenerDialog(this, 0, null, getString(R.string.tipsGetDataFailed),
                            "返回上一頁", null, false, view -> {
                                closeListenerDialog();
                                finish();
                            });
                }else {
                    getDataByObject();
                }
            }else {
                getDataByBean();
            }
        }else {
            //未登入
            noLoginDialog(this, this);
        }
    }

    /**
     * 通过obejct对象获取数据
     */
    private void getDataByObject(){
        //获取基础信息
        String name = sObj.getString(SUBJECT_NAME);
        setText(tvSName,name);
        singleCount = sObj.getInt(SUBJECT_SINGLE_COUNT);
        passCount = sObj.getInt(SUBJECT_PASS_COUNT);
        showAdCount = sObj.getInt(SUBJECT_SHOW_AD_COUNT);
        currentIndex = 0;
        newIndex = currentIndex;
        totalPage = singleCount;
        passPage = passCount;
        wrongList.clear();
        correctList.clear();
        questionBeanList.clear();
        //加载题库
        queryQuestion();
    }

    /**
     * 通过bean实体类获取数据
     */
    private void getDataByBean() {
        //获取基础信息
        String name = subjectBean.getSubjectName();
        setText(tvSName,name);
        singleCount = subjectBean.getSingleCount();
        passCount = subjectBean.getPassCount();
        showAdCount = subjectBean.getShowAdCount();
        currentIndex = 0;
        newIndex = currentIndex;
        totalPage = singleCount;
        passPage = passCount;
        wrongList.clear();
        correctList.clear();
        questionBeanList.clear();
        //加载题库
        queryQuestion();
    }

    /**
     * 设置页数
     */
    @SuppressLint("SetTextI18n")
    private void setPage() {
        setText(tvCurrent,(currentIndex + 1) + " / " + totalPage);
    }

    /**
     * 设置正确
     * @param index 题号
     */
    public static void setCorrect(int index){
        if (!correctList.contains(index)){
            correctList.add(index);
        }
        setWrongCorrect();
    }

    /**
     * 设置错误
     * @param index 题号
     */
    public static void setWrong(int index){
        if (!wrongList.contains(index)){
            wrongList.add(index);
        }
        setWrongCorrect();
    }

    /**
     * 设置正确和错误题数
     */
    public static void setWrongCorrect() {
        wrong = wrongList.size();
        correct = correctList.size();
        setText(tvWrong,wrong + "");
        setText(tvCorrect,correct + "");
    }

    /**
     * 加载问题题库
     */
    private void queryQuestion() {
        showLoading(this);
        HashMap<String,Object> params = new HashMap<>();
        params.put(SUBJECT_ID,subjectBean == null ? sObj.getObjectId() : subjectBean.getSubjectId());
        params.put(SUBJECT_SINGLE_COUNT,singleCount);
        ParseCloud.callFunctionInBackground(GET_RANDOM_QUESTIONS, params, (FunctionCallback<List<Map<String,Object>>>) (datas, e) -> {
            hideLoading();
            if (e == null){
                isLoadQues = !judgeEmptyList(datas);
                btnBottom.setActivated(isLoadQues);
                if (isLoadQues){
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(datas);
                    questionBeanList = gson.fromJson(jsonStr, new TypeToken<List<QuestionBean>>(){}.getType());
                    totalPage = questionBeanList.size();
                    //初始化页数
                    setPage();
                    //初始化正题错题
                    setWrongCorrect();
                    iniQuestion();
                }
            }else {
                isLoadQues = false;
            }
        });
    }
    

    private void iniQuestion() {
        viewPager.setOffscreenPageLimit(totalPage);
        pagerAdapter = new ExaminationPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == questionBeanList.size()){
                    currentIndex = position;
                    if (btnBottom.getVisibility() == View.VISIBLE) btnBottom.setVisibility(View.GONE);
                    if (btnBack.getVisibility() == View.VISIBLE) btnBack.setVisibility(View.GONE);
                    if (mToolbar.getVisibility() == View.VISIBLE) mToolbar.setVisibility(View.GONE);
                    if (topView.getVisibility() == View.VISIBLE) topView.setVisibility(View.GONE);
                }else {
                    if (btnBottom.getVisibility() == View.GONE) btnBottom.setVisibility(View.VISIBLE);
                    if (btnBack.getVisibility() == View.GONE) btnBack.setVisibility(View.VISIBLE);
                    if (mToolbar.getVisibility() == View.GONE) mToolbar.setVisibility(View.VISIBLE);
                    if (topView.getVisibility() == View.GONE) topView.setVisibility(View.VISIBLE);
                    currentIndex = position;
                    if (currentIndex > newIndex) newIndex = currentIndex;
                    if (currentIndex > 0){
                        //下一页,判断当前页是否完成
                        int status = 0;
                        if (correctList.contains(currentIndex-1)){
                            status = 1;
                        }
                        if (wrongList.contains(currentIndex-1)){
                            status = 2;
                        }
                        if (status == 0){
                            currentIndex--;
                            newIndex = currentIndex;
                            showListenerDialog(mContext, R.drawable.ic_tips_normal, getString(R.string.tipsWarmReminder),
                                    getString(R.string.tipsPreNotFinish), "", null, false, view -> {
                                        closeListenerDialog();
                                        viewPager.setCurrentItem(currentIndex);
                                    });
                        }else {
                            if (examinationType == EXAMINATION_STATUS_FOR_FREE && showAdCount > 0){
                                boolean isShowAd = currentIndex % showAdCount == 0;
                                if (isShowAd){
                                    //显示广告
                                    showAdvertisementDialog(mContext, subjectBean, view -> {
                                        //购买subject
                                    });
                                }
                            }
                        }
                    }
                    setPage();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.exBottomLayout)
    void onBottom(){
        if (btnBottom.isActivated()){
            new QuestionBottomFragment()
                    .setListener(index -> {
                        if (index <= newIndex){
                            viewPager.setCurrentItem(index);
                        }else {
                            ToastUtils.showBlackCenterToast(this,getString(R.string.tipsPreNotFinish));
                        }
                    }).show(getSupportFragmentManager(), QuestionItemFragment.class.getName());
        }else {
            ToastUtils.showShortToast(this,getString(R.string.tipsPlzWaitLoad));
        }
    }

    @OnClick(R.id.exBackBtn)
    void onBack(){
        //判断是否有做过题目
        if (wrongList.size() == 0 && correctList.size() == 0){
            onFinish();
        }else {
            //弹框询问是否确认要退出
            onAsk();
        }
    }

    @Override
    public void onBackPressed() {
        if (listenerDialog == null || !listenerDialog.isShowing()){
            //判断是否有做过题目
            if (wrongList.size() == 0 && correctList.size() == 0){
                onFinish();
            }else {
                //弹框询问是否确认要退出
                onAsk();
            }
        }
    }

    void onFinish(){
        questionBeanList.clear();
        pagerAdapter.indexs.clear();
        pagerAdapter.fragments.clear();
        wrongList.clear();
        correctList.clear();
        finish();
    }

    void onAsk(){
        showListenerDialog(mContext, R.drawable.ic_tips_normal, getString(R.string.tipsWarmReminder),
                getString(R.string.tipsAlreadyAnswerSome), "退出", null, false, view -> {
                    closeListenerDialog();
                    onFinish();
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dnliConfirmBtn:
                closeListenerDialog();
                LoginSignActivity.show(this, null);
                break;
        }
    }
}
