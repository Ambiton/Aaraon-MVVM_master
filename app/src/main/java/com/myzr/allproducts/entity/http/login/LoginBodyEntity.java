package com.myzr.allproducts.entity.http.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:登录发送体
 * @date : 2020/1/1 22:13
 * {
 *         "smsToken":"1e1c3c4ee4fd418ebbac8b9bc6c38aa4",
 *         "userName":"18518461971",
 *         "password":"222727",
 *         "type":"1"
 *     }
 */
public class LoginBodyEntity implements Parcelable{
    private String smsToken;
    private String userName;
    private String password;
    private String type;

    public LoginBodyEntity(String smsToken, String userName, String password, String type) {
        this.smsToken = smsToken;
        this.userName = userName;
        this.password = password;
        this.type = type;
    }

    protected LoginBodyEntity(Parcel in) {
        smsToken = in.readString();
        userName = in.readString();
        password = in.readString();
        type = in.readString();
    }

    public static final Creator<LoginBodyEntity> CREATOR = new Creator<LoginBodyEntity>() {
        @Override
        public LoginBodyEntity createFromParcel(Parcel in) {
            return new LoginBodyEntity(in);
        }

        @Override
        public LoginBodyEntity[] newArray(int size) {
            return new LoginBodyEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.smsToken);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.type);
    }
}
