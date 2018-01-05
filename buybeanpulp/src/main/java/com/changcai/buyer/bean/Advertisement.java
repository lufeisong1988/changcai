package com.changcai.buyer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.changcai.buyer.IKeepFromProguard;

/**
 * Created by liuxingwei on 2017/7/19.
 */

public  class Advertisement implements Parcelable,IKeepFromProguard{


    private String imageUrl;
    private String description;
    private String butText;
    private String butUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getButText() {
        return butText;
    }

    public void setButText(String butText) {
        this.butText = butText;
    }

    public String getButUrl() {
        return butUrl;
    }

    public void setButUrl(String butUrl) {
        this.butUrl = butUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.description);
        dest.writeString(this.butText);
        dest.writeString(this.butUrl);
    }

    public Advertisement() {
    }

    protected Advertisement(Parcel in) {
        this.imageUrl = in.readString();
        this.description = in.readString();
        this.butText = in.readString();
        this.butUrl = in.readString();
    }

    public static final Creator<Advertisement> CREATOR = new Creator<Advertisement>() {
        @Override
        public Advertisement createFromParcel(Parcel source) {
            return new Advertisement(source);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };
}
