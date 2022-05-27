package com.oscar.writtenexamination.Fragment.Examination;

import android.annotation.SuppressLint;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.oscar.writtenexamination.Adapter.RecyclerAdapter;
import com.oscar.writtenexamination.Base.Fragment;
import com.oscar.writtenexamination.Bean.AnswerBean;
import com.oscar.writtenexamination.Bean.QuestionBean;
import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.DimensionUtils;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.oscar.writtenexamination.Widget.TagTextView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.btnBottom;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.examinationType;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.questionBeanList;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.sObj;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.setCorrect;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.setWrong;

import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.subjectBean;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.viewPager;
import static com.oscar.writtenexamination.Base.Configurations.EXAMINATION_STATUS_FOR_PURCHASED;
import static com.oscar.writtenexamination.Base.Configurations.addWrongBank;
import static com.oscar.writtenexamination.Base.Configurations.getParseImage;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.removeWrongBank;
import static com.oscar.writtenexamination.Base.Configurations.setText;
import static com.oscar.writtenexamination.Base.Configurations.upperCharacters;

@SuppressLint("ValidFragment")
public class QuestionItemFragment extends Fragment {

    //基本
    @BindView(R.id.fqiScrollview)
    NestedScrollView scrollView;
    @BindView(R.id.fqiMainLayout)
    LinearLayout llMain;
    @BindView(R.id.fqiQuesTv)
    TagTextView tvQuestion;
    @BindView(R.id.fqiPBLayout)
    RelativeLayout llPB;
    @BindView(R.id.fqiPB)
    ProgressBar progressBar;
    @BindView(R.id.fqiQuesImg)
    ImageView imgQuestion;
    @BindView(R.id.fqiRy)
    RecyclerView ry;
    @BindView(R.id.fqiViewOne)
    View viewOne;

    //解析
    @BindView(R.id.fqiCorAnswerLayout)
    LinearLayout llCorAnswer;
    @BindView(R.id.fqiCorAnswerNumTv)
    TextView tvCorNum;

    //底部
    @BindView(R.id.fqiBottomLayout)
    RelativeLayout llBottom;
    @BindView(R.id.fqiSubmitBtn)
    TextView btnSubmit;

    //失败
    @BindView(R.id.fqiFailedLayout)
    LinearLayout llFailed;
    @BindView(R.id.fqiRegetBtn)
    TextView btnReget;

    //参数
    private int choiceType = 0;
    String[] chooseString = new String[]{"單選", "多選"};
    //数据
    private int status = 0; //0正常，1正确，2错误
    private int position = 0;
    private QuestionBean questionBean;
    private List<AnswerBean> answerBeanList = new ArrayList<>();
    private List<AnswerBean> correctList = new ArrayList<>();
    private List<AnswerBean> choices = new ArrayList<>();

    //适配器
    private RecyclerAdapter<AnswerBean> mAdapter;

    public QuestionItemFragment(int index) {
        if (index < questionBeanList.size()) {
            position = index;
            questionBean = questionBeanList.get(index);
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_question_item;
    }

    @Override
    protected void initData() {
        if (questionBean == null){

        }else {
            boolean isShowImg = questionBean.isQuestionShowImg();
            viewOne.setVisibility(isShowImg ? View.VISIBLE : View.GONE);
            llPB.setVisibility(isShowImg ? View.VISIBLE : View.GONE);
            if (isShowImg) {
                String url = questionBean.getQuestionImageUrl();
                boolean isNullFile = url == null;
                llPB.setVisibility(isNullFile ? View.GONE : View.VISIBLE);
                if (!isNullFile) {
                    llPB.setBackground(null);
                    progressBar.setVisibility(View.GONE);
                    getParseImage(imgQuestion, url);
                }
            }
            if (llCorAnswer.getVisibility() == View.VISIBLE){
                scrollToCorrectAnswer();
            }
            getAnswer();
        }
    }

    @Override
    protected void setNetConnect(boolean isNetConnect) {

    }

    /**
     * 获取答案
     */
    private void getAnswer() {
        answerBeanList = questionBean.getQuestionOptions();
        boolean isEmpty = judgeEmptyList(answerBeanList);
        if (isEmpty) {
            getAnswerFailed();
        } else {
            //获取正确答案
            getCorrectAnswer();
        }
    }

    /**
     * 获取选项失败
     */
    private void getAnswerFailed() {
        scrollView.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
        llFailed.setVisibility(View.VISIBLE);
    }

    /**
     * 重新获取
     */
    @OnClick(R.id.fqiRegetBtn)
    void onReget(){
        scrollView.setVisibility(View.VISIBLE);
        llFailed.setVisibility(View.GONE);
        getAnswer();
    }

    /**
     * 获取正确答案
     */
    private void getCorrectAnswer() {
        correctList.clear();
        for (AnswerBean answerBean : answerBeanList) {
            if (answerBean.isOptionIsCorrect() && !correctList.contains(answerBean)) {
                //判断是否正确答案，再判断是否重复
                correctList.add(answerBean);
            }
        }
        if (!judgeEmptyList(correctList)){
            //设置单选多选
            setChoiceType();
        }
    }

    /**
     * 设置单选多选
     */
    @SuppressLint("SetTextI18n")
    private void setChoiceType(){
        if (choiceType == 0) choiceType = judgeChoiceType();
        if (choiceType > 0){
            //设置单选多选标题和问题
            List<String> tags = new ArrayList<>();
            tags.add(choiceType == 1 ? chooseString[0] : chooseString[1]);
            String question = questionBean.getQuestionText();
            tvQuestion.setContentAndTag(question, tags);
            tvQuestion.setBackground(null);
//            if (ExaminationActivity.chooseList.size() > position){
//                choices = ExaminationActivity.chooseList.get(position);
//            }
            //设置正确解析
            setText(tvCorNum,getCorrectNum());
            if (choiceType == 1){
                //单选
                status = judgeSingleAnswer();
            }else {
                //多选
                status = judgeMoreAnswer();
            }
            //显示解析布局
            showCorAnswerLayout();
            //显示底部按钮
            showBottomLayout();
            //初始化答案
            initAnswer();
        }else {
            getAnswerFailed();
        }
    }

    /**
     * 显示答案解析布局
     */
    private void showCorAnswerLayout(){
        llCorAnswer.setVisibility(View.GONE);
        switch (status){
            case 0:
                //未选
                break;
            case 1:
                //正确
                break;
            case 2:
                //错误
                llCorAnswer.setVisibility(View.VISIBLE);
                scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);//滑到底部
//                scrollToCorrectAnswer();
                break;
        }
    }

    /**
     * 显示提交底部布局
     */
    private void showBottomLayout(){
        llBottom.setVisibility(View.GONE);
        btnSubmit.setActivated(false);
        if (position == questionBeanList.size() -1){
            btnSubmit.setText(getString(R.string.buttonGotoResult));
        }else {
            btnSubmit.setText(getString(R.string.buttonNext));
        }
        switch (status){
            case 0:
                //未选
                btnSubmit.setActivated(true);
                btnSubmit.setText(getString(R.string.buttonSubmit));
                break;
            case 1:
                //正确
                break;
            case 2:
                //错误
                llBottom.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showBottomLayoutByChoice(){
        boolean isActivited = !judgeEmptyList(choices);
        llBottom.setVisibility(isActivited ? View.VISIBLE : View.GONE);
        btnSubmit.setActivated(isActivited);
    }

    /**
     * 判断单选多选
     * @return 0失败，1单选，2多选
     */
    private int judgeChoiceType(){
        int size = correctList.size();
        if (size> 0){
            if (size == 1){
                return 1;
            }else {
                return 2;
            }
        }else {
            return 0;
        }
    }

    /**
     * 获取正确答案编号
     * @return 对应文本
     */
    private String getCorrectNum(){
        StringBuilder string = new StringBuilder("正確答案是：");
        if (choiceType == 1){
            //单选
            int num = 0;
            for (AnswerBean answerBean : answerBeanList){
                if (answerBean.getOptionId().equals(correctList.get(0).getOptionId())){ break; }
                num++;
            }
            string.append(upperCharacters[num]);
        }
        if (choiceType == 2){
            //多选
            for (AnswerBean correctBean : correctList){
                int num = 0;
                for (AnswerBean answerBean : answerBeanList){
                    if (correctBean.getOptionId().equals(answerBean.getOptionId())){
                        string.append(upperCharacters[num]);
                        break;
                    }
                    num++;
                }
            }
        }
        return string.toString();
    }

    /**
     * 判断单选
     * @return 0未选，1正确，2错误
     */
    private int judgeSingleAnswer(){
        if (judgeEmptyList(choices)){
            return 0;
        }else {
            int type = 2;
            if (choices.size() == 1 && choices.get(0) != null && choices.get(0).isOptionIsCorrect()){
                type = 1;
            }
            return type;
        }
    }

    /**
     * 多选判断
     * @return 0未选，1正确，2错误
     */
    private int judgeMoreAnswer(){
        if (judgeEmptyList(choices)){
            return 0;
        }else {
            int type = 2;
            int size = choices.size();
            if (size == correctList.size()){
                int num = 0;
                for (AnswerBean choiceBean : choices){
                    if (choiceBean.isOptionIsCorrect()) num++;
                }
                if (num == size) type = 1;
            }
            return type;
        }
    }


    /**
     * 初始化答案
     */
    private void initAnswer() {
        if (mAdapter == null){
            //布局参数
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ry.setLayoutManager(layoutManager);
            ry.setNestedScrollingEnabled(false);
            ry.setHasFixedSize(true);
            mAdapter = new RecyclerAdapter<AnswerBean>(answerBeanList, null) {
                @Override
                protected int getItemViewType(int position, AnswerBean answerBean) {
                    return R.layout.item_answer_normal;
                }

                @Override
                protected ViewHolder<AnswerBean> onCreateViewHolder(View root, int viewType) {
                    return new QuestionItemFragment.ViewHolder(root);
                }
            };
            ry.setAdapter(mAdapter);
            //设置监听器
            mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<AnswerBean>() {
                @Override
                public void onItemClick(RecyclerAdapter.ViewHolder holder, AnswerBean choice) {
                    if (status == 0){
                        int choiceType = judgeChoiceType();
                        if (choiceType > 0){
                            if (choiceType == 1){
                                //单选
                                choices.clear();
                                choices.add(choice);
                                status = judgeSingleAnswer();
                                mAdapter.notifyDataSetChanged();
                                if (examinationType == EXAMINATION_STATUS_FOR_PURCHASED && status != 0){
                                    String questionId = questionBean.getQuestionId();
                                    if (questionBean != null && !TextUtils.isEmpty(questionId)){
                                        if (status == 1){
//                                            if (subjectBean == null){
//                                                if (sObj != null){
//                                                    removeWrongBank(sObj,questionId);
//                                                }
//                                            }else {
//                                                removeWrongBank(subjectBean,questionId);
//                                            }
                                        }else {
                                            if (subjectBean == null){
                                                if (sObj != null){
                                                    addWrongBank(sObj,questionId);
                                                }
                                            }else {
                                                addWrongBank(subjectBean,questionId);
                                            }
                                        }
                                    }
                                }
                                if (status == 1){
                                    ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsAnswerCorrect));
                                    setCorrect(position);
                                    viewPager.setCurrentItem(position + 1);
                                }
                                if (status ==2){
                                    ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsAnswerWrong));
                                    setWrong(position);
                                }
                                //显示解析布局
                                showCorAnswerLayout();
                                //显示底部按钮
                                showBottomLayout();
                            }else {
                                //多选
                                if (choices.contains(choice)){
                                    choices.remove(choice);    //移除
                                }else {
                                    choices.add(choice); //插入
                                }
                                mAdapter.notifyDataSetChanged();
                                //根据选中设置底部按钮
                                showBottomLayoutByChoice();
                            }
                        }else {
                            ToastUtils.showShortToast(mContext,getString(R.string.tipsGetAnswerFailed));
                        }
                    }else {
                        ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsAlreadyAnswerPlzNotAgain));
                    }
                }
            });
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.fqiSubmitBtn)
    void onSubmit(){
        if (btnSubmit.isActivated()){
            //提交功能（多选）
            status = judgeMoreAnswer();
            mAdapter.notifyDataSetChanged();
            if (examinationType == EXAMINATION_STATUS_FOR_PURCHASED && status != 0){
                String questionId = questionBean.getQuestionId();
                if (questionBean != null && !TextUtils.isEmpty(questionId) && subjectBean != null){
                    if (status == 1){
//                        removeWrongBank(subjectBean,questionId);
                    }else {
                        addWrongBank(subjectBean,questionId);
                    }
                }
            }
            if (status == 1){
                ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsAnswerCorrect));
                setCorrect(position);
                viewPager.setCurrentItem(position + 1);
            }
            if (status == 2){
                ToastUtils.showBlackCenterToast(mContext,getString(R.string.tipsAnswerWrong));
                setWrong(position);
            }
            //显示解析布局
            showCorAnswerLayout();
            //显示底部布局
            showBottomLayout();
        }else {
            //下一步功能
            viewPager.setCurrentItem(position + 1);
        }
    }

    /**
     * 滑动到指定位置
     */
    void scrollToCorrectAnswer(){
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        llCorAnswer.measure(w, h);
        llMain.measure(w,h);
        llBottom.measure(w,h);
        btnBottom.measure(w,h);
        int height = llCorAnswer.getMeasuredHeight();
        int height2 = llMain.getMeasuredHeight();
        int height3 = llBottom.getMeasuredHeight();
        int height4 = btnBottom.getMeasuredHeight();

        ToastUtils.showShortToast(mContext,height2 + "," + height + "," + height3 + "," + height4);
        int distance = height4 + height3 + height2 + height + DimensionUtils.dip2px(mContext,70);
        scrollView.fling(distance);//添加上这句滑动才有效
        scrollView.smoothScrollTo(0,distance);
    }

    /**
     * 科目表ViewHolder
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<AnswerBean> {

        @BindView(R.id.ianNumTv)
        TextView tvNum;
        @BindView(R.id.ianAnswerTv)
        TextView tvAnswer;
        @BindView(R.id.ianPBLayout)
        RelativeLayout llPB;
        @BindView(R.id.ianPB)
        ProgressBar progressBar;
        @BindView(R.id.ianImg)
        ImageView img;

        //0正常，1正确，2错误
        int[] numColors = {R.color.colorBlack95,R.color.colorWhite,R.color.colorWhite,R.color.colorWhite};
        int[] answerColors = {R.color.colorBlack95,R.color.colorMainTwo,R.color.colorRedDark,R.color.colorGreenNormal};
        int[] drawables = {R.drawable.bg_round_blackborder,R.drawable.bg_round_normal,
                R.drawable.bg_round_reddark,R.drawable.bg_round_green};

        ViewHolder(View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onBind(AnswerBean answerBean) {
            int position = getAdapterPosition();
            if (choiceType == 1){
                //单选
                if (status == 0){
                    setNormalItem(position); //正常
                }else {
                    setSingleItem(position,answerBean);
                }
            }else if (choiceType == 2){
                //多选
                if (status == 0){
                    //未提交
                    setChoiceItem(position,answerBean);
                }else {
                    //已提交，即做题正确或错误
                    setMoreItem(position,answerBean);
                }
            }else {
                setNormalItem(position); //默认
            }
            setText(tvAnswer, answerBean.getOptionText());

            boolean isShowImg = answerBean.isOptionShowImage();
            llPB.setVisibility(isShowImg ? View.VISIBLE : View.GONE);
            if (isShowImg) {
                String url = answerBean.getOptionImageUrl();
                boolean isNullFile = url == null;
                if (!isNullFile) {
                    llPB.setBackground(null);
                    progressBar.setVisibility(View.GONE);
                    getParseImage(img,url);
                }
            }
        }

        @Override
        protected void onBind(AnswerBean answerBean, List<Object> payloads) {

        }

        /**
         * 设置正常，正确，错误等效果
         */
        private void setSingleItem(int i,AnswerBean current){
            int type = 0; //默认正常
            if (!judgeEmptyList(choices)){
                //有选中项
                AnswerBean choice = choices.get(0);
                AnswerBean correct = correctList.get(0);
                if (current.getOptionId().equals(choice.getOptionId())) {
                    //当前为选中一项
                    if (current.getOptionId().equals(correct.getOptionId())) {
                        type = 1;  //选中，正确选项
                    } else {
                        type = 2;  //选中，错误选项
                    }
                }else {
                    //当前为非选中一项
                    if (current.getOptionId().equals(correct.getOptionId())){
                        type = 3;//非选中，正确选项
                    }else {
                        type = 0; //非选中，错误选项(正常)
                    }
                }
            }
            if (type == 2){
                tvNum.setText("X");
            }else if (type == 1 || type == 3){
                tvNum.setText("√");
            } else {
                tvNum.setText(upperCharacters[i]);
            }
            tvNum.setTextColor(ColorUtils.getColor(numColors[type]));
            tvNum.setBackgroundResource(drawables[type]);
            tvAnswer.setTextColor(ColorUtils.getColor(answerColors[type]));
        }

        /**
         * 设置选中或未选中
         * @param current 当前项
         */
        private void setChoiceItem(int i,AnswerBean current){
            int type = 0; //默认未选中
            if (!judgeEmptyList(choices)){
                //有选中项
                for (AnswerBean choice :choices){
                    if (current.getOptionId().equals(choice.getOptionId())){
                        type = 1; //当前为选中项
                        break;
                    }
                }

            }
            tvNum.setText(upperCharacters[i]);
            tvNum.setTextColor(ColorUtils.getColor(numColors[type]));
            tvNum.setBackgroundResource(drawables[type]);
            tvAnswer.setTextColor(ColorUtils.getColor(answerColors[type]));
        }

        /**
         * 设置正常，正确，错误等效果
         * 0错误选项未选中，1正确选项选中，2错误选项选中，3正确选项未选中
         */
        private void setMoreItem(int i,AnswerBean current){
            int type = 0; //默认正常
            if (!judgeEmptyList(choices)){
                //有选中项
                boolean isChoice = false;
                for (AnswerBean choice : choices){
                    if (current.getOptionId().equals(choice.getOptionId())){
                           isChoice = true;
                    }
                }
                if (isChoice){
                    //选中项
                    for (AnswerBean correct : correctList){
                        if (current.getOptionId().equals(correct.getOptionId())){
                            type = 1; //选中，正确选项，退出循环
                            break;
                        }else {
                            type = 2; //选中，错误选项，继续循环
                        }
                    }
                } else {
                    //非选中项
                    for (AnswerBean correct : correctList){
                        if (current.getOptionId().equals(correct.getOptionId())){
                            type = 3; //非选中，正确选项，退出循环
                            break;
                        }
                    }
                }
            }
            if (type == 2){
                tvNum.setText("X");
            }else if (type == 1 || type == 3){
                tvNum.setText("√");
            }else {
                tvNum.setText(upperCharacters[i]);
            }
            tvNum.setTextColor(ColorUtils.getColor(numColors[type]));
            tvNum.setBackgroundResource(drawables[type]);
            tvAnswer.setTextColor(ColorUtils.getColor(answerColors[type]));
        }

        /**
         * 正常item
         */
        private void setNormalItem(int i){
            tvNum.setText(upperCharacters[i]);
            tvNum.setTextColor(ColorUtils.getColor(numColors[0]));
            tvNum.setBackgroundResource(drawables[0]);
            tvAnswer.setTextColor(ColorUtils.getColor(answerColors[0]));
        }

    }
}
