package com.myzr.allproduct.entity.http.register;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:注册接收体Data
 * @date : 2020/1/1 22:13
 *             "data": {
 *               "userId": 6,
 *               "token": "aeb80a93ac01aa6fe1a1314fc7b7cbda"
 *              }
 *
 */
public class RegisterResponseDataEntity implements Parcelable{
    private int userId;
    private String token;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected RegisterResponseDataEntity(Parcel in) {
        userId = in.readInt();
        token = in.readString();
    }

    public static final Creator<RegisterResponseDataEntity> CREATOR = new Creator<RegisterResponseDataEntity>() {
        @Override
        public RegisterResponseDataEntity createFromParcel(Parcel in) {
            return new RegisterResponseDataEntity(in);
        }

        @Override
        public RegisterResponseDataEntity[] newArray(int size) {
            return new RegisterResponseDataEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.token);
    }
}
