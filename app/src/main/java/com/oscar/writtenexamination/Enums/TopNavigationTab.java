package com.oscar.writtenexamination.Enums;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.blankj.utilcode.util.ColorUtils;
import com.oscar.writtenexamination.R;


public enum TopNavigationTab {

    LOGIN(R.drawable.bg_bottom_view_unselect, R.drawable.bg_bottom_view_selected, R.string.buttonLogin, ColorUtils.getColor(R.color.colorBlack95), ColorUtils.getColor(R.color.colorMainOne),false),
    SIGNUP(R.drawable.bg_bottom_view_unselect, R.drawable.bg_bottom_view_selected, R.string.buttonSignup, ColorUtils.getColor(R.color.colorBlack95), ColorUtils.getColor(R.color.colorMainOne),false);

    public static final float SPECIAL_ITEM_WIDTH_RATIO = 1.0f;

    @DrawableRes
    private int selectedResId;
    @DrawableRes
    private int unselectedResId;
    @StringRes
    private int titleResId;
    @ColorInt
    private int unselectedColorId;
    @ColorInt
    private int selectedColorId;
    private boolean isSpecialItem;

    TopNavigationTab(@DrawableRes int unselectedResId,
                     @DrawableRes int selectedResId,
                     @StringRes int titleResId,
                     @ColorInt int unselectedColorId,
                     @ColorInt int selectedColorId,
                     boolean isSpecialItem) {
        this.unselectedResId = unselectedResId;
        this.selectedResId = selectedResId;
        this.titleResId = titleResId;
        this.unselectedColorId = unselectedColorId;
        this.selectedColorId = selectedColorId;
        this.isSpecialItem = isSpecialItem;
    }

    public static int getSpecialItemCount() {
        int counter = 0;
        for (TopNavigationTab current : TopNavigationTab.values()) {
            if (current.isSpecialItem()) {
                counter++;
            }
        }

        return counter;
    }

    public boolean isSpecialItem() {
        return isSpecialItem;
    }

    public int getUnselectedResId() {
        return unselectedResId;
    }

    public int getSelectedResId() {
        return selectedResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getUnselectedColorId() {
        return unselectedColorId;
    }

    public int getSelectedColorId() {
        return selectedColorId;
    }

}
