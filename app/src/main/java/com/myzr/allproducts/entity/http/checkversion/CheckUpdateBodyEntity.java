package com.myzr.allproducts.entity.http.checkversion;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:
 * @date : 2020/1/1 22:13
 * {
 *    “appVer”:”01.00”,
 * “bootadVer”:”01.00”,
 * “inneradVer”:”01.00”
 * }
 */
public class CheckUpdateBodyEntity implements Parcelable{
    private String appVer;
    private String bootadVer;
    private String inneradVer;

    public CheckUpdateBodyEntity(String appVer, String bootadVer, String inneradVer) {
        this.appVer = appVer;
        this.bootadVer = bootadVer;
        this.inneradVer = inneradVer;
    }

    public CheckUpdateBodyEntity(Parcel in) {
        this.appVer=in.readString();
        this.bootadVer = in.readString();
        this.inneradVer = in.readString();
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getBootadVer() {
        return bootadVer;
    }

    public void setBootadVer(String bootadVer) {
        this.bootadVer = bootadVer;
    }

    public String getInneradVer() {
        return inneradVer;
    }

    public void setInneradVer(String inneradVer) {
        this.inneradVer = inneradVer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appVer);
        dest.writeString(this.bootadVer);
        dest.writeString(this.inneradVer);
    }


    public static final Creator<CheckUpdateBodyEntity> CREATOR = new Creator<CheckUpdateBodyEntity>() {
        @Override
        public CheckUpdateBodyEntity createFromParcel(Parcel in) {
            return new CheckUpdateBodyEntity(in);
        }

        @Override
        public CheckUpdateBodyEntity[] newArray(int size) {
            return new CheckUpdateBodyEntity[size];
        }
    };
}
