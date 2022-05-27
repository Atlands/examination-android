package com.oscar.writtenexamination.Activity.CategorySubject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.Examination.ExaminationActivity;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Adapter.RecyclerAdapter;
import com.oscar.writtenexamination.Base.Activity;
import com.oscar.writtenexamination.Base.Configurations;
import com.oscar.writtenexamination.Bean.CategoryBean;
import com.oscar.writtenexamination.Bean.SubjectBean;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_BEAN;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_GOOGLE_ORIGINAL_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_NAME;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.EXAMINATION_STATUS;
import static com.oscar.writtenexamination.Base.Configurations.EXAMINATION_STATUS_FOR_FREE;
import static com.oscar.writtenexamination.Base.Configurations.EXAMINATION_STATUS_FOR_PURCHASED;
import static com.oscar.writtenexamination.Base.Configurations.OBJECT_ID;
import static com.oscar.writtenexamination.Base.Configurations.PURCHASED_HISTORY_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.PURCHASED_HISTORY_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_BEAN;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_GOOGLE_ORIGINAL_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_GOOGLE_SPECIAL_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_INFO;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_PASS_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_SINGLE_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.USER_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.USER_PURCHASED_SUBJECT;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.goExamination;
import static com.oscar.writtenexamination.Base.Configurations.hideLoading;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.judgeObject;
import static com.oscar.writtenexamination.Base.Configurations.setText;
import static com.oscar.writtenexamination.Base.Configurations.showListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.showLoading;
import static com.oscar.writtenexamination.Base.Configurations.showPurSubjectDialog;

public class SubjectActivity extends Activity {

    @BindView(R.id.subToolbar)
    Toolbar mToolbar;
    @BindView(R.id.subNameTv)
    TextView tvName;

    //基本信息
    @BindView(R.id.subInforTv)
    TextView tvInfor;
    @BindView(R.id.subCateLayout)
    LinearLayout llCate;
    @BindView(R.id.subCategoryTv)
    TextView tvCate;
    @BindView(R.id.subAllNumTv)
    TextView tvAllNum;
    @BindView(R.id.subPassNumTv)
    TextView tvPassNum;

    //其他科目
    @BindView(R.id.subOtherLayout)
    LinearLayout llOhter;
    @BindView(R.id.subRy)
    RecyclerView subRy;

    @BindView(R.id.subPriceLayout)
    LinearLayout llPrice;
    @BindView(R.id.subOriginalPriceTv)
    TextView tvOPrice;
    @BindView(R.id.subSpecialPriceTv)
    TextView tvSPrice;
    @BindView(R.id.subPriceStatus)
    TextView statusPrice;
    @BindView(R.id.subFreeTrialBtn)
    TextView btnFreeTrial;
    @BindView(R.id.subPurchasedBtn)
    TextView btnPurchased;

    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    //适配器
    private RecyclerAdapter<SubjectBean> mAdapter;
    private RecyclerAdapter<ParseObject> mAdapter2;
    //数据
    private static CategoryBean categoryBean;
    private static SubjectBean subjectBean;
    private List<SubjectBean> subjectList = new ArrayList<>();
    private List<ParseObject> subjectList2 = new ArrayList<>();
    private static ParseObject cObj, sObj;
    private String name,infor,cateName;
    private int singleCount,passCount;
    private int purStatus = 3; //默认未购买

    /**
     * 跳转入口
     */
    public static void show(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SubjectActivity.class);
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
        context = this;
        if (bundle == null) {
            categoryBean = null;
            subjectBean = null;
            cObj = null;
            sObj = null;
        } else {
            categoryBean = bundle.getParcelable(CATEGORY_BEAN);
            subjectBean = bundle.getParcelable(SUBJECT_BEAN);
            cObj = bundle.getParcelable(CATEGORY_POINTER);
            sObj = bundle.getParcelable(SUBJECT_POINTER);
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
        return R.layout.activity_subject;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        StatusBarUtil.setGradientColor(this, mToolbar);
        UiUtils.setStatusBarDarkTheme(this, true);
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        if (categoryBean == null || subjectBean == null) {
            if (cObj == null || sObj == null) {
                showListenerDialog(this, 0, null, getString(R.string.tipsGetDataFailed),
                        "返回上一頁", null, false, view -> {
                            closeListenerDialog();
                            finish();
                        }
                );
                purStatus = 1;
                setPurchasedLayout(0);
            } else {
                getDataByObject();
            }
        } else {
            getDataByBean();
        }
    }

    /**
     * 通过object对象获取数据
     */
    private void getDataByObject() {
        showLoading(this);
        //基本信息
        name = sObj.getString(SUBJECT_NAME);
        infor = sObj.getString(SUBJECT_INFO);
        cateName = cObj.getString(CATEGORY_NAME);
        singleCount = sObj.getInt(SUBJECT_SINGLE_COUNT);
        passCount = sObj.getInt(SUBJECT_PASS_COUNT);
        setBaseData();
        getPurchasedStatus(2);
        getSubject(2);
        hideLoading();
    }

    /**
     * 通过bean实体类获取数据
     */
    private void getDataByBean() {
        showLoading(this);
        //基本资料
        name = subjectBean.getSubjectName();
        infor = subjectBean.getSubjectInfo();
        cateName = categoryBean.getCategoryName();
        //题目信息
        singleCount = subjectBean.getSingleCount();
        passCount = subjectBean.getPassCount();
        setBaseData();
        getPurchasedStatus(1);
        getSubject(1);
        hideLoading();
    }

    private void getSubject(int way) {
        //默认隐藏
        llOhter.setVisibility(View.GONE);
        if (way == 1){
            //方式一，bean对象
            subjectList = categoryBean.getSubjects();
            int position = 0;
            for (SubjectBean bean : subjectList){
                if (bean.getSubjectId().equals(subjectBean.getSubjectId())){
                    subjectList.remove(position);
                    break;
                }
                position++;
            }
            subjectList.remove(subjectBean);
            setSubject(way);
        }
        if (way == 2){
            //方式二，object对象
            ParseQuery<ParseObject> query = ParseQuery.getQuery(SUBJECT_CLASS_NAME);
            query.whereEqualTo(CATEGORY_POINTER,cObj);
            query.whereNotEqualTo(OBJECT_ID,sObj.getObjectId());
            query.findInBackground((objects, e) -> {
                if (e == null){
                    subjectList2 = objects;
                    subjectList2.remove(sObj);
                }else {
                    subjectList2.clear();
                }
                setSubject(way);
            });
        }
    }

    /**
     * 设置科目布局
     */
    private void setSubject(int way){
        boolean isEmpty;
        if (way == 1){
            isEmpty = judgeEmptyList(subjectList);
        }else {
            isEmpty = judgeEmptyList(subjectList2);
        }
        if (!isEmpty) {
            llOhter.setVisibility(View.VISIBLE);
            initOtherSubject(way);
        }
    }

    /**
     * 获取购买状态
     * @param way 方式
     */
    private void getPurchasedStatus(int way) {
        if (way == 1){
            //方式一，bean对象
            purStatus = isLogin() ? (subjectBean.isBuy() ? 3 : 2) : 0;
            setPurchasedLayout(way);
        }
        if (way == 2){
            //方式二，object对象
            if (isLogin()){
                ParseQuery<ParseObject> queryCate = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                queryCate.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
                queryCate.whereEqualTo(CATEGORY_POINTER, cObj);
                queryCate.findInBackground((objects, e) -> {
                    if (e == null) {
                        if (judgeEmptyList(objects)) {
                            ParseQuery<ParseObject> querySubject = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                            querySubject.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
                            querySubject.whereEqualTo(SUBJECT_POINTER, sObj);
                            querySubject.findInBackground((objects1, e1) -> {
                                purStatus = e1 == null ? (judgeEmptyList(objects1) ? 2 : 3) : 1;
                                setPurchasedLayout(way);
                            });
                        } else {
                            //已购买
                            purStatus = 3;
                            setPurchasedLayout(way);
                        }
                    } else {
                        //查询失败
                        purStatus = 1;
                        setPurchasedLayout(way);
                    }
                });
            }else {
                purStatus = 0;
                setPurchasedLayout(way);
            }
        }
    }

    /**
     * 设置购买布局
     * @param way 方式
     */
    private void setPurchasedLayout(int way) {
        initPurchasedLayout();
        switch (purStatus) {
            case 0:
            case 2:
                //1未登入,2未购买
                llPrice.setVisibility(View.VISIBLE);
                btnFreeTrial.setVisibility(View.VISIBLE);
                btnPurchased.setVisibility(View.VISIBLE);
                break;
            case 3:
                //已购买
                btnPurchased.setVisibility(View.VISIBLE);
                btnPurchased.setText(getString(R.string.buttonGotoExamination));
                btnPurchased.setActivated(false);
                break;
        }
        if (purStatus == 0 || purStatus == 2) {
            boolean isSpecial;
            String original,special;
            if (way == 1){
                float oPrice = subjectBean.getSubjectGoogleOriginalPrice();
                float sPrice = subjectBean.getSubjectGoogleSpecialPrice();
                original = "$" + oPrice;
                special = "$" + sPrice;
                isSpecial = sPrice > 0;
            }else {
                double oPrice = cObj.getDouble(SUBJECT_GOOGLE_ORIGINAL_PRICE);
                double sPrice = cObj.getDouble(SUBJECT_GOOGLE_SPECIAL_PRICE);
                original = "$" + oPrice;
                special = "$" + sPrice;
                isSpecial = sPrice > 0;
            }
            setText(tvOPrice, original);
            tvSPrice.setVisibility(View.GONE);
            statusPrice.setVisibility(View.GONE);
            //如果有特惠价格
            if (isSpecial) {
                //设置特惠价格
                setText(tvSPrice, special);
                tvSPrice.setVisibility(View.VISIBLE);
                setText(statusPrice, getString(R.string.titleLimitedSpecial));
                statusPrice.setBackgroundResource(R.drawable.bg_corner_redborder_whitebg);
                statusPrice.setVisibility(View.VISIBLE);
                //原价折扣样式
                tvOPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                tvOPrice.setTextColor(ColorUtils.getColor(R.color.colorBF));
            }
        }
    }

    /**
     * 初始化底部按钮
     */
    private void initPurchasedLayout(){
        llPrice.setVisibility(View.GONE);
        btnPurchased.setVisibility(View.GONE);
        btnFreeTrial.setVisibility(View.GONE);
        btnPurchased.setText(getString(R.string.buttonSinglePurchased));
        btnPurchased.setBackgroundResource(R.drawable.bg_button_rectangle_normal_activited_selector);
        btnPurchased.setActivated(true);
    }

    /**
     * 设置基础信息
     */
    private void setBaseData() {
        setText(tvName, name);
        setText(tvInfor, infor);
        boolean isCate = TextUtils.isEmpty(cateName);
        llCate.setVisibility(isCate ? View.GONE : View.VISIBLE);
        if (!isCate){
            tvCate.setText(cateName);
            tvCate.setBackgroundResource(R.drawable.bg_corner_redborder_whitebg);
        }
        setText(tvAllNum, singleCount + "");
        setText(tvPassNum, passCount + "");
        tvAllNum.setBackgroundColor(ColorUtils.getColor(R.color.colorMainTwo));
        tvPassNum.setBackgroundColor(ColorUtils.getColor(R.color.colorRedNormal));
    }

    @OnClick(R.id.subFreeTrialBtn)
    void onFreeTrial() {
        Bundle bundle = new Bundle();
        bundle.putInt(EXAMINATION_STATUS, EXAMINATION_STATUS_FOR_FREE);
        if (categoryBean == null || subjectBean == null) {
            if (cObj == null || sObj == null) {
                ToastUtils.showBlackCenterToast(this, getString(R.string.tipsGetDataFailed));
            } else {
                if (CategoryActivity.activity != null) CategoryActivity.activity.finish();
                bundle.putParcelable(CATEGORY_POINTER, cObj);
                bundle.putParcelable(SUBJECT_POINTER, sObj);
                ExaminationActivity.show(this, bundle);
                finish();
            }
        } else {
            if (CategoryActivity.activity != null) CategoryActivity.activity.finish();
            bundle.putParcelable(CATEGORY_BEAN, categoryBean);
            bundle.putParcelable(SUBJECT_BEAN, subjectBean);
            ExaminationActivity.show(this, bundle);
            finish();
        }
    }

    /**
     * 购买subject
     */
    private void purchasedSubject(ParseObject category, ParseObject subject) {
        //查询是否购买一整套
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
        query.whereEqualTo(CATEGORY_POINTER, category);
        query.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
        query.findInBackground((objects, e) -> {
            if (e == null){
                if (judgeEmptyList(objects)){
                    //未购买一整套，通过单独判断
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                    query1.whereEqualTo(SUBJECT_POINTER, subject);
                    query1.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
                    query1.getFirstInBackground((object1, e1) -> {
                        if (e1 == null) {
                            if (judgeObject(object1)) {
                                //未购买过，开始单独购买
                                ParseUser user = ParseUser.getCurrentUser();
                                ParseObject pObj = ParseObject.create(PURCHASED_HISTORY_CLASS_NAME);
                                List<String> purSubList = user.getList(USER_PURCHASED_SUBJECT);
                                if (purSubList == null) purSubList = new ArrayList<>();
                                if (!purSubList.contains(subject.getObjectId())) {
                                    purSubList.add(subject.getObjectId());
                                }
                                user.put(USER_PURCHASED_SUBJECT, purSubList);
                                pObj.put(USER_POINTER, user);
                                pObj.put(SUBJECT_POINTER, subject);
                                double sPrice = subject.getDouble(SUBJECT_GOOGLE_SPECIAL_PRICE);
                                if (sPrice > 0) {
                                    pObj.put(PURCHASED_HISTORY_PRICE, sPrice);
                                } else {
                                    double oPrice = subject.getDouble(CATEGORY_GOOGLE_ORIGINAL_PRICE);
                                    pObj.put(PURCHASED_HISTORY_PRICE, oPrice);
                                }
                                pObj.saveInBackground(e2 -> {
                                    hideLoading();
                                    if (e2 == null) {
                                        ToastUtils.showBlackCenterToast(this, getString(R.string.tipsSavePurchasedHistorySuccess));
                                    } else {
                                        ToastUtils.showBlackCenterToast(this, getString(R.string.tipsSavePurchasedHistoryFailed) + "：" + e2.getMessage());
                                    }
                                });
                            } else {
                                //已单独购买
                                hideLoading();
                                ToastUtils.showBlackCenterToast(this, getString(R.string.tipsAlreadyPurchasedPlzNotAgain));
                            }
                        } else {
                            hideLoading();
                            ToastUtils.showBlackCenterToast(this, getString(R.string.tipsGetDataFailed) + "：" + e1.getMessage());
                        }
                    });
                }else {
                    //已经购买一整套
                    hideLoading();
                    ToastUtils.showBlackCenterToast(this, getString(R.string.tipsAlreadyPurchasedPlzNotAgain));
                }
            }else {
                hideLoading();
                ToastUtils.showBlackCenterToast(this, getString(R.string.tipsGetDataFailed) + "：" + e.getMessage());
            }
        });
    }

    @OnClick(R.id.subPurchasedBtn)
    void onPurchased() {
        if (isLogin()) {
            if (categoryBean == null || subjectBean == null) {
                if (cObj == null || sObj == null) {
                    ToastUtils.showBlackCenterToast(this, getString(R.string.tipsGetDataFailed));
                } else {
                    if (btnPurchased.isActivated()) {
                        //未购买
                        if (purStatus == 2) {
                            showPurSubjectDialog(this, sObj, view -> {
                                //开始购买逻辑，传递category和subject
                                purchasedSubject(cObj,sObj);
                            });
                        }
                    } else {
                        //已购买
                        goExamination(this,EXAMINATION_STATUS_FOR_PURCHASED,cObj,sObj);
                    }
                }
            } else {
//                bundle.putParcelable(CATEGORY_BEAN,categoryBean);
//                bundle.putParcelable(SUBJECT_BEAN,subjectBean);
//                if (CategoryActivity.activity != null) CategoryActivity.activity.finish();
//                if (SubjectActivity.activity != null) SubjectActivity.activity.finish();
//                ExaminationActivity.show(this,bundle);
                boolean isBuy = subjectBean.isBuy();
                if (isBuy) {
                    goExamination(this,EXAMINATION_STATUS_FOR_PURCHASED,categoryBean,subjectBean);
                } else {
                    showPurSubjectDialog(this, subjectBean, view -> {
                        showLoading(this);
                        //根据beanId获取subject
                        ParseQuery<ParseObject> query = ParseQuery.getQuery(SUBJECT_CLASS_NAME);
                        query.whereEqualTo(OBJECT_ID,subjectBean.getSubjectId());
                        query.getFirstInBackground((object, e) -> {
                            if (e == null){
                                if (judgeObject(object)){
                                    //subject为空
                                    hideLoading();
                                    ToastUtils.showBlackCenterToast(this,getString(R.string.tipsGetDataFailed));
                                }else {
                                    //获取详细subject
                                    object.fetchIfNeededInBackground((object1, e1) -> {
                                        if (e1 == null){
                                            if (judgeObject(object1)){
                                                //详细subject为空
                                                hideLoading();
                                                ToastUtils.showBlackCenterToast(this,getString(R.string.tipsGetDataFailed));
                                            }else {
                                                //获取对应category
                                                ParseObject category = object1.getParseObject(CATEGORY_POINTER);
                                                if (judgeObject(category)){
                                                    //对应category为空
                                                    hideLoading();
                                                    ToastUtils.showBlackCenterToast(this,getString(R.string.tipsGetDataFailed));
                                                }else {
                                                    //不为空，开始购买逻辑，传递category和subject
                                                    purchasedSubject(category,object1);
                                                }
                                            }
                                        }else {
                                            //获取详细subject失败
                                            hideLoading();
                                            ToastUtils.showBlackCenterToast(this,getString(R.string.tipsGetDataFailed) + "：" + e1.getMessage());
                                        }
                                    });
                                }
                            }else {
                                //获取subject失败
                                hideLoading();
                                ToastUtils.showBlackCenterToast(this,getString(R.string.tipsGetDataFailed) + "：" + e.getMessage());
                            }
                        });
                    });
                }
            }
        } else {
            closeListenerDialog();
            LoginSignActivity.show(this, null);
            finish();
        }
    }


    @OnClick(R.id.subBackBtn)
    void onBack() {
        finish();
    }

    /**
     * 加载其他科目
     */
    private void initOtherSubject(int way) {
        initOtherLayoutManager();
        if (way == 1){
            if (mAdapter == null) {
                mAdapter = new RecyclerAdapter<SubjectBean>(subjectList, null) {
                    @Override
                    protected int getItemViewType(int position, SubjectBean subjectBean) {
                        return R.layout.item_subject_other;
                    }

                    @Override
                    protected ViewHolder<SubjectBean> onCreateViewHolder(View root, int viewType) {
                        return new SubjectActivity.ViewHolder(root);
                    }
                };
                subRy.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }else {
            if (mAdapter2 == null) {
                mAdapter2 = new RecyclerAdapter<ParseObject>(subjectList2, null) {
                    @Override
                    protected int getItemViewType(int position, ParseObject parseObject) {
                        return R.layout.item_subject_other;
                    }

                    @Override
                    protected ViewHolder<ParseObject> onCreateViewHolder(View root, int viewType) {
                        return new ViewHolder2(root);
                    }
                };
                subRy.setAdapter(mAdapter2);
            } else {
                mAdapter2.notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置布局参数
     */
    private void initOtherLayoutManager() {
        //布局参数
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        subRy.setLayoutManager(layoutManager);
    }

    /**
     * 科目表ViewHolder
     */
    static class ViewHolder extends RecyclerAdapter.ViewHolder<SubjectBean> {
        @BindView(R.id.isoPositionTv)
        TextView tvPosition;
        @BindView(R.id.isoNameTv)
        TextView tvName;
        @BindView(R.id.isoPassCountTv)
        TextView tvPassNum;
        @BindView(R.id.isoSingleCountTv)
        TextView tvSinNum;
        @BindView(R.id.isoOriginalPriceTv)
        TextView tvOPrice;
        @BindView(R.id.isoSpecialPriceTv)
        TextView tvSPrice;
        @BindView(R.id.isoSpecialStatus)
        TextView staSpecial;

        @BindView(R.id.isoItemLayout)
        LinearLayout llItem;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(SubjectBean bean) {
            tvPosition.setText((getAdapterPosition() + 1) + "");
            //基础资料
            String name = bean.getSubjectName();
            setText(tvName, name);
            //题数
            int passCount = bean.getPassCount();
            int singleCount = bean.getSingleCount();
            setText(tvPassNum, "合格題數：" + passCount);
            setText(tvSinNum, "總共題數：" + singleCount);

            //价格
            float oPrice = bean.getSubjectGoogleOriginalPrice();
            float sPrice = bean.getSubjectGoogleSpecialPrice();
            setText(tvOPrice, "$" + oPrice);
            boolean isSpecial = sPrice > 0;
            staSpecial.setVisibility(View.GONE);
            tvSPrice.setVisibility(View.GONE);
            if (isSpecial) {
                setText(tvSPrice, "$" + sPrice);
                tvSPrice.setVisibility(View.VISIBLE);
                setText(staSpecial, context.getString(R.string.titleLimitedSpecial));
                staSpecial.setBackgroundResource(R.drawable.bg_corner_redborder_whitebg);
                staSpecial.setVisibility(View.VISIBLE);
                tvOPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                tvOPrice.setTextColor(ColorUtils.getColor(R.color.colorBF));
            }

            llItem.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(CATEGORY_BEAN, categoryBean);
                bundle.putParcelable(SUBJECT_BEAN, bean);
                SubjectActivity.show(context, bundle);
                activity.finish();
            });

        }

        @Override
        protected void onBind(SubjectBean bean, List<Object> payloads) {

        }
    }

    /**
     * 科目表ViewHolder
     */
    static class ViewHolder2 extends RecyclerAdapter.ViewHolder<ParseObject> {
        @BindView(R.id.isoPositionTv)
        TextView tvPosition;
        @BindView(R.id.isoNameTv)
        TextView tvName;
        @BindView(R.id.isoPassCountTv)
        TextView tvPassNum;
        @BindView(R.id.isoSingleCountTv)
        TextView tvSinNum;
        @BindView(R.id.isoOriginalPriceTv)
        TextView tvOPrice;
        @BindView(R.id.isoSpecialPriceTv)
        TextView tvSPrice;
        @BindView(R.id.isoSpecialStatus)
        TextView staSpecial;

        @BindView(R.id.isoItemLayout)
        LinearLayout llItem;

        ViewHolder2(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(ParseObject object) {
            tvPosition.setText((getAdapterPosition() + 1) + "");
            //基础资料
            String name = object.getString(SUBJECT_NAME);
            setText(tvName, name);
            //题数
            int passCount = object.getInt(SUBJECT_PASS_COUNT);
            int singleCount = object.getInt(SUBJECT_SINGLE_COUNT);
            setText(tvPassNum, "合格題數：" + passCount);
            setText(tvSinNum, "總共題數：" + singleCount);

            //价格
            double oPrice = object.getDouble(SUBJECT_GOOGLE_ORIGINAL_PRICE);
            double sPrice = object.getDouble(SUBJECT_GOOGLE_SPECIAL_PRICE);
            setText(tvOPrice, "$" + oPrice);
            boolean isSpecial = sPrice > 0;
            staSpecial.setVisibility(View.GONE);
            tvSPrice.setVisibility(View.GONE);
            if (isSpecial) {
                setText(tvSPrice, "$" + sPrice);
                tvSPrice.setVisibility(View.VISIBLE);
                setText(staSpecial, context.getString(R.string.titleLimitedSpecial));
                staSpecial.setBackgroundResource(R.drawable.bg_corner_redborder_whitebg);
                staSpecial.setVisibility(View.VISIBLE);
                tvOPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                tvOPrice.setTextColor(ColorUtils.getColor(R.color.colorBF));
            }

            llItem.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(CATEGORY_POINTER, cObj);
                bundle.putParcelable(SUBJECT_POINTER, object);
                SubjectActivity.show(context, bundle);
                activity.finish();
            });

        }

        @Override
        protected void onBind(ParseObject sObj, List<Object> payloads) {

        }
    }

}
