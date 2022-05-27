package com.oscar.writtenexamination.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class SubjectBean implements Parcelable{

    private String id;
    private String subjectId;
    private String subjectName;
    private String subjectInfo;
    private String subjectGoogleProductId;
    private String subjectProductImgUrl;

    private float subjectGoogleOriginalPrice;
    private float subjectGoogleSpecialPrice;

    private int singleCount;
    private int passCount;
    private int showAdCount;

    private boolean isBuy;
    private boolean subjectShowProductImg;

    public SubjectBean(Parcel in) {
        id = in.readString();
        subjectId = in.readString();
        subjectName = in.readString();
        subjectInfo = in.readString();
        subjectGoogleProductId = in.readString();
        subjectProductImgUrl = in.readString();

        subjectGoogleOriginalPrice = in.readFloat();
        subjectGoogleSpecialPrice =  in.readFloat();

        singleCount = in.readInt();
        passCount = in.readInt();
        showAdCount = in.readInt();

        //读取的String转为布尔类型
        isBuy = Boolean.parseBoolean(in.readString());
        subjectShowProductImg = Boolean.parseBoolean(in.readString());
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(subjectId);
        out.writeString(subjectName);
        out.writeString(subjectInfo);
        out.writeString(subjectGoogleProductId);
        out.writeString(subjectProductImgUrl);

        out.writeFloat(subjectGoogleOriginalPrice);
        out.writeFloat(subjectGoogleSpecialPrice);

        out.writeInt(singleCount);
        out.writeInt(passCount);
        out.writeInt(showAdCount);

        out.writeString(String.valueOf(isBuy));
        out.writeString(String.valueOf(subjectShowProductImg));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectInfo() {
        return subjectInfo;
    }

    public void setSubjectInfo(String subjectInfo) {
        this.subjectInfo = subjectInfo;
    }

    public boolean isSubjectShowProductImg() {
        return subjectShowProductImg;
    }

    public void setSubjectShowProductImg(boolean subjectShowProductImg) {
        this.subjectShowProductImg = subjectShowProductImg;
    }

    public int getSingleCount() {
        return singleCount;
    }

    public void setSingleCount(int singleCount) {
        this.singleCount = singleCount;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getShowAdCount() {
        return showAdCount;
    }

    public void setShowAdCount(int showAdCount) {
        this.showAdCount = showAdCount;
    }

    public String getSubjectGoogleProductId() {
        return subjectGoogleProductId;
    }

    public void setSubjectGoogleProductId(String subjectGoogleProductId) {
        this.subjectGoogleProductId = subjectGoogleProductId;
    }

    public float getSubjectGoogleOriginalPrice() {
        return subjectGoogleOriginalPrice;
    }

    public void setSubjectGoogleOriginalPrice(float subjectGoogleOriginalPrice) {
        this.subjectGoogleOriginalPrice = subjectGoogleOriginalPrice;
    }

    public float getSubjectGoogleSpecialPrice() {
        return subjectGoogleSpecialPrice;
    }

    public void setSubjectGoogleSpecialPrice(float subjectGoogleSpecialPrice) {
        this.subjectGoogleSpecialPrice = subjectGoogleSpecialPrice;
    }

    public String getSubjectProductImgUrl() {
        return subjectProductImgUrl;
    }

    public void setSubjectProductImgUrl(String subjectProductImgUrl) {
        this.subjectProductImgUrl = subjectProductImgUrl;
    }

    public static final Creator<SubjectBean> CREATOR = new Creator<SubjectBean>() {
        @Override
        public SubjectBean createFromParcel(Parcel in) {
            return new SubjectBean(in);
        }

        @Override
        public SubjectBean[] newArray(int size) {
            return new SubjectBean[size];
        }
    };


    @Override
    public String toString() {
        id = String.valueOf(UUID.randomUUID());
        return "SubjectBean{" +
                "subjectId='" + subjectId + '\'' +
                ", isBuy=" + isBuy +
                ", subjectName='" + subjectName + '\'' +
                ", subjectInfo='" + subjectInfo + '\'' +
                ", subjectShowProductImg=" + subjectShowProductImg +
                ", singleCount=" + singleCount +
                ", passCount=" + passCount +
                ", showAdCount=" + showAdCount +
                ", subjectGoogleProductId='" + subjectGoogleProductId + '\'' +
                ", subjectGoogleOriginalPrice=" + subjectGoogleOriginalPrice +
                ", subjectGoogleSpecialPrice=" + subjectGoogleSpecialPrice +
                ", subjectProductImgUrl=" + subjectProductImgUrl +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
