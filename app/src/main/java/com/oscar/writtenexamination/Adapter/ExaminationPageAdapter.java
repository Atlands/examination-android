package com.oscar.writtenexamination.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oscar.writtenexamination.Fragment.Examination.QuestionItemFragment;
import com.oscar.writtenexamination.Fragment.Examination.ScroesItemFragment;

import java.util.ArrayList;
import java.util.List;

import static com.oscar.writtenexamination.Activity.Examination.ExaminationActivity.questionBeanList;

public class ExaminationPageAdapter extends FragmentStatePagerAdapter {

    public List<Integer> indexs = new ArrayList<>();
    public List<QuestionItemFragment> fragments = new ArrayList<>();


    public ExaminationPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == questionBeanList.size()){
            return new ScroesItemFragment();
        }else {
            if (indexs.contains(i)){
                return fragments.get(i);
            }else {
                QuestionItemFragment fragment = new QuestionItemFragment(i);
                fragments.add(fragment);
                indexs.add(i);
                return new QuestionItemFragment(i);
            }
        }
    }

    @Override
    public int getCount() {
        return questionBeanList.size() + 1;
    }
}
