package com.myzr.allproduct.entity.http.verifiedcode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Areo
 * @description:验证码返回提
 * @date : 2020/1/1 22:13
{
"status": 200,
"message": "",
"data": "6459d57c4be94a6eb591696702c3ae31"
}
 */
public class VerifiedCodeResponseEntity implements Parcelable{
    private int status;
    private String message;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public VerifiedCodeResponseEntity(Parcel in) {
        this.status=in.readInt();
        this.message=in.readString();
        this.data=in.readString();
    }


    public static final Creator<VerifiedCodeResponseEntity> CREATOR = new Creator<VerifiedCodeResponseEntity>() {
        @Override
        public VerifiedCodeResponseEntity createFromParcel(Parcel in) {
            return new VerifiedCodeResponseEntity(in);
        }

        @Override
        public VerifiedCodeResponseEntity[] newArray(int size) {
            return new VerifiedCodeResponseEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.message);
        dest.writeString(this.data);
    }
}
