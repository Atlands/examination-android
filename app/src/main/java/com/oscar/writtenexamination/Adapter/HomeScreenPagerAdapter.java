package com.oscar.writtenexamination.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oscar.writtenexamination.Fragment.Home.WrongBankFragment;
import com.oscar.writtenexamination.Fragment.Home.HomeFragment;
import com.oscar.writtenexamination.Fragment.Home.MineFragment;
import com.oscar.writtenexamination.Fragment.Home.RecordFragment;

public class HomeScreenPagerAdapter extends FragmentStatePagerAdapter {

    public final static int FRAGMENTS_COUNT = 4;

    public final static int HOME_FRAGMENT_POSITION = 0;
    public final static int RECORD_FRAGMENT_POSITION = 1;
    public final static int COLLECT_FRAGMENT_POSITION = 2;
    public final static int MINE_FRAGMENT_POSITION = 3;


    public HomeScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HOME_FRAGMENT_POSITION:
                return new HomeFragment();
            case RECORD_FRAGMENT_POSITION:
                return new RecordFragment();
            case COLLECT_FRAGMENT_POSITION:
                return new WrongBankFragment();
            case MINE_FRAGMENT_POSITION:
                return new MineFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }
}
