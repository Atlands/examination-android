package com.oscar.writtenexamination.Fragment.Examination;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.oscar.writtenexamination.Activity.Examination.ExaminationActivity;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.Bean.CategoryBean;
import com.oscar.writtenexamination.Bean.SubjectBean;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Widget.CircularStatisticsView;
import java.text.NumberFormat;
import butterknife.BindView;
import butterknife.OnClick;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.activity;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.cObj;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.categoryBean;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.correctList;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.examinationType;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.pagerAdapter;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.passPage;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.questionBeanList;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.sObj;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.subjectBean;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.totalPage;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.tvSName;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.viewPager;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.wrongList;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_BEAN;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.EXAMINATION_STATUS_FOR_PURCHASED;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_BEAN;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.closeListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.isLogin;
import static com.oscar.writtenexamination.Base.Configurations.setText;
import static com.oscar.writtenexamination.Base.Configurations.showListenerDialog;
import static com.oscar.writtenexamination.Base.Configurations.switchHomeVp;

public class ScroesItemFragment extends Fragment {

    @BindView(R.id.fsiSubjectNameTv)
    TextView tvName;
    @BindView(R.id.fsiCirStaView)
    CircularStatisticsView csView;
    @BindView(R.id.fsiScroeMumTv)
    TextView tvScroe;
    @BindView(R.id.fsiAllCountTv)
    TextView tvAllCount;
    @BindView(R.id.fsiCorrectNumTv)
    TextView tvCorrect;
    @BindView(R.id.fsiWrongNumTv)
    TextView tvWrong;
    @BindView(R.id.fsiResultTv)
    TextView tvResult;
    @BindView(R.id.fsiFinishBtn)
    TextView btnFinish;
    @BindView(R.id.fsiRestartBtn)
    TextView btnRestart;
    @BindView(R.id.fsiWrongBankBtn)
    TextView btnWrongBank;

    private int totalCount;
    int passCount;
    int wrongCount = 0;
    int correctCount = 0;

    boolean isVisible = false;

    public ScroesItemFragment() {
        totalCount = totalPage;
        passCount = passPage;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_scroes_item;
    }

    /**
     * 可见
     * */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 延迟加载
     */
    private void lazyLoad() {
        wrongCount = wrongList.size();
        correctCount = correctList.size();
        boolean isNormal = wrongCount + correctCount == totalCount;
        btnFinish.setActivated(isNormal);
        btnRestart.setActivated(isNormal);
        boolean isWrong = wrongCount > 0;
        if (isLogin() && examinationType == EXAMINATION_STATUS_FOR_PURCHASED){
            btnWrongBank.setVisibility(isWrong ? View.VISIBLE : View.GONE);
        }
        if (isNormal){
            csView.setCircleWidth(20);
            csView.setPercentage(wrongCount,correctCount);
            setText(tvName,tvSName.getText().toString());
            setText(tvScroe,getPer(correctCount,totalCount));
            setText(tvAllCount,"本考試共" + totalCount + "題，答對" + passCount + "題即通過考試");
            setText(tvCorrect,correctCount + "");
            setText(tvWrong,wrongCount + "");
            boolean isPass = correctCount >= passCount;
            setText(tvResult,isPass ? "恭喜你，成功通過考試！" : "通過考試失敗，繼續加油！");
        }else {
            //做题数不足，返回最前未做的一道题
            showListenerDialog(mContext, R.drawable.ic_tips_normal, getString(R.string.tipsWarmReminder),
                    getString(R.string.tipsPreNotFinish), "前往", null, false, view -> {
                        closeListenerDialog();
                        int target = questionBeanList.size() - 1; //默认最后一题
                        for(int index : pagerAdapter.indexs){
                            if (!correctList.contains(index) && !wrongList.contains(index)) target = index;
                        }
                        viewPager.setCurrentItem(target);
                    });
        }
    }

    int noResouce = R.drawable.bg_nodata_normal;
    /**不可见*/
    protected void onInvisible() {
        if (tvScroe == null) return;
        tvScroe.setText("");
        tvAllCount.setText("");
        tvAllCount.setBackgroundResource(noResouce);
        tvCorrect.setText("");
        tvCorrect.setBackgroundResource(noResouce);
        tvWrong.setText("");
        tvWrong.setBackgroundResource(noResouce);
        tvResult.setText("");
        tvResult.setBackgroundResource(noResouce);
        btnFinish.setActivated(false);
        btnRestart.setActivated(false);
        btnWrongBank.setActivated(false);
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) {

    }

    /**
     * 退出答题
     */
    @OnClick(R.id.fsiFinishBtn)
    void onFinish(){

    }

    /**
     * 重新做题
     */
    @OnClick(R.id.fsiRestartBtn)
    void onRestart(){
        activity.finish();
        Bundle bundle = new Bundle();
        if (categoryBean == null || subjectBean == null){
            if (cObj == null || sObj == null){
                ToastUtils.showBlackCenterToast(mContext,mContext.getString(R.string.tipsGetDataFailed));
            }else {
                bundle.putParcelable(CATEGORY_POINTER,cObj);
                bundle.putParcelable(SUBJECT_POINTER,sObj);
            }
        }else {
            bundle.putParcelable(CATEGORY_BEAN,categoryBean);
            bundle.putParcelable(SUBJECT_BEAN,subjectBean);
        }
//                activity.finish();
        ExaminationActivity.show(mContext,bundle);
    }

    @OnClick(R.id.fsiWrongBankBtn)
    void onWrongBank(){
        //前往错题本
        activity.finish();
        switchHomeVp(2);
    }

    /**
     * 计算百分比
     *
     * @return 返回比例
     */
    private String getPer(float now, float total) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(now / total * 100);
    }
}
