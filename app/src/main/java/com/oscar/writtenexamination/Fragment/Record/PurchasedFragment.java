package com.oscar.writtenexamination.Fragment.Record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.oscar.writtenexamination.Activity.LoginSign.LoginSignActivity;
import com.oscar.writtenexamination.Adapter.RecyclerAdapter;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_NAME;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.PURCHASED_HISTORY_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.PURCHASED_HISTORY_PRICE;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.USER_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.judgeObject;
import static com.oscar.writtenexamination.Base.Configurations.noLoginDialog;
import static com.oscar.writtenexamination.Base.Configurations.setText;

public class PurchasedFragment extends Fragment implements View.OnClickListener{


    //??????
    @BindView(R.id.fphFailedLayout)
    LinearLayout llFailed;

    //?????????
    @BindView(R.id.fphNodataTv)
    TextView tvNodata;

    //??????
    @BindView(R.id.fphMainLayout)
    LinearLayout llMain;
    @BindView(R.id.fpwRy)
    RecyclerView recyclerView;

    //??????
    private int status;
    //??????
    private List<ParseObject> historyList = new ArrayList<>();
    //?????????
    private RecyclerAdapter<ParseObject> mAdapter;



    public static Fragment newInstance(){
        return new PurchasedFragment();
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_purchased_history;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    protected void initData() {
        if (isLogin()){
            //??????
            queryHistory(0);
        }else {
            //?????????
            noLoginDialog(mContext, this);
        }
    }


    /**
     * ??????????????????
     */
    private void queryHistory(int way){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PURCHASED_HISTORY_CLASS_NAME);
        setQuery(query,way);
        query.findInBackground((objects, e) -> {
            if (e == null){
                status = judgeEmptyList(objects) ? 1 : 2;
                if (status == 2){
                    historyList = objects;
                    initHistory();
                }
            }else {
                status = 3;
            }
            setMainLayout();
        });
    }

    private void setQuery(ParseQuery<ParseObject> query,int way){
        query.whereEqualTo(USER_POINTER,ParseUser.getCurrentUser());
        switch (way){
            case 1:
                query.whereDoesNotExist(SUBJECT_POINTER);
                break;
            case 2:
                query.whereDoesNotExist(CATEGORY_POINTER);
                break;
            case 3:
                query.whereDoesNotExist(CATEGORY_POINTER);
                query.whereDoesNotExist(SUBJECT_POINTER);
                break;
        }
    }

    /**
     * ?????????????????????
     */
    private void initHistory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        if (mAdapter == null) {
            mAdapter = new RecyclerAdapter<ParseObject>(historyList, null) {
                @Override
                protected int getItemViewType(int position, ParseObject hObj) {
                    return R.layout.item_purchased_history;
                }

                @Override
                protected ViewHolder<ParseObject> onCreateViewHolder(View root, int viewType) {
                    return new PurchasedFragment.ViewHolder(root,mContext);
                }
            };

            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * ?????????????????????0?????????1????????????2?????????3??????
     */
    private void setMainLayout() {
        llFailed.setVisibility(View.GONE);
        llMain.setVisibility(View.GONE);
        tvNodata.setVisibility(View.GONE);
        switch (status){
            case 1:
                tvNodata.setVisibility(View.VISIBLE);
                break;
            case 2:
                llMain.setVisibility(View.VISIBLE);
                break;
            case 3:
                llFailed.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.fphRegetBtn)
    void onReget(){
        initData();
    }

//    @OnClick(R.id.fphScreenBtn)
    void onScreen(){
//        int postion;
//        String title = tvScreen.getText().toString();
//        if (title.equals(getString(R.string.normalCategoryPurchasedRecords))){
//            postion = 1;
//        }else if (title.equals(getString(R.string.normalSubjectPurchasedRecords))){
//            postion = 2;
//        }else if (title.equals(getString(R.string.normalOtherPurchasedRecords))){
//            postion = 3;
//        }else {
//            postion = 0;
//        }
//        showHistoryScreenDialog(mContext,postion,this);
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
        initData();
        super.onResume();
    }

    /**
     * ?????????ViewHolder
     */
    static class ViewHolder extends RecyclerAdapter.ViewHolder<ParseObject>{

        @BindView(R.id.iphTypeTv)
        TextView tvType;
        @BindView(R.id.iphNameTv)
        TextView tvName;
        @BindView(R.id.iphDateTv)
        TextView tvDate;
        @BindView(R.id.iphPriceTv)
        TextView tvPrice;

        private Context context;

        private int[] roundRes = new int[]{R.drawable.bg_round_gray,R.drawable.bg_round_normal,R.drawable.bg_round_green};

        ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        @Override
        protected void onBind(ParseObject object) {
            int type; // 0?????????1???????????????2????????????
            ParseObject cObj = object.getParseObject(CATEGORY_POINTER);
            ParseObject sObj = object.getParseObject(SUBJECT_POINTER);
            if (cObj == null && sObj == null){
                type = 0;
                setData(type,null);
            }else {
                type = cObj == null ? 1 : 2;
                setData(type,type == 1 ? sObj : cObj);
            }
            setDate(object);
            double price = object.getDouble(PURCHASED_HISTORY_PRICE);
            setText(tvPrice,"- $ " + price);
        }


        /**
         * ??????????????????
         * @param type ??????
         */
        private void setData(int type,ParseObject object) {
            switch (type){
                case 0:
                    tvType.setText("???");
                    setText(tvName,"????????????");
                    break;
                case 1:
                    tvType.setText("???");
                    if (object != null){
                        object.fetchIfNeededInBackground(this::done);
                    }else {
                        setText(tvName,"????????????");
                    }
                    break;
                case 2:
                    tvType.setText("???");
                    if (object != null){
                        object.fetchIfNeededInBackground(this::done2);
                    }else {
                        setText(tvName,"????????????");
                    }
                    break;
            }
            tvType.setBackgroundResource(roundRes[type]);
        }

        /**
         * ????????????
         */
        private void setDate(ParseObject object) {
            Calendar calendar = Calendar.getInstance();
            int a = calendar.get(Calendar.YEAR);
            Date date = object.getCreatedAt();
            calendar.setTime(date);
            int b = calendar.get(Calendar.YEAR);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sd = new SimpleDateFormat(a== b ? "MM???dd???  HH???mm???" : "yyyy???MM???dd??? HHH???mm???");
            setText(tvDate,sd.format(date));
        }

        @Override
        protected void onBind(ParseObject object, List<Object> payloads) {

        }

        private void done(ParseObject object1, ParseException e) {
            String name;
            if (e == null && !judgeObject(object1)) {
                name = "????????????--" + object1.getString(SUBJECT_NAME);
            } else {
                name = "????????????";
            }
            setText(tvName, name);
        }

        private void done2(ParseObject object1, ParseException e) {
            String name;
            if (e == null && !judgeObject(object1)) {
                name = "????????????--" + object1.getString(CATEGORY_NAME);
            } else {
                name = "????????????";
            }
            setText(tvName, name);
        }
    }
}
