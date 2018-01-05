package com.changcai.buyer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2016/12/21.
 */
public class H5Resources implements Serializable, Parcelable , IKeepFromProguard{



    /**
     * name : common
     * version : 0.1
     */

    private String name;
    private String version;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.version);
        dest.writeString(this.url);
    }

    public H5Resources() {
    }

    protected H5Resources(Parcel in) {
        this.name = in.readString();
        this.version = in.readString();
        this.url = in.readString();
    }

    public static final Creator<H5Resources> CREATOR = new Creator<H5Resources>() {
        @Override
        public H5Resources createFromParcel(Parcel source) {
            return new H5Resources(source);
        }

        @Override
        public H5Resources[] newArray(int size) {
            return new H5Resources[size];
        }
    };
}
