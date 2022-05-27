package com.oscar.writtenexamination.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class PurchasedHistoryBean implements Parcelable {

    private String id;
    private String objectId;
    private String name;
    private float price;
    private String Date;

    public PurchasedHistoryBean(Parcel in) {
        id = in.readString();
        objectId = in.readString();
        name = in.readString();
        Date = in.readString();

        price = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(objectId);
        out.writeString(name);
        out.writeString(Date);

        out.writeFloat(price);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public static final Creator<PurchasedHistoryBean> CREATOR = new Creator<PurchasedHistoryBean>() {
        @Override
        public PurchasedHistoryBean createFromParcel(Parcel in) {
            return new PurchasedHistoryBean(in);
        }

        @Override
        public PurchasedHistoryBean[] newArray(int size) {
            return new PurchasedHistoryBean[size];
        }
    };


    @Override
    public String toString() {
        id = String.valueOf(UUID.randomUUID());
        return "PurchasedHistoryBean{" +
                "objectId='" + objectId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", Date='" + Date + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
