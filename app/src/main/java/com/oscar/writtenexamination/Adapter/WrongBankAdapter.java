package com.oscar.writtenexamination.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oscar.writtenexamination.R;
import com.oscar.writtenexamination.Utils.ToastUtils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_IMG;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_INFO;
import static com.oscar.writtenexamination.Base.Configurations.CATEGORY_NAME;
import static com.oscar.writtenexamination.Base.Configurations.OBJECT_ID;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_NAME;
import static com.oscar.writtenexamination.Base.Configurations.SUBJECT_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.USER_POINTER;
import static com.oscar.writtenexamination.Base.Configurations.WRONG_BANK_CLASS_NAME;
import static com.oscar.writtenexamination.Base.Configurations.WRONG_BANK_QUESTIONS;
import static com.oscar.writtenexamination.Base.Configurations.getParseImage;
import static com.oscar.writtenexamination.Base.Configurations.judgeEmptyList;
import static com.oscar.writtenexamination.Base.Configurations.judgeObject;
import static com.oscar.writtenexamination.Base.Configurations.setText;

public class WrongBankAdapter extends BaseExpandableListAdapter {

    Context context;
    List<String> cateList;//一级List
    List<List<ParseObject>> subjectList;//二级List

    public WrongBankAdapter(Context context,List<String> groupList, List<List<ParseObject>> childList){
        this.context = context;
        cateList = groupList;
        subjectList = childList;

    }

    /**
     * 返回第一级List长度
     */
    @Override
    public int getGroupCount() {
        return cateList.size();
    }

    /**
     * 返回指定groupPosition的第二级List长度
     * @param i 一级position
     */
    @Override
    public int getChildrenCount(int i) {
        return subjectList.get(i).size();
    }

    /**
     * 返回一级List里的内容
     * @param i 一级position
     */
    @Override
    public String getGroup(int i) {
        return cateList.get(i);
    }

    /**
     * 返回二级List的内容
     * @param i1 一级position
     * @param i2 二级position
     */
    @Override
    public ParseObject getChild(int i1, int i2) {
        return subjectList.get(i1).get(i2);
    }

    /**
     * 返回一级View的id 保证id唯一
     * @param i 一级position
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }

    /**
     * 返回二级View的id 保证id唯一
     * @param i1 一级position
     * @param i2 二级position
     */
    @Override
    public long getChildId(int i1, int i2) {
        return i1 + i2;
    }

    /**
     * 指示在对基础数据进行更改时子ID和组ID是否稳定
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder holder;
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wrong_category, viewGroup,false);
            holder = new GroupHolder(view);
            view.setTag(holder);
        }else {
            holder = (GroupHolder) view.getTag();
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery(CATEGORY_CLASS_NAME);
        query.whereEqualTo(OBJECT_ID,getGroup(i));
        query.getFirstInBackground((object, e) -> {
            if (e == null && !judgeObject(object)){
                getParseImage(holder.imgCate,object,CATEGORY_IMG);
                //基础信息
                setText(holder.tvName,object.getString(CATEGORY_NAME));
                setText(holder.tvInfor,object.getString(CATEGORY_INFO));
            }
        });
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int i1, int i2, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wrong_subject, viewGroup,false);
            holder = new ChildHolder(view);
            view.setTag(holder);
        }else {
            holder = (ChildHolder) view.getTag();
        }
        holder.tvPosition.setText((i2 + 1) + "");
        ParseObject subject = getChild(i1, i2);
        setText(holder.tvName,subject.getString(SUBJECT_NAME));
        ParseQuery<ParseObject> query = ParseQuery.getQuery(WRONG_BANK_CLASS_NAME);
        query.whereEqualTo(USER_POINTER, ParseUser.getCurrentUser());
        query.whereEqualTo(SUBJECT_POINTER,subject);
        query.getFirstInBackground((object, e) -> {
            if (e == null && !judgeObject(object)){
                List<String> questions = object.getList(WRONG_BANK_QUESTIONS);
                boolean isEmpty = judgeEmptyList(questions);
                if (!isEmpty){
                    holder.tvNum.setVisibility(View.VISIBLE);
                    setText(holder.tvNum,"" + questions.size());
                }
            }
        });
        holder.btnCheck.setOnClickListener(view1 -> {
            ToastUtils.showBlackCenterToast(context,"未实现");
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class GroupHolder{
        private ImageView imgCate;
        private TextView tvName;
        private TextView tvInfor;

        GroupHolder(View view){
            imgCate = view.findViewById(R.id.iwcCateImg);
            tvName = view.findViewById(R.id.iwcCataNameTv);
            tvInfor = view.findViewById(R.id.iwcCateInforTv);
        }
    }

    class ChildHolder{
        private TextView tvPosition;
        private TextView tvName;
        private TextView tvNum;
        private TextView btnCheck;

        ChildHolder(View view){
            tvPosition = view.findViewById(R.id.iwsPositionTv);
            tvName = view.findViewById(R.id.iwsNameTv);
            tvNum = view.findViewById(R.id.iwsNumTv);
            btnCheck = view.findViewById(R.id.iwsCheckBtn);
        }
    }
}
