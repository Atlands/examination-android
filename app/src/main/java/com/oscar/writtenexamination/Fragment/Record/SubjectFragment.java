package com.oscar.writtenexamination.Fragment.Record;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Adapter.RecyclerAdapter;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.EXAMINATION_STATUS_FOR_PURCHASED;
import static com.oscar.writtenexamination.Base.Configurations.PURCHASED_HISTORY_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_INFO;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_PASS_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_SINGLE_COUNT;
import static com.oscar.writtenexamination.Base.Configurations.USER_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.goExamination;
import static com.oscar.writtenexamination.Base.Configurations.goSubject;
import static com.oscar.writtenexamination.Base.Configurations.hideLoading;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.judgeObject;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;
import static com.oscar.writtenexamination.Base.Configurations.setText;
import static com.oscar.writtenexamination.Base.Configurations.showLoading;

public class SubjectFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.fslLoadingLayout)
    RelativeLayout llLoading;

    //失败
    @BindView(R.id.fslFailedLayout)
    LinearLayout llFailed;

    //没数据
    @BindView(R.id.fslNodataTv)
    TextView tvNodata;


    @BindView(R.id.fslRy)
    RecyclerView recyclerView;

    //参数
    private int status;
    //数据
    private List<ParseObject> purchasedList = new ArrayList<>();
    //适配器
    private RecyclerAdapter<ParseObject> mAdapter;

    public static Fragment newInstance(){
        return new SubjectFragment();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_subject_list;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    protected void initData() {
        if (isLogin()){
            //登入
            status = 0;
            setMainLayout();
            queryHistory();
        }else {
            noLoginDialog(mContext,this);
        }
    }

    /**
     * 加载历史记录
     */
    private void queryHistory(){
        purchasedList.clear();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
        query.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
        query.findInBackground((objects, e) -> {
            if (e == null){
                status = judgeEmptyList(objects) ? 1 : 2;
                if (status == 2){
                    handleData(objects);
                }
            }else {
                status = 3;
                setMainLayout();
            }
        });
    }

    private void handleData(List<ParseObject> list) {
        int position = 0;
        for (ParseObject object : list){
            ParseObject cObj = object.getParseObject(CATEGORY_POINTER);
            ParseObject sObj = object.getParseObject(SUBJECT_POINTER);
            if (cObj == null && sObj == null){
                position++;
            }else {
                position++;
                if (cObj == null){
                    //单独购买
                    purchasedList.add(sObj);
                    if (position == list.size()){
                        initPurchased();
                    }
                }else {
                    //合辑购买
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(SUBJECT_CLASS_NAME);
                    query.whereEqualTo(CATEGORY_POINTER,cObj);
                    int finalPosition = position;
                    query.findInBackground((objects, e) -> {
                        if (e == null){
                            for (ParseObject subject : objects){
                                boolean isContain = false;
                                for (ParseObject pObj : purchasedList){
                                    if (pObj.getObjectId().equals(subject.getObjectId())){
                                        isContain = true;
                                        break;
                                    }
                                }
                                if (!isContain){
                                    purchasedList.add(subject);
                                }
                            }
                            if (finalPosition == list.size()){
                                initPurchased();

                            }
                        }
                    });
                }
            }
            setMainLayout();
        }
    }

    private void initPurchased() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        if (mAdapter == null) {
            mAdapter = new RecyclerAdapter<ParseObject>(purchasedList, null) {
                @Override
                protected int getItemViewType(int position, ParseObject hObj) {
                    return R.layout.item_subject_purchased;
                }

                @Override
                protected ViewHolder<ParseObject> onCreateViewHolder(View root, int viewType) {
                    return new SubjectFragment.ViewHolder(root,mContext);
                }
            };
            mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<ParseObject>() {
                @Override
                public void onItemClick(RecyclerAdapter.ViewHolder holder, ParseObject object) {
                    showLoading(mContext);
                    object.fetchIfNeededInBackground((object1, e) -> {
                        if (e == null){
                            if (judgeObject(object1)){
                                hideLoading();
                                ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsGetDataFailed));
                            }else {
                                ParseObject cObj = object1.getParseObject(CATEGORY_POINTER);
                                if (cObj == null){
                                    hideLoading();
                                    ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsGetDataFailed));
                                }else {
                                    cObj.fetchIfNeededInBackground((object2, e1) -> {
                                        hideLoading();
                                        if (e1 == null){
                                            if (judgeObject(object2)){
                                                ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsGetDataFailed));
                                            }else {
                                                //跳转到sub页
                                                goSubject(mContext,object2,object1);
                                            }
                                        }else {
                                            ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsGetDataFailed) + "：" + e1.getMessage());
                                        }
                                    });
                                }
                            }
                        }else {
                            hideLoading();
                            ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsGetDataFailed) + "：" + e.getMessage());
                        }
                    });
                }
            });
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置主要布局，0加载，1空数据，2成功，3失败
     */
    private void setMainLayout() {
        llFailed.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvNodata.setVisibility(View.GONE);
        llLoading.setVisibility(View.GONE);
        switch (status){
            case 0:
                llLoading.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvNodata.setVisibility(View.VISIBLE);
                break;
            case 2:
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case 3:
                llFailed.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.fslRegetBtn)
    void onReget(){
        initData();
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) { }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dnliConfirmBtn:
                closeListenerDialog();
                LoginSignActivity.show(mContext,null);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 科目表ViewHolder
     */
    static class ViewHolder extends RecyclerAdapter.ViewHolder<ParseObject>{

        @BindView(R.id.ispNameTv)
        TextView tvName;
        @BindView(R.id.ispInforTv)
        TextView tvInfor;
        @BindView(R.id.ispSingleCountTv)
        TextView tvSingle;
        @BindView(R.id.ispPassCountTv)
        TextView tvPass;
        @BindView(R.id.ispExamBtn)
        TextView btnExam;

        private Context context;


        ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        @Override
        protected void onBind(ParseObject sObj) {
            sObj.fetchIfNeededInBackground((object, e) -> {
                if (e == null && !judgeObject(object)){
                    String name = object.getString(SUBJECT_NAME);
                    String infor = object.getString(SUBJECT_INFO);
                    int singleCount = object.getInt(SUBJECT_SINGLE_COUNT);
                    int passCount = object.getInt(SUBJECT_PASS_COUNT);
                    setText(tvName,name);
                    setText(tvInfor,infor);
                    setText(tvSingle,"總共題數：" + singleCount + "題");
                    setText(tvPass,"合格題數：" + passCount + "題");
                    btnExam.setOnClickListener(view -> {
                        showLoading(context);
                        ParseObject cObj = object.getParseObject(CATEGORY_POINTER);
                        if (judgeObject(cObj)){
                            hideLoading();
                            ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed));
                        }else {
                            cObj.fetchIfNeededInBackground((object1, e1) -> {
                                hideLoading();
                                if (e1 == null){
                                    if (judgeObject(object1)){
                                        ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed));
                                    }else {
                                        goExamination(context,EXAMINATION_STATUS_FOR_PURCHASED,object1,object);
                                    }
                                }else {
                                    ToastUtils.showBlackCenterToast(context,context.getString(R.string.tipsGetDataFailed) + "：" + e1.getMessage());
                                }
                            });
                        }
                    });
                }
            });

        }


        @Override
        protected void onBind(ParseObject object, List<Object> payloads) {

        }
    }
}
