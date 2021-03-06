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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.Examination.ExaminationActivity;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Adapter.RecyclerAdapter;
import com.oscar.writtenexamination.Base.Activity;
import com.oscar.writtenexamination.Bean.CategoryBean;
import com.oscar.writtenexamination.Bean.SubjectBean;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.parse.ParseACL;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_BEAN;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_GOOGLE_ORIGINAL_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_GOOGLE_SPECIAL_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_IMG;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_INFO;
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
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_PASS_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_SINGLE_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.USER_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.USER_PURCHASED_CATEGORY;
import static com.oscar.writtenexamination.Base.Configurations.USER_PURCHASED_SUBJECT;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.getParseImage;
import static com.oscar.writtenexamination.Base.Configurations.goExamination;
import static com.oscar.writtenexamination.Base.Configurations.goSubject;
import static com.oscar.writtenexamination.Base.Configurations.hideLoading;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.judgeObject;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;
import static com.oscar.writtenexamination.Base.Configurations.setText;
import static com.oscar.writtenexamination.Base.Configurations.showListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.showLoading;
import static com.oscar.writtenexamination.Base.Configurations.showPurCateDialog;
import static com.oscar.writtenexamination.Base.Configurations.showPurSubjectDialog;

public class CategoryActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.cateToolbar)
    Toolbar mToolbar;
    @BindView(R.id.cateNameTv)
    TextView tvName;

    @BindView(R.id.catePBLayout)
    RelativeLayout llPb;
    @BindView(R.id.cateProgressBar)
    ProgressBar imgPb;
    @BindView(R.id.cateImg)
    ImageView imgCate;
    @BindView(R.id.cateStatusTv)
    TextView tvStatus;

    @BindView(R.id.cateInforTv)
    TextView tvInfor;

    @BindView(R.id.cateSubjectLayout)
    LinearLayout llSubject;
    @BindView(R.id.cateSubjectNumTv)
    TextView tvSubNum;
    @BindView(R.id.cateSubRy)
    RecyclerView subRy;

    @BindView(R.id.catePriceLayout)
    LinearLayout llPrice;
    @BindView(R.id.cateOriginalPriceTv)
    TextView tvOriginal;
    @BindView(R.id.cateSpecialPriceTv)
    TextView tvSpecial;
    @BindView(R.id.catePriceStatus)
    TextView statusPrice;

    @BindView(R.id.catePurchasedBtn)
    TextView btnPurchased;

    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    //??????
    private static int purStatus = 0;
    //?????????
    private RecyclerAdapter<SubjectBean> mAdapter;
    private RecyclerAdapter<ParseObject> mAdapter2;
    //??????
    public static ParseObject cObj;
    public static CategoryBean categoryBean;
    private String name,infor,imgUrl;
    private boolean isShowImg = false;
    public List<SubjectBean> subjectList = new ArrayList<>();
    public List<ParseObject> subjectList2 = new ArrayList<>();


    /**
     * ????????????
     */
    public static void show(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CategoryActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * ?????????????????????
     *
     * @param bundle ??????Bundle
     * @return ????????????????????????True???????????????False
     */
    @Override
    protected boolean initArgs(Bundle bundle) {
        if (bundle != null) {
            categoryBean = bundle.getParcelable(CATEGORY_BEAN);
            cObj = bundle.getParcelable(CATEGORY_POINTER);
        } else {
            categoryBean = null;
        }
        activity = this;
        return super.initArgs(bundle);
    }

    /**
     * ?????????????????????????????????Id
     *
     * @return ????????????Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_category;
    }


    /**
     * ???????????????
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        StatusBarUtil.setGradientColor(this, mToolbar);
        UiUtils.setStatusBarDarkTheme(this, true);
    }

    /**
     * ???????????????
     */
    @Override
    protected void initData() {
        if (categoryBean == null) {
            if (cObj == null) {
                showListenerDialog(this, 0, null, getString(R.string.tipsGetCategoryFailed),
                        "???????????????", null, false, view -> {
                            closeListenerDialog();
                            finish();
                        });
                purStatus = 1;
                setPurchasedLayout(0);
                getSubject(0);
            } else {
                getDataByObject();
            }
        } else {
            getDateByBean();
        }
    }

    /**
     * ????????????object??????????????????
     */
    private void getDataByObject() {
        showLoading(this);
        name = cObj.getString(CATEGORY_NAME);
        infor = cObj.getString(CATEGORY_INFO);
        //???????????????
        ParseFile cateFile = cObj.getParseFile(CATEGORY_IMG);
        if (cateFile!= null){
            imgUrl = cateFile.getUrl();
            isShowImg = !TextUtils.isEmpty(imgUrl);
        }else {
            isShowImg = false;
        }
        setBaseData();
        setCateImg();
        getPurchasedStatus(2);
        hideLoading();
    }

    /**
     * ????????????bean?????????????????????
     */
    private void getDateByBean() {
        showLoading(this);
        //????????????
        name = categoryBean.getCategoryName();
        infor = categoryBean.getCategoryInfo();
        imgUrl = categoryBean.getCategoryImgUrl();
        isShowImg = !TextUtils.isEmpty(imgUrl);
        setBaseData();
        setCateImg();
        getPurchasedStatus(1);
        getSubject(1);
    }

    /**
     * ????????????????????????name???infor
     */
    private void setBaseData(){
        setText(tvName, name);
        setText(tvInfor, infor);
    }

    /**
     * ??????????????????
     */
    private void setCateImg(){
        llPb.setBackgroundColor(ColorUtils.getColor(isShowImg ? R.color.colorWhite : R.color.colorF8));
        imgPb.setVisibility(isShowImg ? View.GONE : View.VISIBLE);
        imgCate.setVisibility(isShowImg ? View.VISIBLE : View.GONE);
        if (isShowImg) {
            getParseImage(imgCate,imgUrl);
        }
    }

    /**
     * ??????????????????
     * @param way ??????
     */
    private void getPurchasedStatus(int way) {
        if (way == 1){
            //????????????bean??????
            purStatus = isLogin() ? (categoryBean.isBuy() ? 3 : 2) : 0;
            //??????????????????
            setPurchasedLayout(way);
        }
        if (way == 2){
            //????????????object??????
            if (isLogin()){
                ParseQuery<ParseObject> queryCate = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                queryCate.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
                queryCate.whereEqualTo(CATEGORY_POINTER, cObj);
                queryCate.findInBackground((objects, e) -> {
                    if (e == null) {
                        if (!judgeEmptyList(objects)) {
                            //?????????
                            purStatus = 3;
                            setPurchasedLayout(way);
                        }
                        getSubject(2);
                    } else {
                        //????????????
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
     * ????????????
     */
    private void getSubject(int way) {
        //????????????
        llSubject.setVisibility(View.GONE);
        if (way == 1){
            //????????????bean??????
            subjectList = categoryBean.getSubjects();
            setSubject(way);
        }
        if (way == 2){
            //????????????object??????
            ParseQuery<ParseObject> query1 = ParseQuery.getQuery(SUBJECT_CLASS_NAME);
            query1.whereEqualTo(CATEGORY_POINTER, cObj);
            query1.findInBackground((objects1, e1) -> {
                if (e1 == null) {
                    if (judgeEmptyList(objects1)) {
                        subjectList2.clear();
                        purStatus = 2;
                        setPurchasedLayout(way);
                    } else {
                        subjectList2 = objects1;
                        if (purStatus != 3){
                            ParseQuery<ParseObject> querySubject = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                            querySubject.whereEqualTo(USER_POINTER, currentUser);
                            querySubject.whereContainedIn(SUBJECT_POINTER, subjectList2);
                            querySubject.findInBackground((objects2, e2) -> {
                                purStatus = e2 == null ? (judgeEmptyList(objects2) ? 2 : 3) : 1;
                                setPurchasedLayout(way);
                            });
                        }
                    }
                } else {
                    subjectList2.clear();
                    purStatus = 1;
                    setPurchasedLayout(way);
                }
                setSubject(way);
                hideLoading();
            });
        }
    }

    /**
     * ??????????????????
     */
    private void setSubject(int way){
        boolean isEmpty;
        if (way == 1){
            isEmpty = judgeEmptyList(subjectList);
        }else {
            isEmpty = judgeEmptyList(subjectList2);
        }
        tvSubNum.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        if (!isEmpty) {
            llSubject.setVisibility(View.VISIBLE);
            setText(tvSubNum, (way == 1 ? subjectList.size() : subjectList2 .size() )+ " ?????????");
            initSubject(way);
        }
    }

    /**
     * ?????????????????????
     */
    private void initPurchasedLayout(){
        llPrice.setVisibility(View.GONE);
        btnPurchased.setVisibility(View.GONE);
        btnPurchased.setText(getString(R.string.buttonAllPurchased));
        btnPurchased.setBackgroundResource(R.drawable.bg_button_bfgray);
        btnPurchased.setActivated(true);
    }

    /**
     * ??????????????????
     */
    private void setPurchasedLayout(int way) {
        initPurchasedLayout();
        switch (purStatus) {
            case 0:
            case 2:
                //1?????????,2?????????
                llPrice.setVisibility(View.VISIBLE);
                btnPurchased.setVisibility(View.VISIBLE);
                btnPurchased.setBackgroundResource(R.drawable.bg_button_normal_activited_selector);
                break;
            case 3:
                //?????????
                btnPurchased.setActivated(false);
                break;
        }
        if (purStatus == 0 || purStatus == 2) {
            //1????????????2?????????
            //??????
            boolean isSpecial;
            String original,special;
            if (way == 1){
                float oPrice = categoryBean.getGoogleOriginalPrice();
                float sPrice = categoryBean.getGoogleSpecialPrice();
                original = "$" + oPrice;
                special = "$" + sPrice;
                isSpecial = sPrice > 0;
            }else {
                double oPrice = cObj.getDouble(CATEGORY_GOOGLE_ORIGINAL_PRICE);
                double sPrice = cObj.getDouble(CATEGORY_GOOGLE_SPECIAL_PRICE);
                original = "$" + oPrice;
                special = "$" + sPrice;
                isSpecial = sPrice > 0;
            }
            setText(tvOriginal, original);
            tvSpecial.setVisibility(View.GONE);
            statusPrice.setVisibility(View.GONE);
            //?????????????????????
            if (isSpecial) {
                tvStatus.setVisibility(View.VISIBLE);
                //??????????????????
                setText(tvSpecial, special);
                tvSpecial.setVisibility(View.VISIBLE);
                setText(statusPrice, getString(R.string.titleLimitedSpecial));
                statusPrice.setBackgroundResource(R.drawable.bg_corner_redborder_whitebg);
                statusPrice.setVisibility(View.VISIBLE);
                //??????????????????
                tvOriginal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //????????????
                tvOriginal.setTextColor(ColorUtils.getColor(R.color.colorBF));
            }
        }
        hideLoading();
    }


    /**
     * ?????????subject
     */
    private void initSubject(int way) {
        //????????????
        initSubLayoutManager();
        if (way == 1){
            if (mAdapter == null) {
                mAdapter = new RecyclerAdapter<SubjectBean>(subjectList, null) {
                    @Override
                    protected int getItemViewType(int position, SubjectBean subjectBean) {
                        return R.layout.item_subject_normal;
                    }

                    @Override
                    protected ViewHolder<SubjectBean> onCreateViewHolder(View root, int viewType) {
                        return new CategoryActivity.ViewHolder(root, mContext, mActivity);
                    }
                };
                mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<SubjectBean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, SubjectBean bean) {
                        goSubject(mContext, categoryBean, bean);
                    }
                });
                subRy.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }else {
            if (mAdapter2 == null) {
                mAdapter2 = new RecyclerAdapter<ParseObject>(subjectList2, null) {
                    @Override
                    protected int getItemViewType(int position, ParseObject parseObject) {
                        return R.layout.item_subject_normal;
                    }

                    @Override
                    protected ViewHolder<ParseObject> onCreateViewHolder(View root, int viewType) {
                        return new ViewHolder2(root, mContext, mActivity);
                    }
                };
                mAdapter2.setListener(new RecyclerAdapter.AdapterListenerImpl<ParseObject>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, ParseObject object) {
                        goSubject(mContext, cObj, object);
                    }
                });
                subRy.setAdapter(mAdapter2);
            } else {
                mAdapter2.notifyDataSetChanged();
            }
        }
    }

    private void initSubLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        subRy.setLayoutManager(layoutManager);
    }

    /**
     *
     */
    @OnClick(R.id.catePurchasedBtn)
    void onPurchased() {
        if (isLogin()) {
            if (categoryBean == null) {
                if (cObj == null) {
                    ToastUtils.showBlackCenterToast(this, getString(R.string.tipsGetDataFailed));
                } else {
                    if (btnPurchased.isActivated()) {
                        if (purStatus == 2) {
                            //????????????
                            showPurCateDialog(this, cObj, this);
                        }
                    } else {
                        ToastUtils.showBlackCenterToast(this, getString(R.string.tipsAlreadyPurchasedPlzNotAgain));
                    }
                }
            } else {
                if (btnPurchased.isActivated()) {
                    if (purStatus == 2) {
                        //????????????
                        showPurCateDialog(this, categoryBean, this);
                    }
                } else {
                    ToastUtils.showBlackCenterToast(this, getString(R.string.tipsAlreadyPurchasedPlzNotAgain));
                }
            }
        } else {
            //?????????
            noLoginDialog(this, view -> {
                closeListenerDialog();
                LoginSignActivity.show(this, null);
                finish();
            });
        }
    }

    @OnClick(R.id.cateBackBtn)
    void onBack() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dpcPurchasedBtn:
                //????????????
                showLoading(this);
                if (isLogin()) {
                    currentUser = ParseUser.getCurrentUser();
                    //???????????????????????????
                    if (categoryBean == null) {
                        if (cObj == null) {
                            hideLoading();
                            ToastUtils.showBlackCenterToast(this, getString(R.string.tipsGetDataFailed));
                        } else {
                            //??????????????????
                            purchasedCate(cObj);
                        }
                    } else {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery(CATEGORY_CLASS_NAME);
                        query.whereEqualTo(OBJECT_ID, categoryBean.getCategoryId());
                        query.getFirstInBackground((object, e) -> {
                            if (e == null) {
                                if (judgeObject(object)) {
                                    hideLoading();
                                    ToastUtils.showBlackCenterToast(this, getString(R.string.tipsGetDataFailed));
                                } else {
                                    //??????????????????
                                    purchasedCate(object);
                                }
                            } else {
                                hideLoading();
                                ToastUtils.showBlackCenterToast(this, getString(R.string.tipsPurchasedFailed) + "???" + e.getMessage());
                            }
                        });
                    }
                } else {
                    hideLoading();
                    currentUser = null;
                    //?????????
                    noLoginDialog(this, view -> {
                        closeListenerDialog();
                        LoginSignActivity.show(this, null);
                        finish();
                    });
                }
                break;
            case R.id.dnliConfirmBtn:
                closeListenerDialog();
                LoginSignActivity.show(this, null);
        }
    }

    /**
     * ??????category
     */
    private void purchasedCate(ParseObject category) {
        //??????????????????
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
        query1.whereEqualTo(CATEGORY_POINTER, category);
        query1.whereEqualTo(USER_POINTER, currentUser);
        query1.findInBackground((objects, e) -> {
            if (e == null){
                if (judgeEmptyList(objects)){
                    //???????????????????????????
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(SUBJECT_CLASS_NAME);
                    query.whereEqualTo(CATEGORY_POINTER,category);
                    query.findInBackground((object2, e2) -> {
                        if (e2 == null){
                            if (!judgeEmptyList(object2)){
                                ParseUser user = ParseUser.getCurrentUser();
                                ParseObject pObj = ParseObject.create(PURCHASED_HISTORY_CLASS_NAME);
                                List<String> purSubList = user.getList(USER_PURCHASED_SUBJECT);
                                List<String> purCateList = user.getList(USER_PURCHASED_CATEGORY);
                                if (purSubList == null){
                                    purSubList = new ArrayList<>();
                                }
                                for (ParseObject subject : object2){
                                    if (!purSubList.contains(subject.getObjectId())){
                                        purSubList.add(subject.getObjectId());
                                    }
                                }
                                if (purCateList == null){
                                    purCateList = new ArrayList<>();
                                }
                                if (!purCateList.contains(category.getObjectId())){
                                    purCateList.add(category.getObjectId());
                                }
                                user.put(USER_PURCHASED_CATEGORY,purCateList);
                                user.put(USER_PURCHASED_SUBJECT,purSubList);
                                pObj.put(USER_POINTER, user);
                                pObj.put(CATEGORY_POINTER, category);
                                double sPrice = category.getDouble(CATEGORY_GOOGLE_SPECIAL_PRICE);
                                if (sPrice > 0) {
                                    pObj.put(PURCHASED_HISTORY_PRICE, sPrice);
                                } else {
                                    double oPrice = category.getDouble(CATEGORY_GOOGLE_ORIGINAL_PRICE);
                                    pObj.put(PURCHASED_HISTORY_PRICE, oPrice);
                                }
                                pObj.saveInBackground(e1 -> {
                                    hideLoading();
                                    if (e1 == null) {
                                        ToastUtils.showBlackCenterToast(this, getString(R.string.tipsSavePurchasedHistorySuccess));
                                        initData();
                                    } else {
                                        ToastUtils.showBlackCenterToast(this, getString(R.string.tipsSavePurchasedHistoryFailed) + "???" + e1.getMessage());
                                    }
                                });
                            }
                        }else {
                            hideLoading();
                            ToastUtils.showBlackCenterToast(this,getString(R.string.tipsGetDataFailed) + "???" + e2.getMessage());
                        }
                    });
                }else {
                    //??????????????????????????????
                    hideLoading();
                    ToastUtils.showBlackCenterToast(this, getString(R.string.tipsAlreadyPurchasedPlzNotAgain));
                }
            }else {
                hideLoading();
                ToastUtils.showBlackCenterToast(this, getString(R.string.tipsPurchasedFailed) + "???" + e.getMessage());
            }
        });
    }

    /**
     * ??????subject
     */
    private static void purchasedSubject(Context context,ParseObject category,ParseObject subject) {
        //???????????????????????????
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
        query.whereEqualTo(CATEGORY_POINTER, category);
        query.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
        query.findInBackground((objects, e) -> {
            if (e == null){
                if (judgeEmptyList(objects)){
                    //???????????????????????????????????????
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                    query1.whereEqualTo(SUBJECT_POINTER,subject);
                    query1.whereEqualTo(USER_POINTER,ParseUser.getCurrentUser());
                    query1.getFirstInBackground((object1, e1) -> {
                        if (e1 == null){
                            if (judgeObject(object1)){
                                //?????????????????????????????????
                                ParseUser user = ParseUser.getCurrentUser();
                                List<String> purSubList = user.getList(USER_PURCHASED_SUBJECT);
                                if (purSubList == null) purSubList = new ArrayList<>();
                                if (!purSubList.contains(subject.getObjectId())){
                                    purSubList.add(subject.getObjectId());
                                }
                                user.put(USER_PURCHASED_SUBJECT,purSubList);
                                ParseObject pObj = ParseObject.create(PURCHASED_HISTORY_CLASS_NAME);
                                ParseACL acl = new ParseACL();
                                acl.setWriteAccess(user,true);
                                pObj.setACL(acl);
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
                                        ToastUtils.showBlackCenterToast(context, context.getString(R.string.tipsSavePurchasedHistorySuccess));
                                    } else {
                                        ToastUtils.showBlackCenterToast(context, context.getString(R.string.tipsSavePurchasedHistoryFailed) + "???" + e2.getMessage());
                                    }
                                });
                            }else {
                                //???????????????
                                hideLoading();
                                ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsAlreadyPurchasedPlzNotAgain));
                            }
                        }else {
                            hideLoading();
                            ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed) + "???" + e1.getMessage());
                        }
                    });
                }else {
                    //?????????????????????
                    hideLoading();
                    ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsAlreadyPurchasedPlzNotAgain));
                }
            }else {
                hideLoading();
                ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed) + "???" + e.getMessage());
            }
        });
    }

    /**
     * ?????????ViewHolder
     */
    static class ViewHolder extends RecyclerAdapter.ViewHolder<SubjectBean>{
        @BindView(R.id.isnPositionTv)
        TextView tvPosition;
        @BindView(R.id.isnNameTv)
        TextView tvName;
        @BindView(R.id.isnPassCountTv)
        TextView tvPassNum;
        @BindView(R.id.isnSingleCountTv)
        TextView tvSinNum;

        @BindView(R.id.isnPriceLayout)
        LinearLayout llPrice;
        @BindView(R.id.isnOriginalPriceTv)
        TextView tvOPrice;
        @BindView(R.id.isnSpecialPriceTv)
        TextView tvSPrice;
        @BindView(R.id.isnSpecialStatus)
        TextView staSpecial;

        @BindView(R.id.isnFreeBtn)
        TextView btnFree;
        @BindView(R.id.isnPurchasedBtn)
        TextView btnSinglePurchased;
        @BindView(R.id.isnPurNumTv)
        TextView tvPNum;

        private Context context;
        private Activity activity;

        ViewHolder(View itemView, Context context, Activity activity) {
            super(itemView);
            this.context = context;
            this.activity = activity;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(SubjectBean bean) {
            tvPosition.setText((getAdapterPosition() + 1) + "");
            //????????????
            String name = bean.getSubjectName();
            setText(tvName, name);
            //??????
            int passCount = bean.getPassCount();
            int singleCount = bean.getSingleCount();
            setText(tvPassNum, "???????????????" + passCount);
            setText(tvSinNum, "???????????????" + singleCount);
            //????????????
            tvPNum.setVisibility(View.GONE);
            boolean isBuy = isLogin() && bean.isBuy();
            setPurchased(isBuy);
            if (!isBuy) {
                //?????????????????????
                //??????
                float oPrice = bean.getSubjectGoogleOriginalPrice();
                float sPrice = bean.getSubjectGoogleSpecialPrice();
                setText(tvOPrice, "$" + oPrice);
                boolean isSpecial = sPrice > 0;
                tvSPrice.setVisibility(View.GONE);
                staSpecial.setVisibility(View.GONE);
                if (isSpecial) {
                    setText(tvSPrice, "$" + sPrice);
                    tvSPrice.setVisibility(View.VISIBLE);
                    setText(staSpecial, context.getString(R.string.titleLimitedSpecial));
                    staSpecial.setBackgroundResource(R.drawable.bg_corner_redborder_whitebg);
                    staSpecial.setVisibility(View.VISIBLE);
                    tvOPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //????????????
                    tvOPrice.setTextColor(ColorUtils.getColor(R.color.colorBF));
                }
            }
            btnFree.setOnClickListener(view -> {
                int type = isBuy ? EXAMINATION_STATUS_FOR_PURCHASED : EXAMINATION_STATUS_FOR_FREE;
                ToastUtils.showBlackCenterToast(context, isBuy ? "????????????????????????????????????" : "????????????????????????");
                goExamination(context,type,categoryBean,bean);
            });

            btnSinglePurchased.setOnClickListener(view -> {
                if (isLogin()) {
                    //??????
                    if (isBuy) {
                        //????????????????????????
                       goExamination(context,EXAMINATION_STATUS_FOR_PURCHASED,categoryBean,bean);
                    } else {
                        //????????????????????????
                        showPurSubjectDialog(context, bean, view12 -> {
                            //????????????
                            showLoading(context);
                            //??????beanId????????????subject
                            ParseQuery<ParseObject> query = ParseQuery.getQuery(SUBJECT_CLASS_NAME);
                            query.whereEqualTo(OBJECT_ID,bean.getSubjectId());
                            query.getFirstInBackground((object, e) -> {
                                if (e == null){
                                    if (judgeObject(object)){
                                        //subject??????
                                        hideLoading();
                                        ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed));
                                    }else {
                                        //????????????subject
                                        object.fetchIfNeededInBackground((object1, e1) -> {
                                            if (e1 == null){
                                                //??????subject??????
                                                if (judgeObject(object1)){
                                                    hideLoading();
                                                    ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed));
                                                }else{
                                                    //????????????category
                                                    ParseObject category = object1.getParseObject(CATEGORY_POINTER);
                                                    if (judgeObject(category)){
                                                        //??????category??????
                                                        hideLoading();
                                                        ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed));
                                                    }else {
                                                        //category???????????????????????????????????????category???subject
                                                        purchasedSubject(context,category,object);
                                                    }
                                                }
                                            }else {
                                                //????????????subject??????
                                                hideLoading();
                                                ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed) + "???" + e1.getMessage());
                                            }
                                        });
                                    }
                                }else {
                                    //??????subject??????
                                    hideLoading();
                                    ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed) + "???" + e.getMessage());
                                }
                            });
                        });
                    }
                } else {
                    //?????????
                    noLoginDialog(context, view1 -> {
                        closeListenerDialog();
                        activity.finish();
                    });
                }
            });
        }

        @Override
        protected void onBind(SubjectBean subjectBean, List<Object> payloads) {

        }

        /**
         * ????????????????????????
         */
        private void setPurchased(boolean isPurchased) {
            llPrice.setVisibility(isPurchased ? View.GONE : View.VISIBLE);
            btnFree.setVisibility(isPurchased ? View.GONE : View.VISIBLE);
            btnSinglePurchased.setActivated(isPurchased);
            btnSinglePurchased.setBackgroundResource(R.drawable.bg_button_normal_activited_selector);
            btnSinglePurchased.setText(context.getString(isPurchased ? R.string.buttonGotoExamination : R.string.buttonSinglePurchased));
            if (isPurchased) tvPNum.setVisibility(View.GONE);
        }

    }

    /**
     * ?????????ViewHolder
     */
    static class ViewHolder2 extends RecyclerAdapter.ViewHolder<ParseObject> {
        @BindView(R.id.isnPositionTv)
        TextView tvPosition;
        @BindView(R.id.isnNameTv)
        TextView tvName;
        @BindView(R.id.isnPassCountTv)
        TextView tvPassNum;
        @BindView(R.id.isnSingleCountTv)
        TextView tvSinNum;

        @BindView(R.id.isnPriceLayout)
        LinearLayout llPrice;
        @BindView(R.id.isnOriginalPriceTv)
        TextView tvOPrice;
        @BindView(R.id.isnSpecialPriceTv)
        TextView tvSPrice;
        @BindView(R.id.isnSpecialStatus)
        TextView staSpecial;

        @BindView(R.id.isnFreeBtn)
        TextView btnFree;
        @BindView(R.id.isnPurchasedBtn)
        TextView btnSinglePurchased;
        @BindView(R.id.isnPurNumTv)
        TextView tvPNum;

        private Context context;
        private Activity activity;

        ViewHolder2(View itemView, Context context, Activity activity) {
            super(itemView);
            this.context = context;
            this.activity = activity;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(ParseObject sObj) {
            tvPosition.setText((getAdapterPosition() + 1) + "");
            //????????????
            String name = sObj.getString(SUBJECT_NAME);
            setText(tvName, name);
            //??????
            int passCount = sObj.getInt(SUBJECT_PASS_COUNT);
            int singleCount = sObj.getInt(SUBJECT_SINGLE_COUNT);
            setText(tvPassNum, "???????????????" + passCount);
            setText(tvSinNum, "???????????????" + singleCount);
            //????????????
            tvPNum.setVisibility(View.GONE);
            if (isLogin()) {
                //??????
                ParseQuery<ParseObject> query = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                query.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
                query.whereEqualTo(CATEGORY_POINTER, cObj);
                query.findInBackground((objects, e) -> {
                    if (e == null) {
                        if (judgeEmptyList(objects)) {
                            //??????????????????
                            ParseQuery<ParseObject> query1 = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
                            query1.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
                            query1.whereEqualTo(SUBJECT_POINTER, sObj);
                            query1.findInBackground((objects1, e1) -> {
                                if (e1 == null) {
                                    if (judgeEmptyList(objects1)) {
                                        //?????????
                                        setPrice(sObj);
                                    }
                                    setPurchased(!judgeEmptyList(objects1), sObj);
                                } else {
                                    setPrice(sObj);
                                    setPurchased(false, sObj);
                                }
                            });
                        } else {
                            //???????????????????????????
                            setPurchased(true, sObj);
                        }
                    } else {
                        setPrice(sObj);
                        setPurchased(false, sObj);
                    }
                });
            } else {
                //?????????
                setPrice(sObj);
                setPurchased(false, sObj);
            }
        }

        @Override
        protected void onBind(ParseObject sObj, List<Object> payloads) {

        }

        /**
         * ????????????
         */
        private void setPrice(ParseObject sObj) {
            llPrice.setVisibility(View.VISIBLE);
            //??????
            double oPrice = sObj.getDouble(SUBJECT_GOOGLE_ORIGINAL_PRICE);
            double sPrice = sObj.getDouble(SUBJECT_GOOGLE_SPECIAL_PRICE);
            setText(tvOPrice, "$" + oPrice);
            boolean isSpecial = sPrice > 0;
            tvSPrice.setVisibility(View.GONE);
            staSpecial.setVisibility(View.GONE);
            if (isSpecial) {
                setText(tvSPrice, "$" + sPrice);
                tvSPrice.setVisibility(View.VISIBLE);
                setText(staSpecial, context.getString(R.string.titleLimitedSpecial));
                staSpecial.setBackgroundResource(R.drawable.bg_corner_redborder_whitebg);
                staSpecial.setVisibility(View.VISIBLE);
                tvOPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //????????????
                tvOPrice.setTextColor(ColorUtils.getColor(R.color.colorBF));
            }
        }

        /**
         * ????????????????????????
         */
        private void setPurchased(boolean isPurchased, ParseObject sObj) {
            llPrice.setVisibility(isPurchased ? View.GONE : View.VISIBLE);
            btnFree.setVisibility(isPurchased ? View.GONE : View.VISIBLE);
            btnSinglePurchased.setActivated(isPurchased);
            btnSinglePurchased.setBackgroundResource(R.drawable.bg_button_normal_activited_selector);
            btnSinglePurchased.setText(context.getString(isPurchased ? R.string.buttonGotoExamination : R.string.buttonSinglePurchased));
            if (isPurchased) tvPNum.setVisibility(View.GONE);

            btnFree.setOnClickListener(view -> {
                int type = isPurchased ? EXAMINATION_STATUS_FOR_PURCHASED : EXAMINATION_STATUS_FOR_FREE;
                ToastUtils.showBlackCenterToast(context, isPurchased ? "????????????????????????????????????" : "????????????????????????");
                goExamination(context,type,cObj,sObj);
            });

            btnSinglePurchased.setOnClickListener(view -> {
                if (isLogin()) {
                    //??????
                    if (isPurchased) {
                        //????????????????????????
                        goExamination(context,EXAMINATION_STATUS_FOR_PURCHASED,cObj,sObj);
                    } else {
                        //????????????????????????
                        showPurSubjectDialog(context, sObj, view12 -> {
                            showLoading(context);
                            //????????????category
                            ParseObject category = sObj.getParseObject(CATEGORY_POINTER);
                            if (judgeObject(category)){
                                //??????category??????
                                hideLoading();
                                ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed));
                            }else {
                                //???????????????????????????????????????category???subject
                                purchasedSubject(context,category,sObj);
                            }
                        });
                    }
                } else {
                    //?????????
                    noLoginDialog(context, view1 -> {
                        closeListenerDialog();
                        activity.finish();
                    });
                }
            });
        }


    }
}
