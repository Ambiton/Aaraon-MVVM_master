package com.goldze.mvvmhabit.entity.http.userinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:注册用户信息接收体Data
 * @date : {
 *   "status": 200,
 *   "message": "成功",
 *   "data": true
 * }
 *
 */
public class RegisterUserInfoResponseEntity implements Parcelable{
    private int status;
    private String message;
    private boolean data;

    protected RegisterUserInfoResponseEntity(Parcel in) {
        status = in.readInt();
        message = in.readString();
        data = in.readByte() != 0;
    }

    public static final Creator<RegisterUserInfoResponseEntity> CREATOR = new Creator<RegisterUserInfoResponseEntity>() {
        @Override
        public RegisterUserInfoResponseEntity createFromParcel(Parcel in) {
            return new RegisterUserInfoResponseEntity(in);
        }

        @Override
        public RegisterUserInfoResponseEntity[] newArray(int size) {
            return new RegisterUserInfoResponseEntity[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.message);
        dest.writeBooleanArray(new boolean[]{this.data});
    }
}
