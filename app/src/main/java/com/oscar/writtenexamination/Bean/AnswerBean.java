package com.oscar.writtenexamination.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.UUID;

public class AnswerBean implements Parcelable {
    private String id;
    private String optionId;
    private String optionText;
    private String optionImageUrl;
    private boolean optionShowImage;
    private boolean optionIsCorrect;

    public AnswerBean(Parcel in) {
        id = in.readString();
        optionId = in.readString();
        optionText = in.readString();
        optionImageUrl = in.readString();

        //读取的String转为布尔类型
        optionShowImage = Boolean.parseBoolean(in.readString());
        optionIsCorrect = Boolean.parseBoolean(in.readString());
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(optionId);
        out.writeString(optionText);
        out.writeString(optionImageUrl);

        out.writeString(String.valueOf(optionShowImage));
        out.writeString(String.valueOf(optionIsCorrect));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isOptionShowImage() {
        return optionShowImage;
    }

    public void setOptionShowImage(boolean optionShowImage) {
        this.optionShowImage = optionShowImage;
    }

    public String getOptionImageUrl() {
        return optionImageUrl;
    }

    public void setOptionImageUrl(String optionImageUrl) {
        this.optionImageUrl = optionImageUrl;
    }

    public boolean isOptionIsCorrect() {
        return optionIsCorrect;
    }

    public void setOptionIsCorrect(boolean optionIsCorrect) {
        this.optionIsCorrect = optionIsCorrect;
    }

    public static final Creator<AnswerBean> CREATOR = new Creator<AnswerBean>() {
        @Override
        public AnswerBean createFromParcel(Parcel in) {
            return new AnswerBean(in);
        }

        @Override
        public AnswerBean[] newArray(int size) {
            return new AnswerBean[size];
        }
    };

    @Override
    public String toString() {
        setId(String.valueOf(UUID.randomUUID()));
        return "AnswerBean{" +
                "optionId='" + optionId + '\'' +
                ", optionText='" + optionText + '\'' +
                ", optionShowImage=" + optionShowImage +
                ", optionImageUrl=" + optionImageUrl +
                ", optionIsCorrect=" + optionIsCorrect +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }
}
