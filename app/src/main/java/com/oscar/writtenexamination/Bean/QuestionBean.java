package com.oscar.writtenexamination.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.UUID;

public class QuestionBean implements Parcelable {

    private String id;
    private String questionId;
    private String questionText;
    private String questionCorrectInfo;
    private boolean questionShowImg;
    private boolean questionShowCorrectInfo;
    private boolean questionShowCorrectImg;
    private String questionImageUrl;
    private String questionCorrectImgUrl;
    private List<AnswerBean> questionOptions;

    public QuestionBean(Parcel in) {
        id = in.readString();
        questionId = in.readString();
        questionText = in.readString();
        questionCorrectInfo = in.readString();
        questionImageUrl = in.readString();
        questionCorrectImgUrl = in.readString();

        //读取的String转为布尔类型
        questionShowImg = Boolean.parseBoolean(in.readString());
        questionShowCorrectInfo = Boolean.parseBoolean(in.readString());
        questionShowCorrectImg = Boolean.parseBoolean(in.readString());

        //读取实现了Parcelable接口的对象List
        questionOptions = in.readArrayList(AnswerBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(questionId);
        out.writeString(questionText);
        out.writeString(questionCorrectInfo);
        out.writeString(questionImageUrl);
        out.writeString(questionCorrectImgUrl);

        out.writeString(String.valueOf(questionShowImg));
        out.writeString(String.valueOf(questionShowCorrectInfo));
        out.writeString(String.valueOf(questionShowCorrectImg));
        out.writeList(questionOptions);
    }

    //固定写法, 只用修改Creator的泛型
    public static final Creator<QuestionBean> CREATOR = new Creator<QuestionBean>() {
        @Override
        public QuestionBean createFromParcel(Parcel in) {
            return new QuestionBean(in);
        }

        @Override
        public QuestionBean[] newArray(int size) {
            return new QuestionBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public boolean isQuestionShowImg() {
        return questionShowImg;
    }

    public void setQuestionShowImg(boolean questionShowImg) {
        this.questionShowImg = questionShowImg;
    }

    public String getQuestionImageUrl() {
        return questionImageUrl;
    }

    public void setQuestionImageUrl(String questionImageUrl) {
        this.questionImageUrl = questionImageUrl;
    }

    public String getQuestionCorrectInfo() {
        return questionCorrectInfo;
    }

    public void setQuestionCorrectInfo(String questionCorrectInfo) {
        this.questionCorrectInfo = questionCorrectInfo;
    }

    public boolean isQuestionShowCorrectInfo() {
        return questionShowCorrectInfo;
    }

    public void setQuestionShowCorrectInfo(boolean questionShowCorrectInfo) {
        this.questionShowCorrectInfo = questionShowCorrectInfo;
    }

    public String getQuestionCorrectImgUrl() {
        return questionCorrectImgUrl;
    }

    public void setQuestionCorrectImgUrl(String questionCorrectImgUrl) {
        this.questionCorrectImgUrl = questionCorrectImgUrl;
    }

    public boolean isQuestionShowCorrectImg() {
        return questionShowCorrectImg;
    }

    public void setQuestionShowCorrectImg(boolean questionShowCorrectImg) {
        this.questionShowCorrectImg = questionShowCorrectImg;
    }

    public List<AnswerBean> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<AnswerBean> questionOptions) {
        this.questionOptions = questionOptions;
    }


    @Override
    public String toString() {
        setId(String.valueOf(UUID.randomUUID()));
        return "QuestionBean{" +
                "questionId='" + questionId + '\'' +
                ", questionText='" + questionText + '\'' +
                ", questionShowImg=" + questionShowImg +
                ", questionImageUrl=" + questionImageUrl +
                ", questionCorrectInfo='" + questionCorrectInfo + '\'' +
                ", questionShowCorrectInfo=" + questionShowCorrectInfo +
                ", questionCorrectImgUrl=" + questionCorrectImgUrl +
                ", questionShowCorrectImg=" + questionShowCorrectImg +
                ", questionOptions=" + questionOptions +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
