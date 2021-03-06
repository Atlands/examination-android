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

    //??????
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

    //??????
    @BindView(R.id.fqiCorAnswerLayout)
    LinearLayout llCorAnswer;
    @BindView(R.id.fqiCorAnswerNumTv)
    TextView tvCorNum;

    //??????
    @BindView(R.id.fqiBottomLayout)
    RelativeLayout llBottom;
    @BindView(R.id.fqiSubmitBtn)
    TextView btnSubmit;

    //??????
    @BindView(R.id.fqiFailedLayout)
    LinearLayout llFailed;
    @BindView(R.id.fqiRegetBtn)
    TextView btnReget;

    //??????
    private int choiceType = 0;
    String[] chooseString = new String[]{"??????", "??????"};
    //??????
    private int status = 0; //0?????????1?????????2??????
    private int position = 0;
    private QuestionBean questionBean;
    private List<AnswerBean> answerBeanList = new ArrayList<>();
    private List<AnswerBean> correctList = new ArrayList<>();
    private List<AnswerBean> choices = new ArrayList<>();

    //?????????
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
     * ????????????
     */
    private void getAnswer() {
        answerBeanList = questionBean.getQuestionOptions();
        boolean isEmpty = judgeEmptyList(answerBeanList);
        if (isEmpty) {
            getAnswerFailed();
        } else {
            //??????????????????
            getCorrectAnswer();
        }
    }

    /**
     * ??????????????????
     */
    private void getAnswerFailed() {
        scrollView.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
        llFailed.setVisibility(View.VISIBLE);
    }

    /**
     * ????????????
     */
    @OnClick(R.id.fqiRegetBtn)
    void onReget(){
        scrollView.setVisibility(View.VISIBLE);
        llFailed.setVisibility(View.GONE);
        getAnswer();
    }

    /**
     * ??????????????????
     */
    private void getCorrectAnswer() {
        correctList.clear();
        for (AnswerBean answerBean : answerBeanList) {
            if (answerBean.isOptionIsCorrect() && !correctList.contains(answerBean)) {
                //????????????????????????????????????????????????
                correctList.add(answerBean);
            }
        }
        if (!judgeEmptyList(correctList)){
            //??????????????????
            setChoiceType();
        }
    }

    /**
     * ??????????????????
     */
    @SuppressLint("SetTextI18n")
    private void setChoiceType(){
        if (choiceType == 0) choiceType = judgeChoiceType();
        if (choiceType > 0){
            //?????????????????????????????????
            List<String> tags = new ArrayList<>();
            tags.add(choiceType == 1 ? chooseString[0] : chooseString[1]);
            String question = questionBean.getQuestionText();
            tvQuestion.setContentAndTag(question, tags);
            tvQuestion.setBackground(null);
//            if (ExaminationActivity.chooseList.size() > position){
//                choices = ExaminationActivity.chooseList.get(position);
//            }
            //??????????????????
            setText(tvCorNum,getCorrectNum());
            if (choiceType == 1){
                //??????
                status = judgeSingleAnswer();
            }else {
                //??????
                status = judgeMoreAnswer();
            }
            //??????????????????
            showCorAnswerLayout();
            //??????????????????
            showBottomLayout();
            //???????????????
            initAnswer();
        }else {
            getAnswerFailed();
        }
    }

    /**
     * ????????????????????????
     */
    private void showCorAnswerLayout(){
        llCorAnswer.setVisibility(View.GONE);
        switch (status){
            case 0:
                //??????
                break;
            case 1:
                //??????
                break;
            case 2:
                //??????
                llCorAnswer.setVisibility(View.VISIBLE);
                scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);//????????????
//                scrollToCorrectAnswer();
                break;
        }
    }

    /**
     * ????????????????????????
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
                //??????
                btnSubmit.setActivated(true);
                btnSubmit.setText(getString(R.string.buttonSubmit));
                break;
            case 1:
                //??????
                break;
            case 2:
                //??????
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
     * ??????????????????
     * @return 0?????????1?????????2??????
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
     * ????????????????????????
     * @return ????????????
     */
    private String getCorrectNum(){
        StringBuilder string = new StringBuilder("??????????????????");
        if (choiceType == 1){
            //??????
            int num = 0;
            for (AnswerBean answerBean : answerBeanList){
                if (answerBean.getOptionId().equals(correctList.get(0).getOptionId())){ break; }
                num++;
            }
            string.append(upperCharacters[num]);
        }
        if (choiceType == 2){
            //??????
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
     * ????????????
     * @return 0?????????1?????????2??????
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
     * ????????????
     * @return 0?????????1?????????2??????
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
     * ???????????????
     */
    private void initAnswer() {
        if (mAdapter == null){
            //????????????
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
            //???????????????
            mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<AnswerBean>() {
                @Override
                public void onItemClick(RecyclerAdapter.ViewHolder holder, AnswerBean choice) {
                    if (status == 0){
                        int choiceType = judgeChoiceType();
                        if (choiceType > 0){
                            if (choiceType == 1){
                                //??????
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
                                //??????????????????
                                showCorAnswerLayout();
                                //??????????????????
                                showBottomLayout();
                            }else {
                                //??????
                                if (choices.contains(choice)){
                                    choices.remove(choice);    //??????
                                }else {
                                    choices.add(choice); //??????
                                }
                                mAdapter.notifyDataSetChanged();
                                //??????????????????????????????
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
            //????????????????????????
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
            //??????????????????
            showCorAnswerLayout();
            //??????????????????
            showBottomLayout();
        }else {
            //???????????????
            viewPager.setCurrentItem(position + 1);
        }
    }

    /**
     * ?????????????????????
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
        scrollView.fling(distance);//??????????????????????????????
        scrollView.smoothScrollTo(0,distance);
    }

    /**
     * ?????????ViewHolder
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

        //0?????????1?????????2??????
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
                //??????
                if (status == 0){
                    setNormalItem(position); //??????
                }else {
                    setSingleItem(position,answerBean);
                }
            }else if (choiceType == 2){
                //??????
                if (status == 0){
                    //?????????
                    setChoiceItem(position,answerBean);
                }else {
                    //????????????????????????????????????
                    setMoreItem(position,answerBean);
                }
            }else {
                setNormalItem(position); //??????
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
         * ???????????????????????????????????????
         */
        private void setSingleItem(int i,AnswerBean current){
            int type = 0; //????????????
            if (!judgeEmptyList(choices)){
                //????????????
                AnswerBean choice = choices.get(0);
                AnswerBean correct = correctList.get(0);
                if (current.getOptionId().equals(choice.getOptionId())) {
                    //?????????????????????
                    if (current.getOptionId().equals(correct.getOptionId())) {
                        type = 1;  //?????????????????????
                    } else {
                        type = 2;  //?????????????????????
                    }
                }else {
                    //????????????????????????
                    if (current.getOptionId().equals(correct.getOptionId())){
                        type = 3;//????????????????????????
                    }else {
                        type = 0; //????????????????????????(??????)
                    }
                }
            }
            if (type == 2){
                tvNum.setText("X");
            }else if (type == 1 || type == 3){
                tvNum.setText("???");
            } else {
                tvNum.setText(upperCharacters[i]);
            }
            tvNum.setTextColor(ColorUtils.getColor(numColors[type]));
            tvNum.setBackgroundResource(drawables[type]);
            tvAnswer.setTextColor(ColorUtils.getColor(answerColors[type]));
        }

        /**
         * ????????????????????????
         * @param current ?????????
         */
        private void setChoiceItem(int i,AnswerBean current){
            int type = 0; //???????????????
            if (!judgeEmptyList(choices)){
                //????????????
                for (AnswerBean choice :choices){
                    if (current.getOptionId().equals(choice.getOptionId())){
                        type = 1; //??????????????????
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
         * ???????????????????????????????????????
         * 0????????????????????????1?????????????????????2?????????????????????3?????????????????????
         */
        private void setMoreItem(int i,AnswerBean current){
            int type = 0; //????????????
            if (!judgeEmptyList(choices)){
                //????????????
                boolean isChoice = false;
                for (AnswerBean choice : choices){
                    if (current.getOptionId().equals(choice.getOptionId())){
                           isChoice = true;
                    }
                }
                if (isChoice){
                    //?????????
                    for (AnswerBean correct : correctList){
                        if (current.getOptionId().equals(correct.getOptionId())){
                            type = 1; //????????????????????????????????????
                            break;
                        }else {
                            type = 2; //????????????????????????????????????
                        }
                    }
                } else {
                    //????????????
                    for (AnswerBean correct : correctList){
                        if (current.getOptionId().equals(correct.getOptionId())){
                            type = 3; //???????????????????????????????????????
                            break;
                        }
                    }
                }
            }
            if (type == 2){
                tvNum.setText("X");
            }else if (type == 1 || type == 3){
                tvNum.setText("???");
            }else {
                tvNum.setText(upperCharacters[i]);
            }
            tvNum.setTextColor(ColorUtils.getColor(numColors[type]));
            tvNum.setBackgroundResource(drawables[type]);
            tvAnswer.setTextColor(ColorUtils.getColor(answerColors[type]));
        }

        /**
         * ??????item
         */
        private void setNormalItem(int i){
            tvNum.setText(upperCharacters[i]);
            tvNum.setTextColor(ColorUtils.getColor(numColors[0]));
            tvNum.setBackgroundResource(drawables[0]);
            tvAnswer.setTextColor(ColorUtils.getColor(answerColors[0]));
        }

    }
}
