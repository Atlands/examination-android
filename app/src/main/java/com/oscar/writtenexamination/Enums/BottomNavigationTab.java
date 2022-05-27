package com.oscar.writtenexamination.Enums;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.blankj.utilcode.util.ColorUtils;
import com.oscar.writtenexamination.R;

public enum BottomNavigationTab {

    HOME(R.drawable.ic_home_gray, R.drawable.ic_home_normal, R.string.tabHome, ColorUtils.getColor(R.color.colorBF), ColorUtils.getColor(R.color.colorMainTwo),false),
    RECORD(R.drawable.ic_record_gray, R.drawable.ic_record_normal, R.string.tabRecord, ColorUtils.getColor(R.color.colorBF), ColorUtils.getColor(R.color.colorMainTwo),false),
    COLLECTION(R.drawable.ic_wrongbank_gray, R.drawable.ic_wrongbank_normal, R.string.tabWrongBank, ColorUtils.getColor(R.color.colorBF), ColorUtils.getColor(R.color.colorMainTwo) ,false),
    MINE(R.drawable.ic_mine_gray, R.drawable.ic_mine_normal, R.string.tabMine, ColorUtils.getColor(R.color.colorBF), ColorUtils.getColor(R.color.colorMainTwo),false);

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

    BottomNavigationTab(@DrawableRes int unselectedResId,
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
        for (BottomNavigationTab current : BottomNavigationTab.values()) {
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
