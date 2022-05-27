package com.oscar.writtenexamination.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.List;
import java.util.UUID;

public class CategoryBean implements Parcelable {

    private String id;
    private String categoryId;
    private String categoryName;
    private String categoryInfo;
    private String googleProductId;
    private String categoryImgUrl;
    private float googleOriginalPrice;
    private float googleSpecialPrice;
    private int cateCollectNum;
    private boolean isBuy;
    private List<SubjectBean> subjects;

    public CategoryBean(Parcel in) {
        id = in.readString();
        categoryId = in.readString();
        categoryName = in.readString();
        categoryInfo = in.readString();
        googleProductId = in.readString();
        categoryImgUrl = in.readString();

        googleOriginalPrice = in.readFloat();
        googleSpecialPrice = in.readFloat();
        cateCollectNum = in.readInt();
        //读取的String转为布尔类型
        isBuy = Boolean.parseBoolean(in.readString());
        //读取实现了Parcelable接口的对象List
        subjects = in.readArrayList(SubjectBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(categoryId);
        out.writeString(categoryName);
        out.writeString(categoryInfo);
        out.writeString(googleProductId);
        out.writeString(categoryImgUrl);

        out.writeFloat(googleOriginalPrice);
        out.writeFloat(googleSpecialPrice);

        out.writeInt(cateCollectNum);

        out.writeString(String.valueOf(isBuy));
        out.writeList(subjects);
    }

    //固定写法, 只用修改Creator的泛型
    public static final Creator<CategoryBean> CREATOR = new Creator<CategoryBean>() {
        @Override
        public CategoryBean createFromParcel(Parcel in) {
            return new CategoryBean(in);
        }

        @Override
        public CategoryBean[] newArray(int size) {
            return new CategoryBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(String categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public String getGoogleProductId() {
        return googleProductId;
    }

    public void setGoogleProductId(String googleProductId) {
        this.googleProductId = googleProductId;
    }

    public float getGoogleOriginalPrice() {
        return googleOriginalPrice;
    }

    public void setGoogleOriginalPrice(float googleOriginalPrice) {
        this.googleOriginalPrice = googleOriginalPrice;
    }

    public float getGoogleSpecialPrice() {
        return googleSpecialPrice;
    }

    public void setGoogleSpecialPrice(float googleSpecialPrice) {
        this.googleSpecialPrice = googleSpecialPrice;
    }

    public int getCateCollectNum() {
        return cateCollectNum;
    }

    public void setCateCollectNum(int cateCollectNum) {
        this.cateCollectNum = cateCollectNum;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public String getCategoryImgUrl() {
        return categoryImgUrl;
    }

    public void setCategoryImgUrl(String categoryImgUrl) {
        this.categoryImgUrl = categoryImgUrl;
    }

    public List<SubjectBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectBean> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        id = String.valueOf(UUID.randomUUID());
        return "CategoryBean{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryInfo='" + categoryInfo + '\'' +
                ", googleProductId='" + googleProductId + '\'' +
                ", googleOriginalPrice=" + googleOriginalPrice +
                ", googleSpecialPrice=" + googleSpecialPrice +
                ", cateCollectNum=" + cateCollectNum +
                ", isBuy=" + isBuy +
                ", categoryImgUrl=" + categoryImgUrl +
                ", subjects=" + subjects +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
