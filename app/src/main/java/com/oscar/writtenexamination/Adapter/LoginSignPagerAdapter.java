package com.oscar.writtenexamination.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.oscar.writtenexamination.Fragment.LoginSign.LoginFragment;
import com.oscar.writtenexamination.Fragment.LoginSign.SignFragment;

public class LoginSignPagerAdapter extends FragmentStatePagerAdapter {

    public final static int FRAGMENTS_COUNT = 2;

    public final static int LOGIN_FRAGMENT_POSITION = 0;
    public final static int SIGN_FRAGMENT_POSITION = 1;


    public LoginSignPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case LOGIN_FRAGMENT_POSITION:
                return new LoginFragment();
            case SIGN_FRAGMENT_POSITION:
                return new SignFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return FRAGMENTS_COUNT;
    }
}
