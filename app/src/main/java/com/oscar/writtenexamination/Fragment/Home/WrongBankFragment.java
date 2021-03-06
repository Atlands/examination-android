package com.oscar.writtenexamination.Fragment.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.leaf.library.StatusBarUtil;
import com.oscar.writtenexamination.Activity.CategorySubject.SubjectActivity;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Adapter.WrongBankAdapter;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Utils.UiUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.USER_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.WRONG_BANK_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.isNetConnect;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.judgeObject;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;
import static com.oscar.writtenexamination.Base.Configurations.setText;

public class WrongBankFragment extends Fragment implements View.OnClickListener,WaveSwipeRefreshLayout.OnRefreshListener{

    //Toolbar
    @BindView(R.id.fwToolbar)
    Toolbar mToolbar;
    @BindView(R.id.fwTitleTv)
    TextView tvTitle;

    @BindView(R.id.fwRefresh)
    WaveSwipeRefreshLayout refreshLayout;
    @BindView(R.id.fwMainLayout)
    RelativeLayout llMain;
    @BindView(R.id.fwExpandableListView)
    ExpandableListView listView;
    //????????????
    @BindView(R.id.fwNodateTv)
    TextView tvNodate;
    //??????
    @BindView(R.id.fwFailedLayout)
    LinearLayout llFailed;

    //??????
    int status = 0; //0??????,1???????????????2?????????3??????
    //??????
    List<ParseObject> wrongList = new ArrayList<>();
    List<String> cateList = new ArrayList<>();
    List<ParseObject> subjectList = new ArrayList<>();
    List<List<ParseObject>> dataList = new ArrayList<>();
    //?????????
    WrongBankAdapter adapter;

    /**
     * ?????????????????????????????????Id
     *
     * @return ????????????Id
     */
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_wrong_bank;
    }

    /**
     * ???????????????
     */
    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        StatusBarUtil.setGradientColor(mActivity, mToolbar);
        UiUtils.setStatusBarDarkTheme(mActivity, true);
        //???????????????
        setText(tvTitle, getString(R.string.titleWrongBank));
        refreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        refreshLayout.setWaveColor(ColorUtils.getColor(R.color.colorMainTwo));
    }

    @Override
    protected void initData() {
        if (!isNetConnect) {
            ToastUtils.showBlackCenterToast(mContext, getString(R.string.tipsNetworkConnectFailedPlzCheckIt));
            return;
        }
        if (isLogin()) {
            queryWrongBank();
        } else {
            //?????????
            noLoginDialog(mContext, this);
        }
    }

    @Override
    protected void setListener() {
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh(){
        initData();
        new Handler().postDelayed(() -> refreshLayout.setRefreshing(false), 1000);
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    /**
     * ???????????????
     */
    private void queryWrongBank() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(WRONG_BANK_CLASS_NAME);
        query.whereEqualTo(USER_POINTER, user);
        query.orderByDescending(SUBJECT_POINTER);
        query.findInBackground((objects, e) -> {
            if (e == null) {
                status = judgeEmptyList(objects) ? 1 : 2;
                if (status == 2){
                    wrongList = objects;
                    handleSubject();
                }
            } else {
                status = 3;
                ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsGetDataFailed));
            }
            setMainLayout();
        });
    }

    /**
     * ?????????????????????
     */
    private void handleSubject() {
        for (ParseObject wrong : wrongList){
            ParseObject subject = wrong.getParseObject(SUBJECT_POINTER);
            if(!judgeObject(subject)){
                subject.fetchIfNeededInBackground((object, e) -> {
                    if (e == null && !judgeObject(object)){
                        if (!subjectList.contains(object)){
                            subjectList.add(object);
                        }
                        if (subjectList.size() == wrongList.size()){
                            handleCate();
                        }
                    }
                });
            }
        }
    }

    /**
     * ?????????????????????
     */
    private void handleCate() {
        for (ParseObject subject : subjectList){
            ParseObject category = subject.getParseObject(CATEGORY_POINTER);
            if (category != null){
                String cId = category.getObjectId();
                if (!TextUtils.isEmpty(cId) && !cateList.contains(cId)){
                    cateList.add(cId);
                }
            }
        }
        if (cateList.size() > 0){
            handleData();
        }
    }

    /**
     * ????????????
     */
    private void handleData() {
        for (String cId : cateList){
            List<ParseObject> list = new ArrayList<>();
            for (ParseObject subject : subjectList){
                ParseObject cObj = subject.getParseObject(CATEGORY_POINTER);
                if (cObj != null){
                    if (cObj.getObjectId().equals(cId)){
                        list.add(subject);
                    }
                }
            }
            dataList.add(list);
        }
        if (dataList.size() > 0){
            initWrongBank();
        }
    }

    /**
     * ??????????????????
     */
    private void initWrongBank() {
        adapter = new WrongBankAdapter(mContext,cateList,dataList);
        listView.setAdapter(adapter);
        //??????????????????
        listView.setOnGroupClickListener((expandableListView, view, groupPosition, l) -> {
            //?????????????????????????????????????????????true,???????????????????????????onTouch?????????????????????????????????????????????
            return false;
        });
        //???????????????????????????
        listView.setOnGroupExpandListener(groupPosition -> {
            for (int i = 0; i < cateList.size(); i++) {
                if (groupPosition != i) {
                    listView.collapseGroup(i);
                }
            }
        });
        listView.setOnChildClickListener((expandableListView, view, i1, i2, l) -> {
            //?????????????????????????????????????????????true
            ToastUtils.showBlackCenterToast(mContext,"??????????????????");
            return false;
        });
    }

    /**
     * ???????????????0?????????1????????????2?????????3??????
     */
    void setMainLayout(){
        tvNodate.setVisibility(View.GONE);
        llMain.setVisibility(View.GONE);
        llFailed.setVisibility(View.GONE);
        switch (status){
            case 1:
                tvNodate.setVisibility(View.VISIBLE);
                break;
            case 2:
                llMain.setVisibility(View.VISIBLE);
                break;
            case 3:
                llFailed.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) { }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dnliConfirmBtn:
                closeListenerDialog();
                LoginSignActivity.show(mContext, null);
                break;
        }
    }
}
