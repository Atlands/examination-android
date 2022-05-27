package com.oscar.writtenexamination.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.blankj.utilcode.util.ColorUtils;
import com.oscar.writtenexamination.R;

import java.util.Objects;

import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.correctList;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.currentIndex;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.questionBeanList;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.totalPage;
import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.wrongList;
import static com.oscar.writtenexamination.Base.Configurations.inflater;
import static com.oscar.writtenexamination.Base.Configurations.setText;

public class QuestionBottomFragment extends BottomSheetDialogFragment {

    //监听器
    private BottomSheetDialog dialog ;
    private OnSelectedListener mListener;
    //适配器
    PageAdapter adapter;

    public QuestionBottomFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 返回一个复写的
        dialog = new BottomSheetDialog(Objects.requireNonNull(getContext()));
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_question_bottom, container, true);
        TextView tvCNum = root.findViewById(R.id.fqbCorrectNumTv);
        TextView tvWNum = root.findViewById(R.id.fqbWrongNumTv);
        TextView tvCurrent = root.findViewById(R.id.fqbCurrentIndexTv);
        setText(tvCNum, correctList.size() + "");
        setText(tvWNum, wrongList.size() + "");
        setText(tvCurrent,(currentIndex + 1) + " / " + totalPage);

        GridView gridView = root.findViewById(R.id.fqbGridview);
        int size = questionBeanList.size();
        if (size > 0){
            if (size > 6){
                if(size % 5 > 2){
                    gridView.setNumColumns(5);
                }else {
                    gridView.setNumColumns(4);
                }
            }else {
                gridView.setNumColumns(4);
            }
        }
        adapter = new PageAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            mListener.onSelectedQuestion(i);
            if (dialog != null){
                dialog.dismiss();
            }

        });
        return root;
    }

    /**
     * 设置事件监听，并返回自己
     *
     * @param listener OnSelectedListener
     * @return GalleryFragment
     */
    public QuestionBottomFragment setListener(OnSelectedListener listener) {
        mListener = listener;
        return this;
    }

    static class PageAdapter extends BaseAdapter{

        private static class ViewHolder{
            TextView tvNum;
            View tabView;
        }

        @Override
        public int getCount() {
            return questionBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            return questionBeanList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null){
                view = inflater.inflate(R.layout.item_quesion_bank,null);
                holder = new ViewHolder();
                holder.tvNum = view.findViewById(R.id.iqbNumTv);
                holder.tabView = view.findViewById(R.id.iqbNumTabView);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }
            setText(holder.tvNum,(i + 1) + "");
            boolean isSet = false;
            if (wrongList.contains(i)){
                isSet = true;
                holder.tabView.setBackgroundColor(ColorUtils.getColor(R.color.colorRedNormal));
            }
            if (!isSet){
                if (correctList.contains(i)){
                    isSet = true;
                    holder.tabView.setBackgroundColor(ColorUtils.getColor(R.color.colorMainTwo));
                }
            }
            if(i == currentIndex){
                isSet = true;
                holder.tabView.setBackgroundColor(ColorUtils.getColor(R.color.color78));
            }
            if (!isSet){
                holder.tabView.setBackgroundColor(ColorUtils.getColor(R.color.colorTranslate));
            }
            return view;
        }
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener {
        void onSelectedQuestion(int index);
    }

}
