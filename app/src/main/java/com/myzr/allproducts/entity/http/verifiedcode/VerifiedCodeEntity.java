package com.myzr.allproducts.entity.http.verifiedcode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:验证码发送体
 * @date : 2020/1/1 22:13
 */
public class VerifiedCodeEntity  implements Parcelable{
    private String mobile;
    private String used;

    public VerifiedCodeEntity(String mobile, String used) {
        this.mobile = mobile;
        this.used = used;
    }

    protected VerifiedCodeEntity(Parcel in) {
        mobile = in.readString();
        used = in.readString();
    }

    public static final Creator<VerifiedCodeEntity> CREATOR = new Creator<VerifiedCodeEntity>() {
        @Override
        public VerifiedCodeEntity createFromParcel(Parcel in) {
            return new VerifiedCodeEntity(in);
        }

        @Override
        public VerifiedCodeEntity[] newArray(int size) {
            return new VerifiedCodeEntity[size];
        }
    };

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mobile);
        dest.writeString(this.used);
    }
}
