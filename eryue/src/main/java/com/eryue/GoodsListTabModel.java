package com.eryue;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by enjoy on 2018/2/17.
 */

public class GoodsListTabModel implements Parcelable{

    private String name;

    private String key;

    public GoodsListTabModel(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public GoodsListTabModel(Parcel in) {
        name = in.readString();
        key = in.readString();
    }

    public static final Creator<GoodsListTabModel> CREATOR = new Creator<GoodsListTabModel>() {
        @Override
        public GoodsListTabModel createFromParcel(Parcel in) {
            return new GoodsListTabModel(in);
        }

        @Override
        public GoodsListTabModel[] newArray(int size) {
            return new GoodsListTabModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }
}
